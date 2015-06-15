package org.sarge.jove.scene;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import org.sarge.jove.common.Colour;
import org.sarge.jove.common.Dimensions;
import org.sarge.jove.common.Location;
import org.sarge.jove.common.Rectangle;
import org.sarge.lib.util.ToString;

/**
 * LWJGL viewport.
 * @author Sarge
 */
public class LightweightViewport implements Viewport {
	@Override
	public void init(Rectangle rect) {
		final Location loc = rect.getLocation();
		final Dimensions dim = rect.getDimensions();
		glViewport(loc.getX(), loc.getY(), dim.getWidth(), dim.getHeight());
	}

	@Override
	public void setClearColour(Colour col) {
		glClearColor(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
	}

	@Override
	public void clear(Buffer buffer) {
		glClear(buffer.getValue());
		//		// TODO
		//		glClear( GL_COLOR_BUFFER_BIT );
		//		glClear( GL_DEPTH_BUFFER_BIT );
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
