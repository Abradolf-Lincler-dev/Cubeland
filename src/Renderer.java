import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Renderer {
	// render passes
	List<RenderPass> renderPasses = new ArrayList<RenderPass>();

	// scene data & game engine
	Game game;
	GameScene scene;

	Renderer(Game game, GameScene scene) {
		this.game = game;
		this.scene = scene;

		// create renderpasses

	}

	void init() {
		for (RenderPass rp : renderPasses) {
			rp.init();
		}
	}

	void destroy() {
		for (RenderPass rp : renderPasses) {
			rp.destroy();
		}
	}

	void handleEvents(List<Event> events) {

	}

	void update(double delta) {

	}

	void render() {
		for (RenderPass rp : renderPasses) {
			rp.render();
		}
	}

}
