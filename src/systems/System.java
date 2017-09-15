package systems;
import java.util.List;

import events.Event;
import game.Config;
import game.Game;
import game.GameScene;

// Base class for systems which update the scene.
public abstract class System {

	public abstract void init(Game game, GameScene scene, Config config);

	public abstract void destroy();

	public abstract void handleEvents(List<Event> events);
	
	public abstract void update(double delta);
}
