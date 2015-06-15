package org.sarge.jove.model;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.sarge.jove.common.AbstractGraphicResource;
import org.sarge.jove.model.BufferLayout.AccessMode;
import org.sarge.jove.util.LightweightUtil;

/**
 * Lightweight VBO.
 * @author Sarge
 */
public class LightweightVertexBufferObject extends AbstractGraphicResource implements VertexBufferObject {
	/**
	 * Constructor.
	 */
	public LightweightVertexBufferObject() {
		final int id = GL15.glGenBuffers();
		LightweightUtil.checkError();
		setResourceID(id);
	}

	private static void bind(int id) {
		glBindBuffer(GL_ARRAY_BUFFER, id);
		LightweightUtil.checkError();
	}

	@Override
	public void buffer(FloatBuffer buffer, AccessMode mode) {
		glBufferData(GL_ARRAY_BUFFER, buffer, mode.id);
		LightweightUtil.checkError();
	}

	@Override
	public void buffer(FloatBuffer buffer, int offset) {
		glBufferSubData(GL_ARRAY_BUFFER, offset, buffer);
	}

	@Override
	public void activate() {
		bind(super.getResourceID());
	}
	
	@Override
	public void render() {
		// TODO
//		GL11.glFrontFace(GL11.GL_CW);
//		LightweightUtil.checkError();
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		LightweightUtil.checkError();

		final int stride = (3 + 3 + 2) * 4;
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0);
		LightweightUtil.checkError();

		GL20.glEnableVertexAttribArray(1);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, stride, 3 * 4); // 3 components, 4 bytes per float
		LightweightUtil.checkError();

		GL20.glEnableVertexAttribArray(2);
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, (3 * 4) + (3 * 4));
		LightweightUtil.checkError();

		glDrawArrays(GL11.GL_TRIANGLES, 0, 3 * 2 * 6);
		LightweightUtil.checkError();

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		LightweightUtil.checkError();
	}

	@Override
	public void deactivate() {
		bind(0);
		LightweightUtil.checkError();
	}

	@Override
	protected void delete(int id) {
		GL15.glDeleteBuffers(id);
	}
}
