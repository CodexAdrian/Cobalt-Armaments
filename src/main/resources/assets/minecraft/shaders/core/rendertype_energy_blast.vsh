#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;

out vec4 color;
out vec2 uv;

uniform mat4 ModelViewMat, ProjMat;

void main() {
    color = Color;
    uv = UV0;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}
