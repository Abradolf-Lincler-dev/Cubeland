package systems;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import org.joml.Vector3i;

import events.ChunkEvent;
import events.Event;
import events.ChunkPositionEvent;
import events.LoadChunkEvent;
import events.LoadSceneEvent;
import events.MessageEvent;
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
	public final static byte SAVE_VERSION_META = 1;
	public final static byte SAVE_VERSION_PLAYER = 1;

	// chunk cache
	List<Chunk> changedChunks = new LinkedList<Chunk>();// chunks which were
														// edited
	Map<Vector3i, Chunk> chunkBuffer = new HashMap<Vector3i, Chunk>();// chunks
																		// of
																		// loaded
																		// regions
	List<String> loadedRegions = new ArrayList<String>();// List of loaded
															// region-names
															// TODO: unload if
															// needed
	List<Vector3i> orderedChunks = new LinkedList<Vector3i>();// Chunks which
																// will be
																// created

	// timer
	double saveTimer;// memorize time to next save

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
				game.createEvent(new LoadSceneEvent("default"));
				game.createEvent(new Event(Event.Type.MOVED_TO_NEW_CHUNK));
			} else if (e.type == Event.Type.UPDATE_CHUNK) {
				// memorize chunk
				changedChunks.add(((ChunkEvent) e).chunk);
			} else if (e.type == Event.Type.SAVE) {
				Logger.info("Saving...");
				saveMetaData(scene.envStates.levelName);
				savePlayerData(scene.players.get(config.playername), scene.envStates.levelName);

				int savedChunksCount = changedChunks.size();

				while (!changedChunks.isEmpty()) {// iterate until all chunks
													// have been handled
					Vector3i region = new Vector3i(changedChunks.get(0).position.mul(1 / Chunk.REGION_CHUNKS));

					String regionStr = region.x() / Chunk.REGION_CHUNKS + "_" + region.y() / Chunk.REGION_CHUNKS + "_"
							+ region.z() / Chunk.REGION_CHUNKS;

					List<Chunk> regionChunks;
					regionChunks = loadRegion(regionStr, scene.envStates.levelName);

					for (Chunk c : changedChunks) {// replace with changed
													// chunks
						if (c.position.mul(1 / Chunk.REGION_CHUNKS).equals(region)) {
							for (Chunk rc : regionChunks)
								if (rc.position.equals(c.position)) {
									regionChunks.remove(rc);
									break;
								}
							regionChunks.add(c);
						}
					}
					changedChunks.removeAll(regionChunks);
					saveRegion(regionStr, scene.envStates.levelName, regionChunks);
				}
				Logger.info("Saved (" + savedChunksCount + " saved)");
			} else if (e.type == Event.Type.LOAD_SCENE) {
				String saveName = ((LoadSceneEvent) e).saveName;
				Logger.info("Load scene '" + saveName + "'");
				scene.name = saveName;

				loadMetaData(saveName);
				Player tmpPlayer = loadPlayerData(config.playername, saveName);
				if (tmpPlayer == null) {
					tmpPlayer = new Player(config.playername);
				}
				scene.currPlayer = tmpPlayer;
				scene.players.clear();
				scene.players.put(tmpPlayer.name, tmpPlayer);

				Logger.info("Loaded scene");
			} else if (e.type == Event.Type.LOAD_CHUNK) {
				List<Vector3i> chunksToLoad = ((LoadChunkEvent) e).chunksToLoad;

				for (Vector3i c : chunksToLoad) {
					if (chunkBuffer.containsKey(c)) {// already buffered
						Chunk tmpChunk = chunkBuffer.get(c);
						scene.chunks.add(tmpChunk);
						game.createEvent(new ChunkEvent(Event.Type.UPDATE_RENDER_MESH, tmpChunk));
						Logger.info("Chunk loaded: " + c.x + ", " + c.y + ", " + c.z);
					} else {// Load/create chunk
						String regionStr = ((int) c.x()) / Chunk.REGION_CHUNKS + "_"
								+ ((int) c.y()) / Chunk.REGION_CHUNKS + "_" + ((int) c.z()) / Chunk.REGION_CHUNKS;

						// Search for region in loaded regions
						boolean found = false;
						for (String s : loadedRegions)
							if (s.equals(regionStr)) {
								found = true;
								break;
							}

						if (found) {// generate a new chunk, because region does
									// not contain it
							game.createEvent(new ChunkPositionEvent(Event.Type.GENERATE_CHUNK, c));
							orderedChunks.add(c);
						} else {// Load new region to find chunks
							List<Chunk> tmpChunks = loadRegion(regionStr, scene.name);

							// Find chunk we're searching for & add region to
							// chunk buffer
							Chunk missingChunk = null;
							for (Chunk tmpC : tmpChunks) {
								if (tmpC.position.equals(c))
									missingChunk = tmpC;
								chunkBuffer.put(tmpC.position, tmpC);
							}
							loadedRegions.add(regionStr);

							if (missingChunk == null) {// generate new chunk
								game.createEvent(new ChunkPositionEvent(Event.Type.GENERATE_CHUNK, c));
								orderedChunks.add(c);
							} else {
								game.createEvent(new ChunkEvent(Event.Type.UPDATE_RENDER_MESH, missingChunk));
								Logger.info("Chunk loaded: " + c.x + ", " + c.y + ", " + c.z);
							}
						}
					}
				}

			} else if (e.type == Event.Type.CHUNK_GENERATED) {
				// remove ordered chunks
				Vector3i pos = ((ChunkPositionEvent) e).position;
				for (Vector3i c : orderedChunks)
					if (c.equals(pos)) {
						orderedChunks.remove(c);
						break;
					}
			} else if (e.type == Event.Type.MOVED_TO_NEW_CHUNK) {
				// TODO: check for surrounding chunks to load/unload
				List<Vector3i> chunksToLoad = new ArrayList<Vector3i>();
				chunksToLoad.add(new Vector3i(0, 0, 0));
				chunksToLoad.add(new Vector3i(1, 0, 0));
				chunksToLoad.add(new Vector3i(-1, 0, 0));
				chunksToLoad.add(new Vector3i(0, 0, 1));
				chunksToLoad.add(new Vector3i(0, 0, -1));
				game.createEvent(new LoadChunkEvent(chunksToLoad));
			}

		}
	}

	@Override
	public void update(double delta) {

		saveTimer += delta;
		if (saveTimer > 10) {// in seconds TODO set 60
			saveTimer -= 10;
			game.createEvent(new Event(Event.Type.SAVE));
		}

		if (false) {// Print scene into console
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

	private void loadMetaData(String saveName) {
		try {
			// open file
			File file = new File(savePath + saveName + "/meta.data");
			DataInputStream in = new DataInputStream(new FileInputStream(file));

			byte version = (byte) in.read();
			if (version != SAVE_VERSION_META) {
				Logger.error("Version of meta file is not correct!");
				game.createEvent(new MessageEvent(Event.Type.FAILED_LOAD,
						"Incompatible version '" + version + "'. Can't load file (meta)."));
			}

			// load data
			String name = in.readUTF();
			if (!name.equals(saveName)) {
				Logger.error("Metafile is corrupted: name");
				game.createEvent(new MessageEvent(Event.Type.FAILED_LOAD, "Metadata of save is corrupted: name"));
			}
			scene.envStates.levelName = name;

			in.close();
		} catch (IOException e1) {
			Logger.warn("Meta could not be loaded!");
		}
	}

	private Player loadPlayerData(String playerName, String saveName) {
		Player newPlayer = null;
		try {
			// open file
			File file = new File(savePath + saveName + "/player/" + playerName + ".data");
			DataInputStream in = new DataInputStream(new FileInputStream(file));

			byte version = (byte) in.read();
			if (version != SAVE_VERSION_PLAYER) {
				Logger.error("Version of player file is not correct!");
				game.createEvent(new MessageEvent(Event.Type.FAILED_LOAD,
						"Incompatible version '" + version + "'. Can't load file (player)."));
			}

			// load data
			String name = in.readUTF();
			if (!name.equals(playerName)) {
				Logger.error("Playerfile is corrupted: name");
				game.createEvent(new MessageEvent(Event.Type.FAILED_LOAD, "Playerdata of save is corrupted: name"));
			}
			newPlayer = new Player(name);

			newPlayer.position.x = in.readFloat();
			newPlayer.position.y = in.readFloat();
			newPlayer.position.z = in.readFloat();

			in.close();
		} catch (IOException e1) {
			Logger.warn("Player data could not be loaded!");
		}
		return newPlayer;
	}

	private List<Chunk> loadRegion(String regionName, String saveName) {
		List<Chunk> chunks = new ArrayList<Chunk>();
		try {
			// open file
			File file = new File(savePath + saveName + "/DIM0/" + regionName + ".data");
			DataInputStream in = new DataInputStream(new FileInputStream(file));

			byte version = (byte) in.read();
			if (version != SAVE_VERSION) {
				Logger.error("Version of save file is not correct!");
				game.createEvent(new MessageEvent(Event.Type.FAILED_LOAD,
						"Incompatible version '" + version + "'. Can't load file."));
			}

			// read chunks
			int chunkCount = in.readInt();

			for (int i = 0; i < chunkCount; i++) {
				Chunk newChunk = new Chunk();

				newChunk.position.x = in.readInt();
				newChunk.position.y = in.readInt();
				newChunk.position.z = in.readInt();

				// read blocks
				int chunkSize = in.readInt();
				short singleBlock = in.readShort();
				short singleBlockMeta = 0;
				if (singleBlock != 0)
					singleBlockMeta = in.readShort();

				for (int z = 0; z < chunkSize; z++) {
					for (int y = 0; y < chunkSize; y++) {
						for (int x = 0; x < chunkSize; x++) {
							if (singleBlock == 0) {
								newChunk.blocks[x][y][z] = in.readShort();
								newChunk.blockMeta[x][y][z] = in.readShort();
							} else {
								newChunk.blocks[x][y][z] = singleBlock;
								newChunk.blockMeta[x][y][z] = singleBlockMeta;
							}
						}
					}
				}

				int specialMetaCount = in.readInt();
				for (int j = 0; j < specialMetaCount; j++) {
					String type = in.readUTF();
					newChunk.specialMeta.add(config.metaDataLoader.get(type).load(in, type));
				}
				chunks.add(newChunk);
			}

			in.close();
		} catch (IOException e1) {
			Logger.info("Region could not be loaded (" + regionName + ").");
		}
		return chunks;
	}

	private void saveMetaData(String saveName) {
		try {
			// open file
			File file = new File(savePath + saveName + "/meta.data");
			file.getParentFile().mkdirs();
			file.createNewFile();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

			out.write(SAVE_VERSION_META);

			// save data
			out.writeUTF(scene.envStates.levelName);

			out.close();
		} catch (IOException e1) {
			Logger.error("Meta could not be saved!");
		}
	}

	private void savePlayerData(Player player, String saveName) {
		try {
			// open file
			File file = new File(savePath + saveName + "/player/" + player.name + ".data");
			file.getParentFile().mkdirs();
			file.createNewFile();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

			out.write(SAVE_VERSION_PLAYER);

			// save data
			out.writeUTF(player.name);
			out.writeFloat(player.position.x);
			out.writeFloat(player.position.y);
			out.writeFloat(player.position.z);

			out.close();
		} catch (IOException e1) {
			Logger.error("Player data could not be saved!");
		}
	}

	private void saveRegion(String regionName, String saveName, List<Chunk> chunks) {
		try {
			// open file
			File file = new File(savePath + saveName + "/DIM0/" + regionName + ".data");
			file.getParentFile().mkdirs();
			file.createNewFile();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

			out.write(SAVE_VERSION);

			out.writeInt(chunks.size());

			// write chunks
			ByteBuffer buffer = ByteBuffer.allocate((int) (Math.pow(Chunk.CHUNK_SIZE, 3) * Short.BYTES * 2));
			for (Chunk c : chunks) {
				// write position
				out.writeInt(c.position.x());
				out.writeInt(c.position.y());
				out.writeInt(c.position.z());

				out.writeInt(Chunk.CHUNK_SIZE);

				// write blocks
				short singleBlock = c.blocks[0][0][0];
				short singleBlockMeta = c.blockMeta[0][0][0];
				for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
					for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
						for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
							buffer.putShort(c.blocks[x][y][z]);
							buffer.putShort(c.blockMeta[x][y][z]);
							if (singleBlock != 0
									&& (singleBlock != c.blocks[x][y][z] || singleBlockMeta != c.blockMeta[x][y][z]))
								singleBlock = 0;// found different block.
						}
					}
				}
				buffer.flip();

				out.writeShort(singleBlock);// info about equality of blocks
				if (singleBlock == 0)// different blocks
					out.write(buffer.array());
				else
					out.writeShort(singleBlockMeta);

				// write meta
				out.writeInt(c.specialMeta.size());
				for (BlockMetaData md : c.specialMeta)
					md.save(out);
			}
			out.close();
		} catch (IOException e1) {
			Logger.error("Region could not be saved (" + regionName + ")!");
		}
	}
}
