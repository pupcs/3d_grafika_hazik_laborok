#version 300 es

precision highp float;

in vec2 tex;

uniform struct{ sampler2D colorTexture; } material;
uniform float offset;
uniform float movement_offset;

out vec4 fragmentColor; //#vec4# A four-element vector [r,g,b,a].; Alpha is opacity, we set it to 1 for opaque.; It will be useful later for transparency.

void main(void) {

    float x = tex.x*(1.0f/8.0f) + (1.0f/8.0f)*offset;
    float y = tex.y*(1.0f/8.0f) + (1.0f/8.0f)*movement_offset;

    vec2 tex2 = vec2(x,y);

  fragmentColor = texture(material.colorTexture, tex2);
}
