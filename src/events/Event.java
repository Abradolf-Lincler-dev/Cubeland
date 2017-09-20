package events;

// Base class for all events
public class Event {
	public enum Type {
		INIT_EVENT, EXIT,

		SAVE, LOAD_SCENE, // class LoadSceneEvent; load profile as in config
		FAILED_LOAD, // class MessageEvent
		NEW_PROFILE_LOADED, // class LoadEvent

		LOAD_CHUNK, // class LoadChunkEvent
		GENERATE_CHUNK, // class ChunkPositionEvent
		CHUNK_GENERATED, // class ChunkPositionEvent

		UPDATE_RENDER_MESH, // class ChunkEvent; update mesh of a chunk
		UPDATE_CHUNK, // class ChunkEvent; a chunk was edited
		MOVED_TO_NEW_CHUNK,

		KEY_INPUT,// class KeyInputEvent
	}

	public final Type type;

	public Event(Type type) {
		this.type = type;
	}

}
