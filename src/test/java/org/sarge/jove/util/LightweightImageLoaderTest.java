package org.sarge.jove.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sarge.jove.common.Dimensions;
import org.sarge.lib.io.FileDataSource;

public class LightweightImageLoaderTest {
	private ImageLoader loader;

	@Before
	public void before() {
		loader = new LightweightImageLoader( new FileDataSource( new File( "./src/test/resources" ) ) );
	}

	@Test
	public void load() throws IOException {
		final TextureImage image = loader.load( "thiswayup.jpg" );
		assertNotNull( image );
		assertEquals( false, image.hasAlpha() );
		assertEquals( new Dimensions( 128, 128 ), image.getDimensions() );
		assertNotNull( image.getBuffer() );
		assertEquals( 128 * 128 * 3, image.getBuffer().limit() );
	}

	@Test(expected=IOException.class)
	public void loadNotFound() throws IOException {
		loader.load( "cobblers.jpg" );
	}
}
