package game;

import java.util.LinkedList;
import java.util.List;

import org.joml.*;

// All information in one single chunk
public class Chunk {
	public final static int CHUNK_SIZE = 16; // count of blocks on one side of a
												// chunk.
	public final static int REGION_CHUNKS = 8; // count of chunks on one side
												// of a block.

	public short[][][] blocks = new short[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
	public short[][][] blockMeta = new short[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
	public List<BlockMetaData> specialMeta = new LinkedList<BlockMetaData>();

	public Vector3i position = new Vector3i();

	public BlockMetaData findMeta(Vector3i position) {
		for (BlockMetaData md : specialMeta)
			if (md.position.equals(position))
				return md;
		return null;
	}
}
