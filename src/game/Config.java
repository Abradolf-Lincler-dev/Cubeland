package game;

import java.util.HashMap;
import java.util.Map;

// Captures all game configuration
public class Config {
	Map<Short, BlockType> blockMapping = new HashMap<Short, BlockType>();

	// loads general configuration which is independent of a specific session.
	void loadBasicConfig() {

		// load debug stuff
		blockMapping.put((short) 0, new BlockType((short) 0, "void"));
		blockMapping.put((short) 1, new BlockType((short) 1, "dirt"));
	}

	public BlockType findBlock(short id) {
		return blockMapping.get(id);
	}
	public BlockType findBlock(String name) {
		for( BlockType t : blockMapping.values() ) {
			if( t.name == name ) return t;
		}
		return null;
	}
}
