package org.sarge.jove.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.sarge.jove.common.AbstractPeerObject;
import org.sarge.jove.common.Dimensions;
import org.sarge.jove.texture.Texture.TexturePeer;
import org.sarge.jove.util.LightweightUtil;

/**
 * Lightweight implementation.
 * @author Sarge
 */
public class LightweightTexture extends AbstractPeerObject implements TexturePeer {
	@Override
	public void activateTextureUnit(int unit) {
		GL13.glActiveTexture(unit);
	}
	
	@Override
	public int allocate() {
		return GL11.glGenTextures();
	}
	
	@Override
	public void check() {
		LightweightUtil.checkError();
	}
	
	@Override
	public void setParameter(int target, int name, int value) {
		GL11.glTexParameteri(target, name, value);
	}
	
	@Override
	public void load(int target, TextureImage image) {
		final Dimensions dim = image.getDimensions();
		final int format = image.getFormat();
		GL11.glTexImage2D(target, 0, format, dim.getWidth(), dim.getHeight(), 0, format, GL11.GL_UNSIGNED_BYTE, image.getBuffer());
	}
	
	@Override
	public void generateMipmap(int target) {
		GL30.glGenerateMipmap(target);
	}

	@Override
	public void bind(int target, int id) {
		GL11.glBindTexture(target, id);
	}

	@Override
	public void delete() {
		GL11.glDeleteTextures(getID());
	}
}
