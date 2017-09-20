package game;

public class Logger {
	public enum Level {
		info, warn, error,
	}

	private static Level currentLevel = Level.info;

	public static void log(String text, Level level) {
		if (level.ordinal() >= currentLevel.ordinal()) {
			if (level == Level.info)
				text = "I: " + text;
			else if (level == Level.warn)
				text = "W: " + text;
			else if (level == Level.error)
				text = "E: " + text;

			System.out.println(text);
		}
	}

	public static void info(String text) {
		log(text, Level.info);
	}

	public static void warn(String text) {
		log(text, Level.warn);
	}

	public static void error(String text) {
		log(text, Level.error);
	}

	public void setLevel(Level level) {
		currentLevel = level;
	}
}
