package events;

// Base class for all events
public class Event {
	public enum Type {
		INIT_EVENT, EXIT,

		SAVE, LOAD_SCENE, // class LoadSceneEvent; load profile as in config
		FAILED_LOAD, // class MessageEvent
		NEW_PROFILE_LOADED, // class LoadEvent

		LOAD_CHUNK, // class LoadChunkEvent
		GENERATE_CHUNK, // class GenerateChunkEvent
		
		CHUNK_CHANGED, // class ChunkChangeEvent

		KEY_INPUT,// class KeyInputEvent
	}

	public final Type type;

	public Event(Type type) {
		this.type = type;
	}

}
