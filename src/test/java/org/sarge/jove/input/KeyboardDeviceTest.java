package org.sarge.jove.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class KeyboardDeviceTest {
	private KeyboardDevice dev;
	private InputEventHandler handler;
	private Component src;
	private Frame frame;

	@Before
	public void before() {
		// Mock AWT
		src = mock( Component.class );
		frame = mock( Frame.class );

		// Star device
		dev = new KeyboardDevice( frame );
		handler = mock( InputEventHandler.class );
		dev.start( handler );
	}

	private KeyEvent createKeyEvent( int id, int mods ) {
		return new KeyEvent( src, id, 0L, mods, KeyEvent.VK_A, 'A' );
	}

	@Test
	public void getEventTypes() {
		final Collection<EventType> types = Arrays.asList( dev.getEventTypes() );
		assertEquals( 2, types.size() );
		assertEquals( true, types.contains( EventType.PRESS ) );
		assertEquals( true, types.contains( EventType.RELEASE ) );
	}

	private void check( EventType type, String name ) {
		final ArgumentCaptor<InputEvent> captor = ArgumentCaptor.forClass( InputEvent.class );
		verify( handler ).handle( captor.capture() );

		final InputEvent event = captor.getValue();
		assertNotNull( event );
		assertEquals( dev, event.getDevice() );
		assertNotNull( event.getEventKey() );
		assertEquals( type, event.getEventKey().getType() );
		assertEquals( name, event.getEventKey().getName() );
		assertEquals( null, event.getLocation() );
		assertEquals( null, event.getZoom() );
	}

	@Test
	public void pressed() {
		final KeyEvent awtEvent = createKeyEvent( KeyEvent.KEY_PRESSED, 0 );
		dev.keyPressed( awtEvent );
		check( EventType.PRESS, "A" );
	}

	@Test
	public void pressedModifiers() {
		final KeyEvent awtEvent = createKeyEvent( KeyEvent.KEY_PRESSED, KeyEvent.SHIFT_DOWN_MASK + KeyEvent.CTRL_DOWN_MASK );
		dev.keyPressed( awtEvent );
		check( EventType.PRESS, "Ctrl+Shift+A" );
	}

	@Test
	public void released() {
		final KeyEvent awtEvent = createKeyEvent( KeyEvent.KEY_RELEASED, 0 );
		dev.keyReleased( awtEvent );
		check( EventType.RELEASE, "A" );
	}
}
