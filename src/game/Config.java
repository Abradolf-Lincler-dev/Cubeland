package game;

import java.util.HashMap;
import java.util.Map;

// Captures all game configuration
public class Config {
	public Map<Short, BlockType> blockMapping = new HashMap<Short, BlockType>();
	public Map<String, BlockMetaData> metaDataLoader = new HashMap<String, BlockMetaData>();
	public String playername = "user";

	// loads general configuration which is independent of a specific session.
	void loadBasicConfig() {
		
		// load metadata loader

		// load debug stuff
		blockMapping.put((short) 0, new BlockType((short) 0, "noblock"));// 0 is
																			// constant
																			// and
																			// invalid
																			// for
																			// blocks.
		blockMapping.put((short) 1, new BlockType((short) 1, "void"));
		blockMapping.put((short) 2, new BlockType((short) 2, "dirt"));
	}

	public BlockType findBlock(short id) {
		return blockMapping.get(id);
	}

	public BlockType findBlock(String name) {
		for (BlockType t : blockMapping.values()) {
			if (t.name == name)
				return t;
		}
		return null;
	}
}
