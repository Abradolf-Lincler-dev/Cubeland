package systems;

import java.util.List;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import events.Event;
import events.KeyInputEvent;
import events.LoadSceneEvent;
import game.Config;
import game.Game;
import game.GameScene;
import game.Logger;

public class InputSystem extends System {

	private Game game;
	private GameScene scene;
	private Config config;

	// console control
	Scanner consoleScanner = new Scanner(java.lang.System.in);

	@Override
	public void init(Game game, GameScene scene, Config config) {
		this.game = game;
		this.scene = scene;
		this.config = config;

	}

	@Override
	public void destroy() {

	}

	@Override
	public void handleEvents(List<Event> events) {
		for (Event e : events) {
			if (e.type == Event.Type.KEY_INPUT) {
				KeyInputEvent evt = (KeyInputEvent) e;
				if (evt.action == GLFW.GLFW_PRESS) {
					if (evt.mods == GLFW.GLFW_MOD_CONTROL && evt.key == GLFW.GLFW_KEY_ENTER) { // command
																								// mode
						java.lang.System.out.print("Enter your command: ");

						String commandLine = consoleScanner.nextLine();
						String command = commandLine;
						if (commandLine.indexOf(' ') != -1)
							commandLine.substring(0, commandLine.indexOf(' '));

						if (command.equals("load")) {
							game.createEvent(new LoadSceneEvent("default"));// TODO
																		// change
																		// default
																		// to
																		// last
																		// (or
																		// sth).
						} else if (command.equals("save")) {
							game.createEvent(new Event(Event.Type.SAVE));
						} else if (command.equals("exit")) {
							game.createEvent(new Event(Event.Type.EXIT));
						} else {// unknown command
							Logger.warn("Unknown command " + command + "");
						}
					}
				}
			}
		}

	}

	@Override
	public void update(double delta) {

	}

}
