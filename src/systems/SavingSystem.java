package systems;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.joml.Vector3i;

import events.ChunkChangeEvent;
import events.Event;
import game.Chunk;
import game.*;

// Manages saving & loading of scenes
public class SavingSystem extends System {

	private Game game;
	private GameScene scene;
	private Config config;

	// paths
	String savePath;

	// version
	public final static byte SAVE_VERSION = 1;

	// chunk cache
	List<Chunk> changedChunks = new LinkedList<Chunk>();

	@Override
	public void init(Game game, GameScene scene, Config config) {
		this.game = game;
		this.scene = scene;
		this.config = config;

		savePath = "saves/";
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
				game.createEvent(new ChunkChangeEvent(newChunk));
			} else if (e.type == Event.Type.CHUNK_CHANGE_EVENT) {
				// memorize chunk
				changedChunks.add(((ChunkChangeEvent) e).chunk);

			} else if (e.type == Event.Type.SAVE) {
				while (!changedChunks.isEmpty()) {// iterate until all chunks
													// have been handled
					List<Chunk> chunksToSave = new LinkedList<Chunk>();
					Vector3i region = new Vector3i(changedChunks.get(0).position.mul(1 / Chunk.REGION_CHUNKS));

					String regionStr = region.x() / Chunk.REGION_CHUNKS + "_" + region.y() / Chunk.REGION_CHUNKS + "_"
							+ region.z() / Chunk.REGION_CHUNKS + ".data";

					List<Chunk> regionChunks = loadRegion(regionStr);
					for (Chunk c : changedChunks) {// replace with changed
													// chunks
						if (c.position.mul(1 / Chunk.REGION_CHUNKS).equals(region)) {
							for (Chunk rc : regionChunks)
								if (rc.position.equals(c.position)) {
									regionChunks.remove(rc);
									break;
								}
							regionChunks.add(c);
							changedChunks.remove(c);
						}
					}
					saveRegion(regionStr, regionChunks);
				}
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
					buffer += (tmpChunk.blocks[x][y][Chunk.CHUNK_SIZE / 2] + " ").substring(0, 2);
				}
			}
			java.lang.System.out.println(buffer);
		}
	}

	private List<Chunk> loadRegion(String name) {
		List<Chunk> chunks = new ArrayList<Chunk>();

		return chunks;
	}

	private void saveRegion(String name, List<Chunk> chunks) {
		try {
			// open file
			File file = new File(savePath + name);
			file.getParentFile().mkdirs();
			file.createNewFile();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			out.write(SAVE_VERSION);

			ByteBuffer buffer = ByteBuffer.allocate((int) (Math.pow(Chunk.CHUNK_SIZE, 3) * Short.BYTES * 2));

			for (Chunk c : chunks) {
				// write position
				out.writeInt(c.position.x());
				out.writeInt(c.position.y());
				out.writeInt(c.position.z());

				// write blocks
				for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
					for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
						for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
							buffer.putShort(c.blocks[x][y][z]);
							buffer.putShort(c.blockMeta[x][y][z]);
						}
					}
				}
				buffer.flip();
				out.write(buffer.array());

				// write meta
				for (BlockMetaData md : c.specialMeta) {
					md.save(out);
				}
			}
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
