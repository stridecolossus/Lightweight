package org.sarge.jove.audio;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.sarge.jove.animation.AbstractPlayer;
import org.sarge.jove.common.PeerObject;
import org.sarge.jove.geometry.Point;
import org.sarge.jove.geometry.Tuple;
import org.sarge.jove.geometry.Vector;
import org.sarge.lib.util.ToString;

/**
 * OpenAL implementation.
 */
public final class LightweightAudioPlayer extends AbstractPlayer implements AudioPlayer, PeerObject {
	private final int id;
	private final FloatBuffer buffer = BufferUtils.createFloatBuffer( 3 );

	/**
	 * Constructor.
	 */
	public LightweightAudioPlayer() {
		final IntBuffer idBuffer = BufferUtils.createIntBuffer( 1 );
		AL10.alGenSources( idBuffer );
		LightweightAudioSystem.checkError( "Cannot allocate source" );
		this.id = idBuffer.get( 0 );
	}

	@Override
	public int getResourceID() {
		return id;
	}

	@Override
	public void bind( AudioTrack track ) {
		AL10.alSourcei( id, AL10.AL_BUFFER, track.getResourceID() );
		LightweightAudioSystem.checkError( "Error binding source: src=" + this + " track=" + track );
	}

	@Override
	public boolean isPlaying() {
		// TODO - is this correct way to query state?
		return AL10.alGetBoolean( AL10.AL_PLAYING );
	}

	@Override
	public void setPosition( Point pos ) {
		buffer( pos );
		AL10.alSource( id, AL10.AL_POSITION, buffer );
	}

	@Override
	public void setVelocity( Vector v ) {
		buffer( v );
		AL10.alSource( id, AL10.AL_VELOCITY, buffer );
	}

	private void buffer( Tuple t ) {
		buffer.clear();
		t.append( buffer );
		buffer.flip();
	}

	@Override
	public void setPitch( float pitch ) {
		AL10.alSourcef( id, AL10.AL_PITCH, pitch );
	}

	@Override
	public void setVolume( float gain ) {
		AL10.alSourcef( id, AL10.AL_GAIN, gain );
	}

	@Override
	public void setRepeat( boolean repeat ) {
		AL10.alSourcei( id, AL10.AL_LOOPING, repeat ? AL10.AL_TRUE : AL10.AL_FALSE );
	}

	@Override
	public void setState( State state ) {
		switch( state ) {
		case PLAYING:
			AL10.alSourcePlay( id );
			break;

		case PAUSED:
			AL10.alSourcePause( id );
			break;

		case STOPPED:
			AL10.alSourceStop( id );
			break;
		}
		super.setState( state );
	}

	@Override
	public void release() {
		final IntBuffer idBuffer = BufferUtils.createIntBuffer( 1 );
		idBuffer.put( id );
		AL10.alDeleteSources( idBuffer );
		LightweightAudioSystem.checkError( "Cannot release source" );
	}

	@Override
	public String toString() {
		return ToString.toString( this );
	}
}
