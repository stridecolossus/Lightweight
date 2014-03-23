package org.sarge.jove.util;

import java.io.File;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

/**
 * Runner for tests that require an LWJGL context to be active.
 */
public final class LightweightTestRunner extends Runner {
	private final Runner runner;

	public LightweightTestRunner( Class<?> clazz ) throws org.junit.runners.model.InitializationError {
		runner = new BlockJUnit4ClassRunner( clazz );
	}

	@Override
	public Description getDescription() {
		return runner.getDescription();
	}

	@Override
	public void run( RunNotifier notifier ) {
		// TODO
		System.setProperty( "org.lwjgl.librarypath", new File( "target/natives" ).getAbsolutePath() );

		// Lazily create window
		if( !Display.isCreated() ) {
			try {
				Display.create();
			}
			catch( final LWJGLException e ) {
				throw new RuntimeException( e );
			}
		}

		// Delegate
		runner.run( notifier );
	}

	@Override
	public int testCount() {
		return runner.testCount();
	}
}
