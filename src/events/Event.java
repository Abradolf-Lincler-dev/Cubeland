package events;

// Base class for all events
public class Event {
	public enum Type {
		INIT_EVENT,
		
	}
	public final Type type;
	
	public Event( Type type ) {
		this.type = type;
	}
}
