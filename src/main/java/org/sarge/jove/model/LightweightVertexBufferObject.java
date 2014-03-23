package org.sarge.jove.model;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.sarge.jove.common.AbstractGraphicResource;

/**
 * Lightweight VBO.
 * @author Sarge
 */
public class LightweightVertexBufferObject extends AbstractGraphicResource implements VertexBufferObject {
	/**
	 * Constructor.
	 */
	protected LightweightVertexBufferObject() {
		final int id = GL15.glGenBuffers();
		setResourceID( id );
	}

	private void bind( int id ) {
		glBindBuffer( GL_ARRAY_BUFFER, id );
	}

	@Override
	public void buffer( FloatBuffer buffer, AccessMode mode ) {
		glBufferData( GL_ARRAY_BUFFER, buffer, mapAccessMode( mode ) );
	}

	static int mapAccessMode( AccessMode mode ) {
		switch( mode ) {
		case STATIC:	return GL15.GL_STATIC_DRAW;
		case STREAM:	return GL15.GL_STREAM_DRAW;
		case DYNAMIC:	return GL15.GL_DYNAMIC_DRAW;
		default:		throw new UnsupportedOperationException( mode.toString() );
		}
	}

	@Override
	public void buffer( FloatBuffer buffer, int offset ) {
		glBufferSubData( GL_ARRAY_BUFFER, offset, buffer );
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
