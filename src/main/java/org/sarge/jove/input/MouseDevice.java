package org.sarge.jove.input;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.sarge.jove.common.Location;
import org.sarge.lib.util.ToString;

/**
 * Mouse device handler.
 * Maps AWT mouse events to generic {@link EventKey}s.
 * @author Sarge
 */
public class MouseDevice extends MouseAdapter implements Device {
	private static final EventType[] TYPES = {
		EventType.PRESS,
		EventType.RELEASE,
		EventType.DOUBLE,
		EventType.DRAG,
		EventType.ZOOM
	};

	/**
	 * Records current drag location.
	 */
	private class DragLocation extends Location {
		private void set( int x, int y ) {
			this.x = x;
			this.y = y;
		}
	}

	private final Frame frame;

	private final DragLocation current = new DragLocation();

	private InputEventHandler handler;

	public MouseDevice( Frame frame ) {
		this.frame = frame;
	}

	@Override
	public EventType[] getEventTypes() {
		return TYPES;
	}

	@SuppressWarnings("hiding")
	@Override
	public void start( InputEventHandler handler ) {
		this.handler = handler;
		frame.addMouseListener( this );
		frame.addMouseMotionListener( this );
		frame.addMouseWheelListener( this );
	}

	@Override
	public void stop() {
		frame.removeMouseListener( this );
		frame.removeMouseMotionListener( this );
		frame.removeMouseWheelListener( this );
	}

	@Override
	public void mousePressed( MouseEvent e ) {
		if( e.getClickCount() == 2 ) {
			mouseButton( EventType.DOUBLE, e );
		}
		else {
			mouseButton( EventType.PRESS, e );
		}
	}

	@Override
	public void mouseReleased( MouseEvent e ) {
		mouseButton( EventType.RELEASE, e );
	}

	@Override
	public void mouseDragged( MouseEvent e ) {
		// Calc drag deltas
		// TODO - do we really want to keep this state here?
		final Location drag = Location.get( e.getX() - current.getX(), e.getY() - current.getY() );

		// Update current position
		current.set( e.getX(), e.getY() );

		// Generate drag event
		final String buttons = MouseEvent.getModifiersExText( e.getModifiersEx() );
		final EventKey key = EventKey.POOL.get();
		key.init( EventType.DRAG, buttons );

		// Create event
		final InputEvent event = InputEvent.POOL.get();
		event.init( this, key );
		event.setLocation( drag );

		// Delegate to handler
		handler.handle( event );
	}

	@Override
	public void mouseWheelMoved( MouseWheelEvent e ) {
		// Init event descriptor
		final EventKey key = EventKey.POOL.get();
		key.init( EventType.ZOOM, null );

		// Create event
		final InputEvent event = InputEvent.POOL.get();
		event.init( this, key );
		event.setZoom( e.getWheelRotation() );

		// Delegate to handler
		handler.handle( event );
	}

	/**
	 * Helper - generates a mouse-button event.
	 * @param type	Event type
	 * @param e		AWT mouse event
	 */
	private void mouseButton( EventType type, MouseEvent e ) {
		// Update mouse position
		final int x = e.getX();
		final int y = e.getY();
		final Location loc = Location.get( x, y );
		current.set( x, y );

		// Init event key
		final String buttons = MouseEvent.getMouseModifiersText( e.getModifiers() );
		final EventKey key = EventKey.POOL.get();
		key.init( type, buttons );

		// Create event
		final InputEvent event = InputEvent.POOL.get();
		event.init( this, key );
		event.setLocation( loc );

		// Delegate to handler
		handler.handle( event );
	}

	@Override
	public String toString() {
		return ToString.toString( this );
	}
}
