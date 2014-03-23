package org.sarge.jove.texture;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.sarge.jove.util.LightweightTestRunner;

@Ignore
@RunWith( LightweightTestRunner.class )
public class LightweightTextureTest {
	/*
	private TextureDescriptor info;

	@Before
	public void before() {
		info = new MutableTextureDescriptor( 256, 256 );
	}

	@Test
	public void lifecycle() throws IOException {
		// Load a texture image
		final ImageLoader loader = new ImageLoader();
		final InputStream in = LightweightTextureTest.class.getResourceAsStream( "marble.jpg" );
		final BufferedImage image = loader.load( in );

		// Create texture
		final LightweightTexture tex = new LightweightTexture( image, info );
		assertEquals( true, tex.isActive() );
		assertEquals( true, tex.id > 0 );
		assertEquals( new Dimension( 256, 256 ), tex.getDimension() );
		assertEquals( 256 * 256, tex.getEstimatedSize() );

		// Check textures enabled
		assertEquals( true, GL11.glIsEnabled( GL11.GL_TEXTURE_2D ) );

		// Bind texture and check selected
		tex.select();
		check( GL11.GL_TEXTURE_2D, tex.id );

		// Select default texture
		tex.selectDefault();
		//check( GL11.GL_TEXTURE_2D, 0 );
		// TODO - now this is NOT zero!!!

		// Delete texture
		tex.release();
		assertEquals( false, tex.isActive() );
	}

	// TODO
	@Ignore
	@Test( expected = IllegalArgumentException.class )
	public void invalidDataFormat() {
		final BufferedImage image = new BufferedImage( 4, 4, BufferedImage.TYPE_USHORT_555_RGB );
		new LightweightTexture( image, info );
	}

	@Test( expected = IllegalArgumentException.class )
	public void notSquare() {
		TextureDescriptor.setEnforceSquare( true );
		final BufferedImage image = new BufferedImage( 4, 3, BufferedImage.TYPE_3BYTE_BGR );
		new LightweightTexture( image, info );
	}

	@Test( expected = IllegalArgumentException.class )
	public void notPowerOfTwo() {
		TextureDescriptor.setEnforceSquare( true );
		final BufferedImage image = new BufferedImage( 3, 3, BufferedImage.TYPE_3BYTE_BGR );
		new LightweightTexture( image, info );
	}

	@Test( expected = IllegalArgumentException.class )
	public void alreadyDeleted() {
		final BufferedImage image = new BufferedImage( 4, 4, BufferedImage.TYPE_3BYTE_BGR );
		final Texture texture = new LightweightTexture( image, info );
		texture.release();
		texture.release();
	}

	@Test( expected = IllegalArgumentException.class )
	public void cannotSelect() {
		final BufferedImage image = new BufferedImage( 4, 4, BufferedImage.TYPE_3BYTE_BGR );
		final Texture texture = new LightweightTexture( image, info );
		texture.release();
		texture.select();
	}

	private static void check( int code, int expected ) {
		final IntBuffer buffer = BufferUtils.createIntBuffer( 16 );
		GL11.glGetInteger( code, buffer );
		assertEquals( expected, buffer.get() );
	}
	*/
}
