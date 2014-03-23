package org.sarge.jove.input;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.sarge.jove.input.Device;
import org.sarge.jove.input.EventKey;
import org.sarge.jove.input.EventType;
import org.sarge.jove.input.InputEvent;
import org.sarge.jove.input.InputEventHandler;
import org.sarge.lib.util.Util;

/**
 * Keyboard device.
 * @author Sarge
 */
public class KeyboardDevice extends KeyAdapter implements Device {
	private static final EventType[] TYPES = { EventType.PRESS, EventType.RELEASE };

	private final Frame frame;

	private InputEventHandler handler;

	public KeyboardDevice( Frame frame ) {
		this.frame = frame;
	}

	@Override
	public EventType[] getEventTypes() {
		return TYPES;
	}

	@SuppressWarnings("hiding")
	@Override
	public void start( InputEventHandler handler ) {
		frame.addKeyListener( this );
		this.handler = handler;
	}

	@Override
	public void stop() {
		frame.removeKeyListener( this );
	}

	@Override
	public void keyPressed( KeyEvent e ) {
		generate( EventType.PRESS, e );
	}

	@Override
	public void keyReleased( KeyEvent e ) {
		generate( EventType.RELEASE, e );
	}

	/**
	 * Generates an input device for the given key event and delegates to the handler.
	 */
	private void generate( EventType type, KeyEvent e ) {
		// Get key and modifiers text
		final int keyCode = e.getKeyCode();
		final int mods = e.getModifiers();
		final String keyText = clean( KeyEvent.getKeyText( keyCode ) );
		final String modsText = clean( KeyEvent.getKeyModifiersText( mods ) );

		// Build event name
		final String name;
		if( Util.isEmpty( modsText ) ) {
			name = keyText;
		}
		else {
			name = modsText + "+" + keyText;
		}

		// Re-use event key from pool
		final EventKey key = EventKey.POOL.get();
		key.init( type, name );

		// Re-use event from pool
		final InputEvent event = InputEvent.POOL.get();
		event.init( this, key );

		// Delegate to handler
		handler.handle( event );
	}

	/**
	 * Strips white-space.
	 * Event names are used as keys when being persisted.
	 */
	private static String clean( String str ) {
		final StringBuilder sb = new StringBuilder();
		for( char ch : str.toCharArray() ) {
			if( Character.isWhitespace( ch ) ) continue;
			sb.append( ch );
		}
		return sb.toString();
	}
}
