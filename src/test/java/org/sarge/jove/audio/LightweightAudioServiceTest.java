package org.sarge.jove.audio;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class LightweightAudioServiceTest {
	private AudioListener service;

	@Before
	public void before() {
		service = new LightweightAudioSystem();
	}

	@After
	public void after() {
		service.stop();
	}
/*
	@Test
	public void setPosition() {
		service.setPosition( new Point( 1, 2, 3 ) );
		final FloatBuffer buffer = BufferUtils.createFloatBuffer( 3 );
		AL10.alGetListener( AL10.AL_POSITION, buffer );
		TestHelper.assertEquals( 1f, buffer.get() );
		TestHelper.assertEquals( 2f, buffer.get() );
		TestHelper.assertEquals( 3f, buffer.get() );
	}

	@Test
	public void createAudioSource() {
		final AudioPlayer src = service.createAudioSource();
		assertNotNull( src );
	}

	@Test
	public void createAudioTrack() throws IOException {
		// Load audio data
		final WaveAudioLoader loader = new WaveAudioLoader();
		final AudioData audio = loader.load( LightweightAudioServiceTest.class.getResourceAsStream( "./audio.wav" ) );

		// Create track
		final AudioTrack track = service.createAudioTrack( audio );
		assertNotNull( track );
	}
	*/
}
