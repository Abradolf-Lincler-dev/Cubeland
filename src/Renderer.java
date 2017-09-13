import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Renderer {
	// render pass
	List<RenderPass> renderPasses = new ArrayList<RenderPass>();
	
	
	Renderer() {
		// create renderpasses
		
		
	}

	void init() {
		for( RenderPass rp : renderPasses ) {
			rp.render();
		}
	}
	void destroy() {
		for( RenderPass rp : renderPasses ) {
			rp.destroy();
		}
	}
	
	void render() {
		for( RenderPass rp : renderPasses ) {
			rp.render();
		}
	}
	
}
