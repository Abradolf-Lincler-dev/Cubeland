package events;

public class KeyInputEvent extends Event {
	public int key;// GLFW key code
	public int scancode; // GLFW scancode
	public int action;// GLFW action code
	public int mods; // GLFW mods code
	
	public KeyInputEvent(int key, int scancode, int action, int mods) {
		super(Event.Type.KEY_INPUT);

		this.key = key;
		this.scancode = scancode;
		this.action = action;
		this.mods = mods;
	}

}
