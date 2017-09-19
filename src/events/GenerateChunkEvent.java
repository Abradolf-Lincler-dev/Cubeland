package events;

import org.joml.Vector3i;

public class GenerateChunkEvent extends Event {
	public Vector3i position;

	public GenerateChunkEvent(Vector3i position) {
		super(Event.Type.GENERATE_CHUNK);
		this.position = position;
	}

}
