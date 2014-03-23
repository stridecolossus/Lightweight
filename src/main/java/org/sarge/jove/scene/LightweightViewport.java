package org.sarge.jove.scene;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import org.sarge.jove.common.Colour;
import org.sarge.jove.common.Rectangle;
import org.sarge.lib.util.ToString;

/**
 * LWJGL viewport.
 * @author Sarge
 */
public class LightweightViewport implements Viewport {
	@Override
	public void init( Rectangle rect ) {
		glViewport( rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight() );
	}

	@Override
	public void clear( Colour col ) {
		glClearColor( col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha() );
		glClear( GL_COLOR_BUFFER_BIT );
	}

	@Override
	public void clear() {
		glClear( GL_DEPTH_BUFFER_BIT );
	}

	@Override
	public String toString() {
		return ToString.toString( this );
	}
}
