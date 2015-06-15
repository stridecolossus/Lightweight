package org.sarge.jove.audio;

import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alListener;

import java.nio.FloatBuffer;

import org.sarge.jove.geometry.Point;
import org.sarge.jove.geometry.Vector;
import org.sarge.jove.util.BufferFactory;

/**
 * LWJGL/OpenAL implementation.
 */
public final class LightweightAudioListener implements AudioListener {
	@Override
	public void setPosition(Point pos) {
		// TODO - to helpers
		final FloatBuffer b = BufferFactory.createFloatBuffer(3);
		pos.append(b);
		b.flip();
		alListener(AL_POSITION, b);
	}

	@Override
	public void setVelocity(Vector v) {
		final FloatBuffer b = BufferFactory.createFloatBuffer(3);
		v.append(b);
		b.flip();
		alListener(AL_VELOCITY, b);
	}

	@Override
	public void setOrientation(Vector dir, Vector up) {
		final FloatBuffer b = BufferFactory.createFloatBuffer(6);
		dir.append(b);
		dir.append(b);
		b.flip();
		alListener(AL_ORIENTATION, b);
	}
}
