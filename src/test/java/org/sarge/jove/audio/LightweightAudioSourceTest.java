package org.sarge.jove.audio;

import org.junit.Ignore;

@Ignore
public class LightweightAudioSourceTest {
	/*
	@BeforeClass
	public static void beforeClass() {
		AudioSystem.getInstance(); // Ensure OpenAL initialised
	}

	@AfterClass
	public static void afterClass() {
		AudioSystem.getInstance().stop();
	}

	private LightweightAudioPlayer src;
	private AudioTrack track;

	@Before
	public void before() throws IOException {
		// Load an audio file
		final AudioLoader loader = new WaveAudioLoader();
		final AudioData data = loader.load( LightweightAudioSourceTest.class.getResourceAsStream( "audio.wav" ) );

		// Create track
		track = new LightweightAudioTrack( data );
		assertEquals( true, track.getEstimatedSize() > 0 );

		// Create source
		src = new LightweightAudioPlayer();
		src.bind( track );
	}

	@After
	public void after() {
		src.release();
		track.release();
	}

	@Test
	public void properties() {
		src.setPitch( 0.5f );
		src.setVolume( 0.5f );
		src.setPosition( new Point() );
		src.setVelocity( new Vector() );
	}

	@Test
	public void lifecycle() {
		// Check initially not playing
		assertEquals( State.STOPPED, src.getState() );

		// Start playing
		src.setState( State.PLAYING );
		assertEquals( State.PLAYING, src.getState() );

		// Pause it
		src.setState( State.PAUSED );
		assertEquals( State.PAUSED, src.getState() );

		// Un-pause it
		src.setState( State.PLAYING );
		assertEquals( State.PLAYING, src.getState() );

		// Stop it
		src.setState( State.STOPPED );
		assertEquals( State.STOPPED, src.getState() );
	}
*/
}
