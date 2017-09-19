package systems;

import java.util.List;

import org.joml.Vector3i;

import events.ChunkChangeEvent;
import events.Event;
import events.GenerateChunkEvent;
import game.Chunk;
import game.Config;
import game.Game;
import game.GameScene;

public class WorldGeneratorSystem extends System {

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
			if (e.type == Event.Type.GENERATE_CHUNK) {
				Chunk newChunk = generateChunk(((GenerateChunkEvent) e).position);
				game.createEvent(new ChunkChangeEvent(newChunk));
			}
		}

	}

	@Override
	public void update(double delta) {

	}

	private Chunk generateChunk(Vector3i position) {
		Chunk chunk = new Chunk();

		// generate a debug chunk
		short dirtID = config.findBlock("dirt").id;
		for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
			for (int y = 0; y < Chunk.CHUNK_SIZE / 2; y++) {
				for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
					chunk.blocks[x][y][z] = dirtID;
				}
			}
		}
		chunk.blocks[4][8][8] = dirtID;
		scene.chunks.add(chunk);

		java.lang.System.out.println("Generated new Chunk at " + position.x + ", " + position.y + ", " + position.z);

		return chunk;
	}

}