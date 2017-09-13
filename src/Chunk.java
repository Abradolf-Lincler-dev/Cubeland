import java.util.HashMap;
import java.util.Map;

// All information in one single chunk
public class Chunk {
	public final static int CHUK_SIZE = 16; 
	
	public short[][][] blocks = new short[CHUK_SIZE][CHUK_SIZE][CHUK_SIZE];
	public short[][][] blockMeta = new short[CHUK_SIZE][CHUK_SIZE][CHUK_SIZE];
	public Map<Integer, BlockMetaData> specialMeta = new HashMap<Integer, BlockMetaData>();
	
}
