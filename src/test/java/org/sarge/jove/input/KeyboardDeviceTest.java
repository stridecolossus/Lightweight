package org.sarge.jove.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class KeyboardDeviceTest {
	private KeyboardDevice dev;
	private Component src;

	@Before
	public void before() {
		src = mock(Component.class);
		dev = new KeyboardDevice();
	}

	private KeyEvent createKeyEvent(int id, int mods) {
		return new KeyEvent(src, id, 0L, mods, KeyEvent.VK_A, 'A');
	}

	private void check(EventType type, String name) {
		// Check event generated
		final List<InputEvent> events = dev.events().collect(Collectors.toList());
		assertEquals(1, events.size());
		
		// Verify the event descriptor
		final InputEvent event = events.get(0);
		assertNotNull(event);
		assertEquals(dev, event.getDevice());
		assertNotNull(event.getEventKey());
		assertEquals(type, event.getEventKey().getType());
		assertEquals(name, event.getEventKey().getName());
		assertEquals(null, event.getLocation());
		assertEquals(null, event.getZoom());
	}

	@Test
	public void pressed() {
		final KeyEvent awtEvent = createKeyEvent(KeyEvent.KEY_PRESSED, 0);
		dev.dispatchKeyEvent(awtEvent);
		check(EventType.PRESS, "A");
	}

	@Test
	public void pressedModifiers() {
		final KeyEvent awtEvent = createKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.SHIFT_DOWN_MASK + KeyEvent.CTRL_DOWN_MASK);
		dev.dispatchKeyEvent(awtEvent);
		check(EventType.PRESS, "Ctrl+Shift+A");
	}

	@Test
	public void released() {
		final KeyEvent awtEvent = createKeyEvent(KeyEvent.KEY_RELEASED, 0);
		dev.dispatchKeyEvent(awtEvent);
		check(EventType.RELEASE, "A");
	}
}
