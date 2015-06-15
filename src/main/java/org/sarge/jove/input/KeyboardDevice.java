package org.sarge.jove.input;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.sarge.lib.util.Util;

/**
 * Keyboard device.
 * @author Sarge
 */
public class KeyboardDevice implements Device, KeyEventDispatcher {
	private final List<InputEvent> queue = new ArrayList<>();

	public KeyboardDevice() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	}

	@Override
	public Stream<InputEvent> events() {
		final Stream<InputEvent> stream = new ArrayList<>(queue).stream();
		queue.clear();
		return stream;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		switch(e.getID()) {
		case KeyEvent.KEY_PRESSED:
			generate(EventType.PRESS, e);
			break;

		case KeyEvent.KEY_RELEASED:
			generate(EventType.RELEASE, e);
			break;
		}

		return true;
	}

	/**
	 * Generates an input device for the given key event and delegates to the handler.
	 */
	private void generate(EventType type, KeyEvent e) {
		// Get key and modifiers text
		final int keyCode = e.getKeyCode();
		final int mods = e.getModifiers();
		final String keyText = clean(KeyEvent.getKeyText(keyCode));
		final String modsText = clean(KeyEvent.getKeyModifiersText(mods));

		// Build event name
		final String name;
		if(Util.isEmpty(modsText)) {
			name = keyText;
		}
		else {
			name = modsText + "+" + keyText;
		}

		// Queue event
		final InputEvent event = new InputEvent(this, new EventKey(type, name), null, null);
		queue.add(event);
	}

	/**
	 * Strips white-space.
	 * Event names are used as keys when being persisted.
	 */
	private static String clean(String str) {
		final StringBuilder sb = new StringBuilder();
		for(char ch : str.toCharArray()) {
			if(!Character.isWhitespace(ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
}
