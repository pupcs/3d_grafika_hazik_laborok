import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import org.khronos.webgl.Float32Array

import vision.gears.webglmath.Mat4

import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext){

  val vsIdle = Shader(gl, GL.VERTEX_SHADER, "idle-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val solidProgram = Program(gl, vsIdle, fsSolid)
  val quadGeometry = TexturedQuadGeometry(gl)

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsGarish = Shader(gl, GL.FRAGMENT_SHADER, "garish-fs.glsl")
  val garishProgram = Program(gl, vsTrafo, fsGarish)
  val triGeometry = TexturedTriangleGeometry(gl)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame


  val asteroidTexture = Texture2D(gl, "media/asteroid.png")

    var red = 0.0f
  var visible = false

  val modelMatrix = Mat4(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
  )
  
  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>, mouseDown:Boolean) {
    gl.clearColor(0.0f, 0.0f, 0.0f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    gl.enable(GL.BLEND)
    gl.blendFunc( GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA)
    
        gl.uniformMatrix4fv(
          gl.getUniformLocation(
            garishProgram.glProgram,
           "gameObject.modelMatrix"),
          false,
          Float32Array(modelMatrix.storage))

         gl.uniform1f(
            gl.getUniformLocation(
                    garishProgram.glProgram,
                    "red"),
            red)

      gl.uniform1i(gl.getUniformLocation(
              solidProgram.glProgram, "material.colorTexture"), 0)
      gl.activeTexture(GL.TEXTURE0)
      gl.bindTexture(GL.TEXTURE_2D, asteroidTexture.glTexture)


      gl.uniform1i(
              gl.getUniformLocation(
                      solidProgram.glProgram,
                      "mouseY"),
              10)
      gl.uniform1i(
              gl.getUniformLocation(
                      solidProgram.glProgram,
                      "mouseX"),
              10)

      val timeAtThisFrame = Date().getTime()
          val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
          timeAtLastFrame = timeAtThisFrame

          if(keysPressed.contains("D")){
                modelMatrix.translate(0.5f * dt)
          }
          if(keysPressed.contains("A")){
                modelMatrix.translate(-0.5f * dt)
          }
          if(keysPressed.contains("W")){
                modelMatrix.translate(0.0f, 0.5f * dt)
          }
          if(keysPressed.contains("S")){
                modelMatrix.translate(0.0f, -0.5f * dt)
          }
          if(keysPressed.contains("E")){
                if (red<1)red+=0.01f
          }
          if(keysPressed.contains("R")){
                if (red>0)red-=0.01f
          }
          if(keysPressed.contains("Q") ||
             keysPressed.contains("A") ||
             keysPressed.contains("W") ||
             keysPressed.contains("S") ||
             keysPressed.contains("D") ||
             keysPressed.contains("E") ||
             keysPressed.contains("R") ||
                  mouseDown ){
                visible = true
          }else{visible = false}


    gl.useProgram(solidProgram.glProgram)
    quadGeometry.draw()
    if(visible) {
      gl.useProgram(garishProgram.glProgram)
      triGeometry.draw()
    }
  }
}
