package org.sarge.jove.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.sarge.jove.common.AbstractPeerObject;
import org.sarge.jove.shader.Shader.ShaderPeer;

/**
 * LWJGL implementation.
 */
public class LightweightShader extends AbstractPeerObject implements ShaderPeer {
	/**
	 * @return Whether shaders are supported by this hardware
	 * TODO - this sucks
	 */
	public static boolean isSupported() {
		if(!GLContext.getCapabilities().GL_ARB_vertex_shader) return false;
		if(!GLContext.getCapabilities().GL_ARB_fragment_shader) return false;
		return true;
	}

	/**
	 * Constructor.
	 * @param type Shader type
	 * @param code GLSL code
	 * @throws RuntimeException if the GLSL code fails compilation
	 */
	public LightweightShader(int type) {
		super(allocate(type));
	}

	@Override
	public int allocate() {
		throw new UnsupportedOperationException();
	}
	
	private static int allocate(int type) {
		return GL20.glCreateShader(type);
	}
	
	@Override
	public void load(String code) {
		GL20.glShaderSource(id, code);
	}
	
	@Override
	public void compile() {
		GL20.glCompileShader(id);
		final int result = GL20.glGetShader(id, GL20.GL_COMPILE_STATUS);
		if(result == GL11.GL_TRUE) return;
		final String log = GL20.glGetShaderInfoLog(id, 1024);
		throw new RuntimeException(log);
	}
	
//	@Override
//	public void check() {
//		LightweightUtil.checkError();
//	}
	
	@Override
	public void delete() {
		GL20.glDeleteShader(id);
	}
}
