package org.sarge.jove.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lwjgl.opengl.GL11;

@RunWith(LightweightTestRunner.class)
public class LightweightUtilTest {
	@Test
	public void enable() {
		GL11.glEnable(GL11.GL_LIGHTING);
		assertTrue(GL11.glIsEnabled(GL11.GL_LIGHTING));

		GL11.glDisable(GL11.GL_LIGHTING);
		assertFalse(GL11.glIsEnabled(GL11.GL_LIGHTING));
	}

	@Test
	public void getError() {
		LightweightUtil.getError();
	}
}
