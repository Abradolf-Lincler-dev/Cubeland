package events;

// Base class for all events
public class Event {
	public enum Type {
		INIT_EVENT,
		EXIT,
		
		SAVE,
		LOAD,// class LoadEvent
		FAILED_LOAD,// class MessageEvent
		
		CHUNK_CHANGE,// class ChunkChangeEvent
		
		KEY_INPUT,// class KeyInputEvent
	}
	public final Type type;
	
	public Event( Type type ) {
		this.type = type;
	}
	
}
