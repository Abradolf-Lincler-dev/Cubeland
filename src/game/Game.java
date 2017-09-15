package game;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import events.*;
import rendering.Renderer;
import systems.System;
import systems.*;

public class Game {

	// constant stuff
	private static final double TARGET_FPS = 60.;
	private static final double TARGET_TIME = 1. / TARGET_FPS;

	// runtime variables
	private boolean running = false;

	// windowing
	private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(java.lang.System.err);
	private long window;
	private WindowCloseCallbackI windowCloseCallback = new WindowCloseCallbackI(this);

	// configuration
	Config config = new Config();
	
	// game scene
	GameScene scene = new GameScene();

	// rendering
	Renderer renderer = new Renderer(this, scene, config);

	// systems
	List<System> systems = new ArrayList<System>();

	// Events
	List<Event> currentEvents = new LinkedList<Event>();
	List<Event> futureEvents = new LinkedList<Event>();

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
		
		// load configuration
		config.loadBasicConfig();

		// initialize renderer
		renderer.init();

		// add systems
		systems.add(new SavingSystem());
		
		for (System s : systems)
			s.init(this, scene, config);

		// ready
		running = true;
		createEvent(new Event(Event.Type.INIT_EVENT));
	}

	private void destroy() {
		// destroy systems
		for (System s : systems)
			s.destroy();

		// destroy renderer
		renderer.destroy();

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

			GL11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
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
		// swap events
		currentEvents = futureEvents;
		futureEvents = new LinkedList<Event>();

		for (System s : systems) {
			s.handleEvents(currentEvents);
			s.update(delta);
		}
		renderer.handleEvents(currentEvents);
		renderer.update(delta);
	}

	private void render() {
		renderer.render();
	}

	// event management
	public void createEvent(Event newEvent) {
		futureEvents.add(newEvent);
	}

	// entry point
	public static void main(String[] args) {
		new Game().startGame();
		java.lang.System.out.println("finished");
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
