package rendering;

// Base class for a render pass
public abstract class RenderPass {
	
	abstract void init(Renderer renderer);
	abstract void destroy();
	abstract void render();
}
