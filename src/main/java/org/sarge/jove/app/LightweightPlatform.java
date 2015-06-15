package org.sarge.jove.app;

import java.nio.file.Paths;

import org.sarge.jove.audio.AudioSystem;
import org.sarge.jove.audio.LightweightAudioSystem;
import org.sarge.jove.input.Device;
import org.sarge.lib.io.DataSource;
import org.sarge.lib.io.FileDataSource;

/**
 * LWJGL/Desktop platform.
 * @author Sarge
 */
public final class LightweightPlatform implements Platform {
	@Override
	public RenderingSystem getRenderingSystem() {
		// TODO - how to instantiate? create window in here?
		// or have some sort of generic 'canvas' that is injected?
		return null;
	}
	
	@Override
	public AudioSystem getAudioSystem() {
		return new LightweightAudioSystem();
	}
	
	@Override
	public Device[] getDevices() {
		// TODO
		return null;
	}
	
	@Override
	public DataSource getDataSource() {
		return new FileDataSource(Paths.get("./resource")); // TODO
	}
}
