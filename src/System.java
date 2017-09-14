import java.util.List;

// Base class for systems which update the scene.
public abstract class System {

	abstract void init(Game game, GameScene scene);

	abstract void destroy();

	abstract void handleEvents(List<Event> events);
	
	abstract void update(double delta);
}
