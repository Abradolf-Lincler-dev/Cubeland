import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Game {

	// constant stuff
	private static final double TARGET_FPS = 60.;
	private static final double TARGET_TIME = 1. / TARGET_FPS;

	// runtime variables
	private boolean running = false;

	// windowing
	private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
	private long window;
	private WindowCloseCallbackI windowCloseCallback = new WindowCloseCallbackI(this);

	private void startGame() {
		init();
		try {
			gameLoop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		destroy();
	}

	private void init() {
		// glfw error callback
		GLFW.glfwSetErrorCallback(errorCallback);

		// initialize GLFW
		if (!GLFW.glfwInit()) {
			GLFW.glfwTerminate();
			throw new IllegalStateException("Unable to initialize GLFW!");
		}

		// create windowglfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		window = GLFW.glfwCreateWindow(800, 600, "Cubeland", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();

		// glfw callbacks
		GLFW.glfwSetWindowCloseCallback(window, windowCloseCallback);

		// initialize own stuff

		// ready
		running = true;
	}

	private void destroy() {
		// destroy own stuff

		// destroy window
		GLFW.glfwDestroyWindow(window);
		Callbacks.glfwFreeCallbacks(window);

		// terminate glfw
		GLFW.glfwTerminate();
		errorCallback.free();
	}

	private void gameLoop() throws InterruptedException {
		double startTime;
		double endTime;
		double deltaTime = 0;

		while (running) {
			startTime = GLFW.glfwGetTime();

			input();
			update(deltaTime);

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			GLFW.glfwSwapBuffers(window);

			endTime = GLFW.glfwGetTime();
			deltaTime = endTime - startTime;
			Thread.sleep((long) (Math.max(TARGET_TIME - deltaTime, 0.) * 1000));
		}
	}

	private void input() {
		GLFW.glfwPollEvents();

	}

	private void update(double delta) {

	}

	private void render() {

	}

	// entry point
	public static void main(String[] args) {
		new Game().startGame();

		System.out.println("exit");
	}

	// callbacks

	class WindowCloseCallbackI extends GLFWWindowCloseCallback {

		private Game gameI;

		WindowCloseCallbackI(Game game) {
			this.gameI = game;
		}

		@Override
		public void invoke(long window) {
			if (window == gameI.window) {
				gameI.running = false;
			}
		}
	}
}
