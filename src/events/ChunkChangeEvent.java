package events;

import game.Chunk;

// The content of a chunk had changed
public class ChunkChangeEvent extends Event {
	public Chunk chunk;
	
	public ChunkChangeEvent(Chunk chunk) {
		super(Event.Type.CHUNK_CHANGE_EVENT);
	
		this.chunk = chunk;
	}

}
