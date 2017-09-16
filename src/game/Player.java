package game;

import org.joml.Vector3f;

// Player data
public class Player {
	public String name;
	public Vector3f position = new Vector3f();
	
	public Player(String name) {
		this.name = name;
	}
}
