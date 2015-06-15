package org.sarge.jove.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.sarge.jove.common.AbstractPeerObject;
import org.sarge.jove.shader.ShaderProgram.ShaderProgramPeer;
import org.sarge.jove.util.LightweightUtil;

/**
 * LWJGL implementation.
 */
public final class LightweightShaderProgram extends AbstractPeerObject implements ShaderProgramPeer {
	@Override
	public int allocate() {
		return GL20.glCreateProgram();
	}
	
	@Override
	public void check() {
		LightweightUtil.checkError();
	}

	@Override
	public void attach(int shader) {
		GL20.glAttachShader(id, shader);
		check();
	}

	@Override
	public void link() {
		GL20.glLinkProgram(id);
		verify(GL20.GL_LINK_STATUS); // TODO
		check();
	}
	
	@Override
	public void validate() {
		GL20.glValidateProgram(id);
		verify(GL20.GL_VALIDATE_STATUS); // TODO
		check();
	}

	/**
	 * Verifies the last operation.
	 */
	private void verify(int pname) {
		// Verify status
		final int result = GL20.glGetProgrami(id, pname);
		if(result == GL11.GL_TRUE) return;

		// Retrieve log
		final String log = GL20.glGetProgramInfoLog(id, 1024);
		throw new RuntimeException(log);
	}

	@Override
	public ParameterDescriptor[] getParameters() {
		// Create array
		final int num = GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORMS);
check();
		final ParameterDescriptor[] params = new ParameterDescriptor[num];

		// Build parameter descriptors
		for(int n = 0; n < num; ++n) {
			// Retrieve parameter details
			final String name = GL20.glGetActiveUniform(id, n, 128);
			final int len = GL20.glGetActiveUniformSize(id, n);
			final int type = GL20.glGetActiveUniformType(id, n);
			final int loc = GL20.glGetUniformLocation(id, name);
check();

			// Create descriptor
			params[n] = new ParameterDescriptor(name, len, type, loc);
		}

		return params;
	}

	@Override
	public void setInteger(ShaderParameter param, int arg) {
		GL20.glUniform1i(param.getLocation(), arg);
		check();
	}

	@Override
	public void setFloat(ShaderParameter param, float f) {
		GL20.glUniform1f(param.getLocation(), f);
		check();
	}
	
	@Override
	public void setVector(ShaderParameter param) {
		// TODO
	}
	
	@Override
	public void setMatrix(ShaderParameter param) {
		// TODO
		GL20.glUniformMatrix4(param.getLocation(), false, param.getBuffer());
		check();
	}
	
//	@Override
//	public void setFloat(int loc, int size, FloatBuffer buffer) {
//		switch(size) {
//		case 1:
//			GL20.glUniform1(loc, buffer);
//			break;
//
//		case 2:
//			GL20.glUniform2(loc, buffer);
//			break;
//
//		case 3:
//			GL20.glUniform3(loc, buffer);
//			break;
//
//		case 4:
//			GL20.glUniform4(loc, buffer);
//			break;
//
//		default:
//			throw new RuntimeException();
//		}
//	}
//
//	@Override
//	public void setMatrix(int loc, int size, FloatBuffer buffer) {
//		switch(size) {
//		case 2:
//			GL20.glUniformMatrix2(loc, false, buffer);
//			break;
//
//		case 3:
//			GL20.glUniformMatrix3(loc, false, buffer);
//			break;
//
//		case 4:
//			GL20.glUniformMatrix4(loc, false, buffer);
//			break;
//
//		default:
//			throw new RuntimeException();
//		}
//	}

	@Override
	public void bind(int program) {
		GL20.glUseProgram(program);
		check();
	}

	@Override
	public void delete() {
		GL20.glDeleteShader(super.getID());
	}
}
