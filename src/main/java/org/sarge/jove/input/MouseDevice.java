package org.sarge.jove.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.sarge.jove.common.Location;
import org.sarge.lib.util.ToString;

/**
 * Mouse device handler.
 * Maps AWT mouse events to generic {@link EventKey}s.
 * 
 * TODO
 * - synchronise queue?
 * 
 * @author Sarge
 */
public class MouseDevice extends MouseAdapter implements Device {
	private final List<InputEvent> queue = new ArrayList<>();

	private int x, y;

	@Override
	public Stream<InputEvent> events() {
		final Stream<InputEvent> events = new ArrayList<>(queue).stream();
		queue.clear();
		return events;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getClickCount() == 2) {
			mouseButton(EventType.DOUBLE, e);
		}
		else {
			mouseButton(EventType.PRESS, e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseButton(EventType.RELEASE, e);
	}

	private void updateLocation(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Calc drag deltas
		final Location drag = new Location(e.getX() - x, e.getY() - y);
		updateLocation(e);

		// Create event
		final String buttons = MouseEvent.getModifiersExText(e.getModifiersEx());
		final EventKey key = new EventKey(EventType.DRAG, buttons);
		final InputEvent event = new InputEvent(this, key, drag, null);
		queue.add(event);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		final EventKey key = new EventKey(EventType.ZOOM);
		final InputEvent event = new InputEvent(this, key, null, e.getWheelRotation());
		queue.add(event);
	}

	/**
	 * Helper - generates a mouse-button event.
	 * @param type	Event type
	 * @param e		AWT mouse event
	 */
	private void mouseButton(EventType type, MouseEvent e) {
		// Init mouse position
		updateLocation(e);

		// Create event key descriptor
		final String buttons = MouseEvent.getMouseModifiersText(e.getModifiers());
		final EventKey key = new EventKey(type, buttons);

		// Create event
		final InputEvent event = new InputEvent(this, key, new Location(x, y), null);
		queue.add(event);
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
