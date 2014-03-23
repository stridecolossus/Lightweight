package org.sarge.jove.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.lwjgl.opengl.GLContext;
import org.sarge.jove.util.LightweightUtil;
import org.sarge.lib.util.ToString;

/**
 * Lightweight implementation.
 * @author Sarge
 */
public class LightweightMesh extends AbstractMesh {
	private final int primitive;

	@Override
	public boolean isSupported() {
		// TODO - centralised capabilities
		if( !GLContext.getCapabilities().GL_ARB_vertex_buffer_object ) return false;
		return true;
	}

	/**
	 * Constructor.
	 * @param mesh Mesh data
	 */
	public LightweightMesh( BufferedMesh mesh ) {
		super( mesh );
		this.primitive = mapPrimitive( mesh.getLayout().getPrimitive() );
	}

	/**
	 * Maps primitive to OpenGL code.
	 * @param p Primitive
	 * @return Open GL primitive
	 */
	private static int mapPrimitive( Primitive p ) {
		switch( p ) {
		case POINTS:			return GL_POINTS;
		case LINES:				return GL_LINES;
		case LINE_STRIP:		return GL_LINE_STRIP;
		case TRIANGLES:			return GL_TRIANGLES;
		case TRIANGLE_STRIP:	return GL_TRIANGLE_STRIP;
		case TRIANGLE_FAN:		return GL_TRIANGLE_FAN;
		default: throw new IllegalArgumentException( "Unsupported primitive: " + p );
		}
	}

	@Override
	protected int allocateVAO() {
		return glGenVertexArrays();
	}

	@Override
	protected void verify() {
		LightweightUtil.checkError();
	}

	@Override
	protected void bind( int id ) {
		glBindVertexArray( id );
	}

	@Override
	protected VertexBufferObject createVertexBuffer() {
		return new LightweightVertexBufferObject();
	}

	@Override
	protected void setVertexAttribute( int idx, int size, int stride, int offset ) {
		// TODO - assumes float
		glVertexAttribPointer( idx, size, GL_FLOAT, false, stride, offset );
	}

	@Override
	protected IndexBufferObject createIndexBuffer() {
		return new LightweightIndexBufferObject();
	}

	@Override
	protected void enableVertexAttribute( int idx, boolean enable ) {
		if( enable ) {
			glEnableVertexAttribArray( idx );
		}
		else {
			glDisableVertexAttribArray( idx );
		}
	}

	@Override
	protected void draw( int size, IndexBufferObject indices ) {
		if( indices == null ) {
			glDrawArrays( primitive, 0, size );
		}
		else {
			glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, indices.getResourceID() );
			glDrawElements( primitive, size, GL_UNSIGNED_INT, 0 ); // TODO - others data types?
			glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, 0 );
		}
	}

	@Override
	protected void deleteVAO( int id ) {
		glDeleteVertexArrays( id );
	}

	@Override
	public String toString() {
		return ToString.toString( this );
	}
}
