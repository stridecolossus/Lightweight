package org.sarge.jove.audio;

import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.alGetError;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

/**
 * LWJGL/OpenAL implementation.
 */
public final class LightweightAudioSystem implements AudioSystem {
	private final AudioListener listener = new LightweightAudioListener();

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
	public AudioListener getListener() {
		return listener;
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
