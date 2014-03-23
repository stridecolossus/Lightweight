package org.sarge.jove.model;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;
import org.sarge.jove.common.AbstractGraphicResource;

/**
 * Lightweight index buffer.
 * @author Sarge
 */
public class LightweightIndexBufferObject extends AbstractGraphicResource implements IndexBufferObject {
	/**
	 * Constructor.
	 */
	protected LightweightIndexBufferObject() {
		final int id = GL15.glGenBuffers();
		setResourceID( id );
	}

	private void bind( int id ) {
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, id );
	}

	@Override
	public void buffer( IntBuffer buffer, AccessMode mode ) {
		glBufferData( GL_ELEMENT_ARRAY_BUFFER, buffer, LightweightVertexBufferObject.mapAccessMode( mode ) );
	}

	@Override
	public void buffer( IntBuffer buffer, int offset ) {
		GL15.glBufferSubData( GL_ELEMENT_ARRAY_BUFFER, offset, buffer );
	}

	@Override
	public void activate() {
		bind( super.getResourceID() );
	}

	@Override
	public void deactivate() {
		bind( 0 );
	}

	@Override
	protected void delete( int id ) {
		GL15.glDeleteBuffers( id );
	}
}
