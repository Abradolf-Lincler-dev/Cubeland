package systems;

import java.util.List;

import events.Event;
import game.Chunk;
import game.*;

// Manages saving & loading of scenes
public class SavingSystem extends System {

	private Game game;
	private GameScene scene;
	private Config config;

	@Override
	public void init(Game game, GameScene scene, Config config) {
		this.game = game;
		this.scene = scene;
		this.config = config;
	}

	@Override
	public void destroy() {

	}

	@Override
	public void handleEvents(List<Event> events) {
		for (Event e : events) {
			if (e.type == Event.Type.INIT_EVENT) {

				// generate a debug chunk
				Chunk newChunk = new Chunk();

				short dirtID = config.findBlock("dirt").id;
				for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
					for (int y = 0; y < Chunk.CHUNK_SIZE / 2; y++) {
						for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
							newChunk.blocks[x][y][z] = dirtID;
						}
					}
				}
				newChunk.blocks[4][8][8] = dirtID;
				scene.chunks.add(newChunk);
			}
		}
	}

	@Override
	public void update(double delta) {

		{// Print scene into console
			Chunk tmpChunk = scene.chunks.get(0);
			String buffer = "";
			for (int y = Chunk.CHUNK_SIZE - 1; y >= 0; y--) {
				buffer += '\n';
				for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
					buffer += " " + tmpChunk.blocks[x][y][Chunk.CHUNK_SIZE / 2];
				}
			}
			java.lang.System.out.println(buffer);
		}
	}

}
