package org.sarge.jove.util;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.sarge.jove.common.Dimensions;
import org.sarge.lib.io.DataSource;
import org.sarge.lib.util.Check;
import org.sarge.lib.util.ToString;

/**
 * Image loader using AWT and ImageIO.
 * @author Sarge
 */
public class LightweightImageLoader implements ImageLoader {
	private static final ColorSpace SPACE = ColorSpace.getInstance( ColorSpace.CS_sRGB );
	private static final ColorModel TRANSLUCENT = createColourModel( true );
	private static final ColorModel OPAQUE = createColourModel( false );

	/**
	 * Creates OpenGL format colour models.
	 */
	private static ColorModel createColourModel( boolean hasAlpha ) {
		return new ComponentColorModel(
			SPACE,
			new int[]{ 8, 8, 8, hasAlpha ? 8 : 0 },
			hasAlpha,
			false,
			hasAlpha ? ComponentColorModel.TRANSLUCENT : ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE
		);
	}

	/**
	 * Converts the texture image to OpenGL format prior to uploading to the hardware.
	 * @param image		Source image
	 * @param flip		Whether to flip the image vertically
	 * @return OpenGL texture image as a byte-buffer
	 */
	public static TextureImage toBuffer( BufferedImage image, boolean flip ) {
		// Select colour model
		final boolean hasAlpha = image.getColorModel().hasAlpha();
		final ColorModel model = hasAlpha ? TRANSLUCENT : OPAQUE;

		// Create raster
		final Dimensions dim = new Dimensions( image.getWidth(), image.getHeight() );
		final int bands = hasAlpha ? 4 : 3;
		final WritableRaster raster = Raster.createInterleavedRaster( DataBuffer.TYPE_BYTE, dim.getWidth(), dim.getHeight(), bands, null );

		// Create OpenGL image
		final BufferedImage dest = new BufferedImage( model, raster, false, new Hashtable<>() );

		// Draw OpenGL image
		final Graphics g = dest.getGraphics();
		if( flip ) {
			g.drawImage( image, 0, 0, dim.getWidth(), dim.getHeight(), 0, dim.getHeight(), dim.getWidth(), 0, null );
		}
		else {
			g.drawImage( image, 0, 0, null );
		}

		// Retrieve raw image data and convert to buffer
        final DataBufferByte data = (DataBufferByte) dest.getRaster().getDataBuffer();
        final ByteBuffer buffer = BufferFactory.createByteBuffer( data.getData() );

        // Cleanup
        g.dispose();

        // Create image wrapper
        return new TextureImage() {
        	@Override
        	public Dimensions getDimensions() {
        		return dim;
        	}

        	@Override
        	public boolean hasAlpha() {
        		return hasAlpha;
        	}

        	@Override
        	public ByteBuffer getBuffer() {
        		return buffer;
        	}
        };
	}

	private final DataSource src;

	private boolean flip = true;

	/**
	 * Constructor.
	 * @param src	Data-source
	 */
	public LightweightImageLoader( DataSource src ) {
		Check.notNull( src );
		this.src = src;
	}

	@Override
	public void setFlip( boolean flip ) {
		this.flip = flip;
	}

	@Override
	public TextureImage load( String path ) throws IOException {
		final BufferedImage image = ImageIO.read( src.open( path ) );
		return toBuffer( image, flip );
	}

	@Override
	public String toString() {
		return ToString.toString( this );
	}
}
