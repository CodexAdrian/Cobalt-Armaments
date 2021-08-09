#version 150

in vec4 color;
in vec2 uv;

out vec4 fragColor;

uniform float GameTime;

void main() {
    float fluctator = 1;// .3 * sin(GameTime * 1000) * 0.5 + 0.5;
    fragColor = vec4(color.rgb, min(color.a * (1 - length(uv - 0.5) * 3 * fluctator) - .5, 1 - length(uv - 0.5)));
    //fragColor = vec4(vec3(sin(GameTime * 1000)), 1.0);
}
