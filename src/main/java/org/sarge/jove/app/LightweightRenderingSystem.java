package org.sarge.jove.app;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_POINT_DISTANCE_ATTENUATION;
import static org.lwjgl.opengl.GL14.GL_POINT_FADE_THRESHOLD_SIZE;
import static org.lwjgl.opengl.GL14.glPointParameter;
import static org.lwjgl.opengl.GL14.glPointParameterf;

import java.awt.Canvas;
import java.awt.Frame;
import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.sarge.jove.input.Device;
import org.sarge.jove.input.KeyboardDevice;
import org.sarge.jove.input.MouseDevice;
import org.sarge.jove.model.AbstractMesh;
import org.sarge.jove.model.BufferedMesh;
import org.sarge.jove.model.LightweightVertexBufferObject;
import org.sarge.jove.model.VertexBufferObject;
import org.sarge.jove.scene.LightweightViewport;
import org.sarge.jove.scene.Viewport;
import org.sarge.jove.shader.LightweightShader;
import org.sarge.jove.shader.LightweightShaderProgram;
import org.sarge.jove.shader.Shader.ShaderPeer;
import org.sarge.jove.shader.ShaderProgram.ShaderProgramPeer;
import org.sarge.jove.texture.LightweightTexture;
import org.sarge.jove.texture.Texture.TexturePeer;
import org.sarge.jove.util.ImageLoader;
import org.sarge.jove.util.LightweightImageLoader;
import org.sarge.lib.io.DataSource;

/**
 * LWJGL rendering system.
 */
public final class LightweightRenderingSystem implements RenderingSystem {
	private final KeyboardDevice keyboard = new KeyboardDevice();
	private final MouseDevice mouse = new MouseDevice();

	/**
	 * Constructor.
	 * @param canvas Rendering canvas
	 */
	public LightweightRenderingSystem(Canvas canvas, Frame frame) {
		// Attach canvas
		try {
			Display.create();
			if(!Display.isCreated()) throw new RuntimeException("Cannot open LWJGL display");
			Display.setParent(canvas);
		} catch(LWJGLException e) {
			throw new RuntimeException("Error attaching canvas");
		}

		// Attach devices
		frame.addMouseListener(mouse);
		frame.addMouseMotionListener(mouse);
		frame.addMouseWheelListener(mouse);
	}

	@Override
	public void update() {
		Display.update();
	}

	@Override
	public void close() {
		Display.destroy();
	}

	@Override
	public String getVersion() {
		return glGetString(GL_VERSION);
	}

	@Override
	public Viewport createViewport() {
		return new LightweightViewport();
	}

	@Override
	public Device[] getDevices() {
		return new Device[]{ keyboard, mouse };
	}

	@Override
	public ImageLoader getImageLoader(DataSource src) {
		return new LightweightImageLoader(src);
	}

	@Override
	public void enable(int state, boolean enable) {
		if(enable) {
			glEnable(state);
		}
		else {
			glDisable(state);
		}
	}

	@Override
	public void setFaceCulling(int face) {
		glCullFace(face);
	}

	@Override
	public void setWindingOrder(int order) {
		glFrontFace(order);
	}

	@Override
	public void setDepthTestFunction(int func) {
		glDepthFunc(func);
	}

	@Override
	public void setPolygonMode(int face, int mode) {
		glPolygonMode(face, mode);
	}

	@Override
	public void setBlendFunction(int src, int dest) {
		glBlendFunc(src, dest);
	}

	@Override
	public void setPointSprites(float threshold, FloatBuffer attenuation) {
		glPointParameterf(GL_POINT_FADE_THRESHOLD_SIZE, threshold);
		glPointParameter(GL_POINT_DISTANCE_ATTENUATION, attenuation);
	}

	@Override
	public TexturePeer createTexture() {
		return new LightweightTexture();
	}

	@Override
	public AbstractMesh createMesh(BufferedMesh mesh) {
		// TODO
		//return new LightweightMesh( mesh );
		return null;
	}

	@Override
	public VertexBufferObject createVertexBufferObject() {
		return new LightweightVertexBufferObject();
	}

	@Override
	public ShaderPeer createShader(int type) {
		return new LightweightShader(type);
	}
	
	@Override
	public ShaderProgramPeer createShaderProgram() {
		return new LightweightShaderProgram();
	}
}
