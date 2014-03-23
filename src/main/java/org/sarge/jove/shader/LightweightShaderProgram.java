package org.sarge.jove.shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.sarge.jove.util.JoveUtils;

/**
 * LWJGL implementation.
 */
public final class LightweightShaderProgram extends AbstractShaderProgram {
	public LightweightShaderProgram( Shader[] shaders ) {
		super( shaders );
	}

	@Override
	protected int allocate() {
        return GL20.glCreateProgram();
	}

	@Override
	protected void attach( int program, int shader ) {
		GL20.glAttachShader( program, shader );
	}

	@Override
	protected void link( int program ) {
        // Link program
        GL20.glLinkProgram( program );
        verify( GL20.GL_LINK_STATUS );

        // Validate program
        GL20.glValidateProgram( program );
        verify( GL20.GL_VALIDATE_STATUS );
	}

	/**
	 * Verifies the last operation.
	 */
	private void verify( int pname ) {
		// Verify status
		final int id = super.getResourceID();
        final int result = GL20.glGetProgrami( id, pname );
        if( result == GL11.GL_TRUE ) return;

        // Retrieve log
    	final String log = GL20.glGetProgramInfoLog( id, 1024 );
    	throw new RuntimeException( log );
	}

	@Override
	protected int getParameterCount() {
		return GL20.glGetProgrami( getResourceID(), GL20.GL_ACTIVE_UNIFORMS );
	}

	@Override
	protected ParameterDescriptor getShaderParameterDescriptor( int idx ) {
		// Retrieve parameter details
		final int program = super.getResourceID();
    	final String name = GL20.glGetActiveUniform( program, idx, 128 );
    	final int len = GL20.glGetActiveUniformSize( program, idx );
    	final int type = GL20.glGetActiveUniformType( program, idx );
    	final int loc = GL20.glGetUniformLocation( program, name );

    	// Map parameter type constant to string
    	final String typeName;
		try {
			typeName = JoveUtils.mapIntegerConstant( GL20.class, type );
		}
		catch( Exception e ) {
			throw new RuntimeException( "Cannot lookup type name: " + name, e );
		}
    	if( typeName == null ) throw new UnsupportedOperationException( "Unknown parameter type: " + name );

    	// Create descriptor
    	return new ParameterDescriptor( name, len, typeName, loc );
	}

	@Override
	public void setInteger( int loc, IntBuffer buffer ) {
		GL20.glUniform1( loc, buffer );
	}

	@Override
	public void setFloat( int loc, int size, FloatBuffer buffer ) {
		switch( size ) {
		case 1:
			GL20.glUniform1( loc, buffer );
			break;

		case 2:
			GL20.glUniform2( loc, buffer );
			break;

		case 3:
			GL20.glUniform3( loc, buffer );
			break;

		case 4:
			GL20.glUniform4( loc, buffer );
			break;

		default:
			throw new RuntimeException();
		}
	}

	@Override
	public void setMatrix( int loc, int size, FloatBuffer buffer ) {
		switch( size ) {
		case 2:
			GL20.glUniformMatrix2( loc, false, buffer );
			break;

		case 3:
			GL20.glUniformMatrix3( loc, false, buffer );
			break;

		case 4:
			GL20.glUniformMatrix4( loc, false, buffer );
			break;

		default:
			throw new RuntimeException();
		}
	}

	@Override
	protected void bind( int program ) {
        GL20.glUseProgram( program );
	}

	@Override
	protected void delete( int id ) {
        GL20.glDeleteShader( id );
	}
}
