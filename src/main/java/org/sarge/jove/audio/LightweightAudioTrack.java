package org.sarge.jove.audio;

import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alGenBuffers;

import org.sarge.jove.common.AbstractGraphicResource;
import org.sarge.jove.util.BufferFactory;
import org.sarge.lib.util.ToString;

/**
 * OpenAL implementation.
 */
public class LightweightAudioTrack extends AbstractGraphicResource implements AudioTrack {
	@Override
	public void buffer( AudioData data ) {
		// Allocate ID
		final int id = alGenBuffers();
		super.setResourceID( id );

		// Buffer audio data
		alBufferData( id, data.getFormat(), BufferFactory.createByteBuffer( data.getData() ), data.getSampleRate() );
		LightweightAudioService.checkError( "Error initialising audio buffer" );
	}

	@Override
	protected void delete( int id ) {
		alDeleteBuffers( id );
		LightweightAudioService.checkError( "Error releasing audio buffer" );
	}

	@Override
	public String toString() {
		return ToString.toString( this );
	}
}
