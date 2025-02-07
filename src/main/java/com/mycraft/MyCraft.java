package com.mycraft;

import com.mycraft.config.MainConfig;
import com.mycraft.domain.ChunkRenderer;
import com.mycraft.domain.MyCraftConfig;
import com.mycraft.domain.Texture;
import com.mycraft.shader.ShaderProgram;
import com.mycraft.utils.ShaderUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MyCraft {

    private static final Logger LOG = Logger.getLogger(MyCraft.class.getName());
    private long window;
    private FloatBuffer projectionBuffer;
    private FloatBuffer viewBuffer;

    private ChunkRenderer chunkRenderer;

    public void run() {
        LOG.info("Starting game with LWJGL ".concat(Version.getVersion()).concat("!"));

        var config = MainConfig.load();

        init(config);
        loop();

    }

    private void init(MyCraftConfig config) {

        var errorCallback = GLFWErrorCallback.createPrint(System.err);
        errorCallback.set();

        if (!glfwInit()) {
            throw new IllegalMonitorStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(config.video.width, config.video.height, "MyCraft Adventure v.0.0.1", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        try (MemoryStack stack = stackPush()) {

            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidmode != null) {
                glfwSetWindowPos(window,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
            }

        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);  // Ativa o Depth Test para evitar problemas de sobreposição
        glEnable(GL_CULL_FACE);   // Ativa Culling para evitar renderização de faces ocultas
        glCullFace(GL_BACK);      // Remove faces traseiras
        glFrontFace(GL_CCW);      // Define que a face frontal será no sentido anti-horário

//        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        createMatrixForProjectionAndVision(config);
//        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.0f, -1.0f, -5.0f); // Move a câmera para ver o bloco

        // Caminhos para os arquivos GLSL
        String vertexShaderCode = ShaderUtils.loadShader("src/main/resources/shaders/vertex.glsl");
        String fragmentShaderCode = ShaderUtils.loadShader("src/main/resources/shaders/fragment.glsl");

        // Cria o programa de shaders
        ShaderProgram shader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
        shader.use();
        shader.setUniformMatrix4f("projection", projectionBuffer);
        shader.setUniformMatrix4f("view", viewBuffer);

        var texture = new Texture("src/main/resources/textures/world/ground.jpg");
        texture.bind();

        chunkRenderer = new ChunkRenderer();
        chunkRenderer.create();

        //habilitando o v-sync
        glfwSwapInterval(1);
        glfwShowWindow(window);

    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        chunkRenderer.render();
    }

    public void cleanup() {
        chunkRenderer.cleanup();
        glfwDestroyWindow(window);
        glfwTerminate();
        GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
        if (errorCallback != null) {
            errorCallback.free();
        }
    }

    private void createMatrixForProjectionAndVision(MyCraftConfig config) {
        // Criando matrizes para Projeção e Visão
        Matrix4f projectionMatrix = new Matrix4f().perspective(
                (float) Math.toRadians(70.0f), // FOV (campo de visão)
                (float) config.video.width / config.video.height, // Aspect Ratio
                0.1f, // Distância mínima de visão
                100.0f // Distância máxima de visão
        );

        Matrix4f viewMatrix = new Matrix4f().lookAt(
                0.0f, 1.0f, 3.0f,  // Posição da câmera
                0.0f, 0.0f, 0.0f,  // Ponto onde a câmera olha
                0.0f, 1.0f, 0.0f   // Para cima (eixo Y)
        );

        // Converter para FloatBuffer para enviar ao shader
        projectionBuffer = BufferUtils.createFloatBuffer(16);
        projectionMatrix.get(projectionBuffer);

        viewBuffer = BufferUtils.createFloatBuffer(16);
        viewMatrix.get(viewBuffer);
    }

    public static void main(String[] args) {
        new MyCraft().run();
    }

}
