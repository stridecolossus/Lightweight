package org.sarge.jove.app;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_GEQUAL;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL14.GL_POINT_DISTANCE_ATTENUATION;
import static org.lwjgl.opengl.GL14.GL_POINT_FADE_THRESHOLD_SIZE;
import static org.lwjgl.opengl.GL14.glPointParameter;
import static org.lwjgl.opengl.GL14.glPointParameterf;
import static org.lwjgl.opengl.GL20.GL_POINT_SPRITE;

import java.awt.Canvas;
import java.awt.Frame;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.sarge.jove.common.FaceCulling;
import org.sarge.jove.input.Device;
import org.sarge.jove.input.KeyboardDevice;
import org.sarge.jove.input.MouseDevice;
import org.sarge.jove.material.BlendProperty;
import org.sarge.jove.material.DepthTestProperty;
import org.sarge.jove.material.PointSpriteProperty;
import org.sarge.jove.model.AbstractMesh;
import org.sarge.jove.model.BufferedMesh;
import org.sarge.jove.model.LightweightMesh;
import org.sarge.jove.scene.LightweightViewport;
import org.sarge.jove.scene.Viewport;
import org.sarge.jove.shader.LightweightShader;
import org.sarge.jove.shader.LightweightShaderProgram;
import org.sarge.jove.shader.Shader;
import org.sarge.jove.shader.Shader.Type;
import org.sarge.jove.shader.ShaderException;
import org.sarge.jove.shader.ShaderProgram;
import org.sarge.jove.texture.LightweightTexture;
import org.sarge.jove.texture.Texture;
import org.sarge.jove.texture.TextureDescriptor;
import org.sarge.jove.util.ImageLoader;
import org.sarge.jove.util.LightweightImageLoader;
import org.sarge.jove.util.LightweightUtil;
import org.sarge.lib.io.DataSource;

/**
 * LWJGL rendering system.
 */
public final class LightweightRenderingSystem implements RenderingSystem {
	private final Device[] devices;

	/**
	 * Constructor.
	 * @param canvas Rendering canvas
	 */
	public LightweightRenderingSystem( Canvas canvas, Frame frame ) {
		// Attach canvas to LWJGL display
		try {
			Display.setParent( canvas );
		}
		catch( LWJGLException e ) {
			throw new RuntimeException( "Error attaching canvas" );
		}

		// Init devices
		devices = new Device[]{ new KeyboardDevice( frame ), new MouseDevice( frame ) };
	}

	@Override
	public void open() {
		try {
			Display.create();
			if( !Display.isCreated() ) throw new RuntimeException( "Cannot open LWJGL display" );
		}
		catch( LWJGLException e ) {
			throw new RuntimeException( "Error opening LWJGL display", e );
		}
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
		return glGetString( GL_VERSION );
	}

	@Override
	public Viewport createViewport() {
		return new LightweightViewport();
	}

	@Override
	public Device[] getDevices() {
		return devices;
	}

	@Override
	public ImageLoader getImageLoader( DataSource src ) {
		return new LightweightImageLoader( src );
	}

	@Override
	public void setFaceCulling( FaceCulling face ) {
		switch( face ) {
		case NONE:
			glDisable( GL_CULL_FACE );
			break;

		case FRONT:
			glEnable( GL_CULL_FACE );
			glCullFace( GL_FRONT );
			break;

		case BACK:
			glEnable( GL_CULL_FACE );
			glCullFace( GL_BACK );
			break;

		case BOTH:
		default:
			glEnable( GL_CULL_FACE );
			glCullFace( GL_FRONT_AND_BACK );
			break;
		}
	}

	@Override
	public void setWindingOrder( boolean order ) {
		GL11.glFrontFace( order ? GL11.GL_CCW : GL11.GL_CW );
	}

	@Override
	public void setDepthTest( DepthTestProperty test ) {
		if( test == null ) {
		    glDisable( GL_DEPTH_TEST );
		}
		else {
		    glEnable( GL_DEPTH_TEST );
		    glDepthFunc( mapDepthTestString( test.getFunction() ) );
//		    glDepthRange( zNear, zFar )
		}
	}

	/**
	 * Maps the given string to an OpenGL depth-test function.
	 */
	private static int mapDepthTestString( String test ) {
		switch( test ) {
		case "<":		return GL_LESS;
		case "<=":		return GL_LEQUAL;
		case "=":		return GL_EQUAL;
		case ">":		return GL_GREATER;
		case ">=":		return GL_GEQUAL;
		case "never":	return GL11.GL_NEVER;
		case "always":	return GL11.GL_ALWAYS;
		default:
			throw new UnsupportedOperationException( "Unsupported depth test: " + test );
		}
	}

	@Override
	public void setWireframeMode( boolean wireframe ) {
		glPolygonMode( GL_FRONT_AND_BACK, wireframe ? GL_LINE : GL_FILL );
	}

	@Override
	public void setBlend( BlendProperty blend ) {
		if( blend == null ) {
			glDisable( GL_BLEND );
		}
		else {
			glEnable( GL_BLEND );
			final int src = mapBlendFunction( blend.getSourceFunction() );
			final int dest = mapBlendFunction( blend.getDestinationFunction() );
			glBlendFunc( src, dest );
		}
	}

	private static int mapBlendFunction( String func ) {
		switch( func ) {
		case "0":		return GL11.GL_ZERO;
		case "1":		return GL11.GL_ONE;
		case "sa":		return GL11.GL_SRC_ALPHA;
		case "1-sa":	return GL11.GL_ONE_MINUS_SRC_ALPHA;
		case "da":		return GL11.GL_DST_ALPHA;
		case "1-da":	return GL11.GL_ONE_MINUS_DST_ALPHA;
		default:		throw new UnsupportedOperationException( func );
		}
	}

	@Override
	public void setPointSprites( PointSpriteProperty sprites ) {
		if( sprites == null ) {
			glDisable( GL_POINT_SPRITE );
		}
		else {
			glEnable( GL_POINT_SPRITE );
			glPointParameter( GL_POINT_DISTANCE_ATTENUATION, sprites.getAttenuation() );
			glPointParameterf( GL_POINT_FADE_THRESHOLD_SIZE, sprites.getFadeThreshold() );
			LightweightUtil.checkError();
			// TODO - depth mask( false )?
		}
	}

	@Override
	public Texture createTexture( ByteBuffer[] buffers, TextureDescriptor info ) {
		return new LightweightTexture( buffers, info );
	}

	@Override
	public AbstractMesh createMesh( BufferedMesh mesh ) {
		return new LightweightMesh( mesh );
	}

	@Override
	public Shader createShader( Type type, String code ) throws ShaderException {
		return new LightweightShader( type, code );
	}

	@Override
	public ShaderProgram createShaderProgram( Shader[] shaders ) throws ShaderException {
		return new LightweightShaderProgram( shaders );
	}
}
