package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import entities.Entity;

// All data about the currently loaded scene.
public class GameScene {
	public String name;// name of this scene (needed for loading)
	public List<Chunk> chunks = new LinkedList<Chunk>();
	public List<Entity> entities = new LinkedList<Entity>();
	public EnvironmentStates envStates = new EnvironmentStates();
	public Map<String, Player> players = new HashMap<String, Player>();// name->player
	public Player currPlayer = null;

	GameScene() {

		// create debug user
		players.put("user", new Player("user"));
	}
}
