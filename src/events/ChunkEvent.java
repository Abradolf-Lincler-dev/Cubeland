package events;

import game.Chunk;

// The content of a chunk had changed
public class ChunkEvent extends Event {
	public Chunk chunk;
	
	public ChunkEvent(Type type, Chunk chunk) {
		super(type);
	
		this.chunk = chunk;
	}

}
