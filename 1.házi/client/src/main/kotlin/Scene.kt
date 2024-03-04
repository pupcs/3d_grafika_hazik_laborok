import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext){

  val vsIdle = Shader(gl, GL.VERTEX_SHADER, "idle-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val solidProgram = Program(gl, vsIdle, fsSolid)
  val quadGeometry = TexturedQuadGeometry(gl)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame
  var timeLastRendering = 0.0f;

  var movement_index = 0;

  var offseti = 0.0f;
  var movement_offset = 0.0f;

  val asteroidTexture = Texture2D(gl, "media/platformer_sprites_base.png")

  var offset = 0.0f;

  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>) {
    gl.clearColor(0.0f, 0.0f, 0.0f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags
    gl.enable(GL.BLEND)
    gl.blendFunc( GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA)

    val timeAtThisFrame = Date().getTime()
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    timeAtLastFrame = timeAtThisFrame

    //itt lehet lassítani vagy gyorsítani a + ertek modositasaval
    if(t>(timeLastRendering+0.0005f)) {
      timeLastRendering = t;

      offset += 0.05f
      if (offset > 8.1f) offset = 0.0f;
    }
    var offset_n = offset;
    if(movement_index == 1){
      offset+= 0.05f
      offset_n=offset/2 -0.3f
    }
    if(movement_index == 2){
      offset+=0.05f
      offset_n=(offset/2)+4.0f
    }
    if(movement_index == 3){
      offset+=(0.05f*(2.0f/8.0f))
      offset_n=(offset*(6.0f/8.0f))+2.0f
    }
    if(movement_index == 4){
      offset+=0.05f
      offset_n=(offset/2)+4.0f
    }
    if(movement_index == 6){
      offset+=(0.05f*(2.0f/8.0f))
      offset_n=(offset*(6.0f/8.0f))+2.0f
    }

      if (offset_n < 7.9f) offseti = 7.0f;
      if (offset_n < 6.9f) offseti = 6.0f;
      if (offset_n < 5.9f) offseti = 5.0f;
      if (offset_n < 4.9f) offseti = 4.0f;
      if (offset_n < 3.9f) offseti = 3.0f;
      if (offset_n < 2.9f) offseti = 2.0f;
      if (offset_n < 1.9f) offseti = 1.0f;
      if (offset_n < 0.9f) offseti = 0.0f;

      gl.uniform1f(
              gl.getUniformLocation(
                      solidProgram.glProgram,
                      "offset"),
              offseti)

      gl.uniform1f(
              gl.getUniformLocation(
                      solidProgram.glProgram,
                      "movement_offset"),
              movement_offset)

      gl.uniform1i(gl.getUniformLocation(
              solidProgram.glProgram, "material.colorTexture"), 0)
      gl.activeTexture(GL.TEXTURE0)
      gl.bindTexture(GL.TEXTURE_2D, asteroidTexture.glTexture)

      gl.useProgram(solidProgram.glProgram)
      quadGeometry.draw()


    if (keysPressed.contains("A")) {
      movement_offset = 0.0f;
      movement_index = 0;
    }
    if (keysPressed.contains("S")) {
      movement_offset = 1.0f;
      movement_index = 1;
    }
    if (keysPressed.contains("D")) {
      movement_offset = 1.0f;
      movement_index = 2;
    }
    if (keysPressed.contains("F")) {
      movement_offset = 2.0f;
      movement_index = 3;
    }
    if (keysPressed.contains("G")) {
      movement_offset = 3.0f;
      movement_index = 4;
    }
    if (keysPressed.contains("H")) {
      movement_offset = 4.0f;
      movement_index = 5;
    }
    if (keysPressed.contains("J")) {
      movement_offset = 5.0f;
      movement_index = 6;
    }
    if (keysPressed.contains("K")) {
      movement_offset = 6.0f;
      movement_index = 7;
    }
    if (keysPressed.contains("L")) {
      movement_offset = 7.0f;
      movement_index = 8;
    }
  }
}
