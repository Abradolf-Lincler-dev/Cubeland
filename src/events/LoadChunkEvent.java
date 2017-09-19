package events;

import java.util.List;

import org.joml.Vector3i;

public class LoadChunkEvent extends Event {
	public List<Vector3i> chunksToLoad;

	public LoadChunkEvent(List<Vector3i> chunksToLoad) {
		super(Event.Type.LOAD_CHUNK);
		this.chunksToLoad = chunksToLoad;
	}
}
