#version 300 es

precision highp float;

in vec2 tex;

uniform struct{ sampler2D colorTexture; } material;
uniform int mouseY;
uniform int mouseX;

out vec4 fragmentColor; //#vec4# A four-element vector [r,g,b,a].; Alpha is opacity, we set it to 1 for opaque.; It will be useful later for transparency.

void main(void) {
  fragmentColor = vec4(0, 1, 0, 1); //#1, 1, 0, 1# solid green

  fragmentColor = texture(material.colorTexture, tex);	

	fragmentColor.x += float(mouseX/1000);
	fragmentColor.y += float(mouseY/1000);
}
