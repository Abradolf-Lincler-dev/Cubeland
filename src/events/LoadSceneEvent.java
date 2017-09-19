package events;

public class LoadSceneEvent extends Event {
	public String saveName;

	public LoadSceneEvent(String saveName) {
		super(Event.Type.LOAD_SCENE);
		this.saveName = saveName;
	}

}
