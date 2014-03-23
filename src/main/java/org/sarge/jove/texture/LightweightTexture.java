package org.sarge.jove.texture;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.nio.ByteBuffer;

import org.sarge.jove.common.Dimensions;
import org.sarge.jove.texture.TextureDescriptor.Filter;
import org.sarge.jove.texture.TextureDescriptor.WrapPolicy;
import org.sarge.jove.util.LightweightUtil;
import org.sarge.lib.util.Check;

/**
 * Lightweight implementation.
 * @author Sarge
 */
public class LightweightTexture extends AbstractTexture {
	private int type;

	/**
	 * Constructor.
	 * <p>
	 * Assumes the following regarding the image format:
	 * <ul>
	 * <li>ARGB format</li>
	 * <li>byte data type</li>
	 * <li>no border</li>
	 * </ul>
	 * @param buffers	Texture data
	 * @param info		Descriptor
	 * @throws IllegalArgumentException if the texture cannot be created
	 */
	public LightweightTexture( ByteBuffer[] buffers, TextureDescriptor info ) {
		// Validate buffers
		Check.notEmpty( buffers );
		switch( info.getTextureDimension() ) {
		case 2:
			if( buffers.length != 1 ) throw new IllegalArgumentException( "Expected one buffer" );
			break;

		case 3:
			if( buffers.length != 6 ) throw new IllegalArgumentException( "Expected cube-map" );
			break;

		default:
			throw new UnsupportedOperationException();
		}

		/*
		 * TODO - rendering system should work out whether this needs to be done?
		 *
		 * if( TextureInfo.isEnforceSquare() ) { if( size.width != size.height )
		 * throw new IllegalArgumentException( "Texture not square: " + size );
		 * if( !TextureInfo.isPowerOfTwo( size.width ) ||
		 * !TextureInfo.isPowerOfTwo( size.height ) ) { throw new
		 * IllegalArgumentException( "Texture dimensions not power-of-two: " +
		 * size ); } }
		 */

		// Allocate texture
		final int id = glGenTextures();
		if( id < 1 ) throw new RuntimeException( "Texture not allocated" );
		setResourceID( id );

		// Determine texture type
		switch( info.getTextureDimension() ) {
		case 2:
			type = GL_TEXTURE_2D;
			break;

		case 3:
			type = GL_TEXTURE_CUBE_MAP;
			break;
		}

		// Select texture
		bind( id, 0 );
		LightweightUtil.checkError();

		// Determine OpenGL texture/image format
		final int format = info.isTranslucent() ? GL_RGBA : GL_RGB;

		// Upload texture
		switch( info.getTextureDimension() ) {
		case 2:
			if( buffers[ 0 ] == null ) throw new IllegalArgumentException( "Texture buffer cannot be null" );
			load( GL_TEXTURE_2D, buffers[ 0 ], format, info.getSize() );
			break;

		case 3:
			for( int n = 0; n < buffers.length; ++n ) {
				final int idx = GL_TEXTURE_CUBE_MAP_POSITIVE_X + n;
				if( buffers[ n ] != null ) {
					load( idx, buffers[ n ], format, info.getSize() );
				}
			}
			break;
		}

		// Set wrapping config
		final int wrap = mapWrapPolicy( info.getWrapPolicy() );
		switch( info.getTextureDimension() ) {
		case 3:
			glTexParameteri( type, GL_TEXTURE_WRAP_R, wrap );
			//$FALL-THROUGH$
		case 2:
			glTexParameteri( type, GL_TEXTURE_WRAP_T, wrap );
			//$FALL-THROUGH$
		case 1:
			glTexParameteri( type, GL_TEXTURE_WRAP_S, wrap );
		}
		LightweightUtil.checkError();

		// Set min/mag config
		glTexParameteri( type, GL_TEXTURE_MIN_FILTER, mapMinificationFilter( info.getMinificationFilter(), info.isMipMapped() ) );
		glTexParameteri( type, GL_TEXTURE_MAG_FILTER, mapMagnificationFilter( info.getMagnificationFilter() ) );
		LightweightUtil.checkError();

		// Generate mipmaps as required
		if( info.isMipMapped() ) {
			glGenerateMipmap( type );
			LightweightUtil.checkError();
		}

		// Cleanup
		bind( 0, 0 );
	}

	/**
	 * Uploads texture data.
	 */
	protected void load( int id, ByteBuffer data, int format, Dimensions size ) {
		glTexImage2D( id, 0, format, size.getWidth(), size.getHeight(), 0, format, GL_UNSIGNED_BYTE, data );
		LightweightUtil.checkError();
	}

	/**
	 * Maps wrapping policy to OpenGL identifier.
	 */
	private static int mapWrapPolicy( WrapPolicy wrap ) {
		switch( wrap ) {
		case CLAMP:			return GL_CLAMP;
		case REPEAT:		return GL_REPEAT;
		case CLAMP_TO_EDGE:	return GL_CLAMP_TO_EDGE;
		}

		throw new UnsupportedOperationException( "Unsupported wrap policy: " + wrap );
	}

	/**
	 * Maps magnification filter to OpenGL identifier.
	 */
	private static int mapMagnificationFilter( Filter filter ) {
		switch( filter ) {
		case LINEAR: 	return GL_LINEAR;
		case NEAREST:	return GL_NEAREST;
		}

		throw new UnsupportedOperationException( "Unsupported filter: " + filter );
	}

	/**
	 * Maps minification filter to OpenGL identifier.
	 */
	private static int mapMinificationFilter( Filter filter, boolean mipmapped ) {
		if( mipmapped ) {
			switch( filter ) {
			case LINEAR:	return GL_LINEAR_MIPMAP_LINEAR;
			case NEAREST:	return GL_LINEAR_MIPMAP_NEAREST;
			}
		}
		else {
			return mapMagnificationFilter( filter );
		}

		throw new UnsupportedOperationException( "Unsupported filter: " + filter + "," + mipmapped );
	}

	@Override
	protected void bind( int id, int unit ) {
		glActiveTexture( GL_TEXTURE0 + unit );
		glBindTexture( type, id );
	}

	@Override
	protected void delete( int id ) {
		glDeleteTextures( id );
	}
}
