package org.sarge.jove.audio;

import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alGetError;
import static org.lwjgl.openal.AL10.alListener;

import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.sarge.jove.geometry.Point;
import org.sarge.jove.geometry.Vector;
import org.sarge.jove.util.BufferFactory;

/**
 * LWJGL/OpenAL implementation.
 */
public final class LightweightAudioService implements AudioListener {
	/**
	 * Checks for an error reported by OpenAL.
	 */
	protected static void checkError( String msg ) {
		final int err = alGetError();

		if( err != AL_NO_ERROR ) {
			throw new RuntimeException( "[" + err + "] " + msg );
		}
	}

	@Override
	public void start() {
		// Init OpenAL
		try {
			AL.create();
		}
		catch( LWJGLException e ) {
			throw new RuntimeException( e );
		}

		// Check created
		if( !AL.isCreated() ) {
			throw new RuntimeException( "OpenAL not initialised" );
		}

		// Clear error flag
		alGetError();
	}

	@Override
	public void stop() {
		if( AL.isCreated() ) {
			AL.destroy();
		}
	}

	@Override
	public void setPosition( Point pos ) {
		// TODO - to helpers
		final FloatBuffer b = BufferFactory.createFloatBuffer( 3 );
		pos.append( b );
		b.flip();
		alListener( AL_POSITION, b );
	}

	@Override
	public void setVelocity( Vector v ) {
		final FloatBuffer b = BufferFactory.createFloatBuffer( 3 );
		v.append( b );
		b.flip();
		alListener( AL_VELOCITY, b );
	}

	@Override
	public void setOrientation( Vector dir, Vector up ) {
		final FloatBuffer b = BufferFactory.createFloatBuffer( 6 );
		dir.append( b );
		dir.append( b );
		b.flip();
		alListener( AL_ORIENTATION, b );
	}

	@Override
	public AudioTrack createAudioTrack() {
		return new LightweightAudioTrack();
	}

	@Override
	public AudioPlayer createAudioPlayer() {
		return new LightweightAudioPlayer();
	}
}
