import java.util.LinkedList;
import java.util.List;

// All data about the currently loaded scene.
public class GameScene {
	public List<Chunk> chunks = new LinkedList<Chunk>();
	public List<Entity> entities = new LinkedList<Entity>();
	public Inventory inventory = new Inventory();
	public EnvironmentStates envStates = new EnvironmentStates();

	GameScene() {

	}
}
