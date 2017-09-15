package rendering;
import java.util.ArrayList;
import java.util.List;

import events.Event;
import game.*;

public class Renderer {
	// render passes
	List<RenderPass> renderPasses = new ArrayList<RenderPass>();

	// game data
	Game game;
	GameScene scene;
	Config config;

	public Renderer(Game game, GameScene scene, Config config) {
		this.game = game;
		this.scene = scene;
		this.config = config;

		// create renderpasses

	}

	public void init() {
		for (RenderPass rp : renderPasses) {
			rp.init(this);
		}
	}

	public void destroy() {
		for (RenderPass rp : renderPasses) {
			rp.destroy();
		}
	}

	public void handleEvents(List<Event> events) {

	}

	public void update(double delta) {

	}

	public void render() {
		for (RenderPass rp : renderPasses) {
			rp.render();
		}
	}

}
