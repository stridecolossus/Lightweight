package org.sarge.jove.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sarge.jove.common.Location;

public class MouseDeviceTest {
	private InputEventHandler handler;
	private MouseDevice dev;

	@Before
	public void before() {
		final Frame frame = mock( Frame.class );
		handler = mock( InputEventHandler.class );
		dev = new MouseDevice( frame );
		dev.start( handler );
	}

	private static MouseEvent createEvent( int id ) {
		final Component src = mock( Component.class );
		when( src.getLocationOnScreen() ).thenReturn( new java.awt.Point() );
		return new MouseEvent( src, id, 0L, MouseEvent.BUTTON1_DOWN_MASK, 1, 2, 1, false, 1 );
	}

	@Test
	public void getEventTypes() {
		final Collection<EventType> types = Arrays.asList( dev.getEventTypes() );
		assertEquals( 5, types.size() );
		assertEquals( true, types.contains( EventType.PRESS ) );
		assertEquals( true, types.contains( EventType.RELEASE ) );
		assertEquals( true, types.contains( EventType.DOUBLE ) );
		assertEquals( true, types.contains( EventType.DRAG ) );
		assertEquals( true, types.contains( EventType.ZOOM ) );
	}

	@Test
	public void press() {
		// Generate a press event
		final MouseEvent event = createEvent( MouseEvent.MOUSE_PRESSED );
		dev.mousePressed( event );

		// Check event created and sent to handler
		final ArgumentCaptor<InputEvent> captor = ArgumentCaptor.forClass( InputEvent.class );
		verify( handler ).handle( captor.capture() );

		// Check event
		final InputEvent result = captor.getValue();
		assertNotNull( result );
		assertEquals( dev, result.getDevice() );
		assertNotNull( result.getEventKey() );
		assertEquals( EventType.PRESS, result.getEventKey().getType() );
		assertEquals( "Button1", result.getEventKey().getName() );
		assertEquals( new Location( 1, 2 ), result.getLocation() );
		assertEquals( null, result.getZoom() );
	}

	@Test
	public void drag() {
		// Generate drag event
		final MouseEvent event = createEvent( MouseEvent.MOUSE_DRAGGED );
		dev.mouseDragged( event );

		// Check event created and sent to handler
		final ArgumentCaptor<InputEvent> captor = ArgumentCaptor.forClass( InputEvent.class );
		verify( handler ).handle( captor.capture() );

		// Check event
		final InputEvent result = captor.getValue();
		assertNotNull( result );
		assertEquals( dev, result.getDevice() );
		assertNotNull( result.getEventKey() );
		assertEquals( EventType.DRAG, result.getEventKey().getType() );
		assertEquals( "Button1", result.getEventKey().getName() );
		assertEquals( new Location( 1, 2 ), result.getLocation() );
		assertEquals( null, result.getZoom() );
	}

	@Test
	public void zoom() {
		// Generate mouse-wheel event
		final Component src = mock( Component.class );
		when( src.getLocationOnScreen() ).thenReturn( new java.awt.Point() );
		final MouseWheelEvent event = new MouseWheelEvent( src, MouseEvent.MOUSE_WHEEL, 0L, 0, 0, 0, 0, false, 0, 0, 42 );
		dev.mouseWheelMoved( event );

		// Check event created and sent to handler
		final ArgumentCaptor<InputEvent> captor = ArgumentCaptor.forClass( InputEvent.class );
		verify( handler ).handle( captor.capture() );

		// Check event
		final InputEvent result = captor.getValue();
		assertNotNull( result );
		assertEquals( dev, result.getDevice() );
		assertNotNull( result.getEventKey() );
		assertEquals( EventType.ZOOM, result.getEventKey().getType() );
		assertEquals( null, result.getEventKey().getName() );
		assertEquals( null, result.getLocation() );
		assertEquals( new Integer( 42 ), result.getZoom() );
	}
}
