package events;

import org.joml.Vector3i;

public class ChunkPositionEvent extends Event {
	public Vector3i position;

	public ChunkPositionEvent(Type type, Vector3i position) {
		super(type);
		this.position = position;
	}

}
