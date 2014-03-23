package org.sarge.jove.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Utility methods for LWJGL implementation.
 */
public final class LightweightUtil {
	private LightweightUtil() {
		// Utility class
	}

	/**
	 * @param code OpenGL function
	 * @return Whether the given function is enabled
	 */
	public static boolean isEnabled( int code ) {
		return GL11.glIsEnabled( code );
	}

	/**
	 * @return Current error string or <tt>null</tt> if none
	 */
	public static String getError() {
		final int code = GL11.glGetError();

		if( code == GL11.GL_NO_ERROR ) {
			return null;
		}
		else {
			return GLU.gluErrorString( code );
		}
	}

	/**
	 * Verifies the most recent OpenGL operation.
	 * @throws RuntimeException if an error occurred
	 * @see #getError()
	 */
	public static void checkError() {
		final String err = getError();
		if( err != null ) throw new RuntimeException( "OpenGL error: " + err );
	}
}
