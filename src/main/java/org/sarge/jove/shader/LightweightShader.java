package org.sarge.jove.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GLContext;
import org.sarge.jove.common.AbstractGraphicResource;

/**
 * LWJGL implementation.
 */
public class LightweightShader extends AbstractGraphicResource implements Shader {
    /**
     * @return Whether shaders are supported by this hardware
     * TODO - this sucks
     */
	public static boolean isSupported() {
		if( !GLContext.getCapabilities().GL_ARB_vertex_shader ) return false;
		if( !GLContext.getCapabilities().GL_ARB_fragment_shader ) return false;
		return true;
	}

	/**
	 * Constructor.
	 * @param type Shader type
	 * @param code GLSL code
	 * @throws ShaderException if the GLSL code fails compilation
	 */
	public LightweightShader( Type type, String code ) throws ShaderException {
        // Allocate shader
        final int nativeType = map( type );
        final int id = GL20.glCreateShader( nativeType );
        setResourceID( id );

        // Upload and compile shader program
        GL20.glShaderSource( id, code );
        GL20.glCompileShader( id );

        // Check compilation
        verify( id, GL20.GL_COMPILE_STATUS );
	}

	/**
	 * Maps shader type to underlying OpenGL id.
	 */
	private static int map( Type type ) {
		switch( type ) {
		case VERTEX:		return GL20.GL_VERTEX_SHADER;
		case FRAGMENT:		return GL20.GL_FRAGMENT_SHADER;
		case GEOMETRY:		return GL32.GL_GEOMETRY_SHADER;
		default:			throw new UnsupportedOperationException( type.toString() );
		}
	}

    /**
     * Verifies the given shader.
     * @param id 		Shader ID
     * @param pname		OpenGL flag
     * @throws ShaderException
     * @throws ShaderException if the shader is not valid
     */
    private static void verify( int id, int pname ) throws ShaderException {
    	// Check operation result
    	final int result = GL20.glGetShader( id, pname );
    	if( result == GL11.GL_TRUE ) return;

    	// Retrieve log
    	final String log = GL20.glGetShaderInfoLog( id, 1024 );
    	throw new ShaderException( log );
    }

    @Override
    protected void delete( int id ) {
        GL20.glDeleteShader( id );
	}
}
