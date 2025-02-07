#version 330 core

in vec2 fragTexCoords;         // Coordenadas UV recebidas do vertex shader
out vec4 fragColor;            // Cor final do pixel

uniform sampler2D textureSampler; // Textura associada

void main() {
    fragColor = texture(textureSampler, fragTexCoords); // Pega a cor da textura nas coordenadas UV
}
