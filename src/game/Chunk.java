package game;
import java.util.HashMap;
import java.util.Map;

// All information in one single chunk
public class Chunk {
	public final static int CHUNK_SIZE = 16; 
	
	public short[][][] blocks = new short[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
	public short[][][] blockMeta = new short[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
	public Map<Integer, BlockMetaData> specialMeta = new HashMap<Integer, BlockMetaData>();
	
}
