package events;

public class LoadEvent extends Event {
	public String saveName;

	public LoadEvent(String saveName) {
		super(Type.LOAD);
		this.saveName = saveName;
	}

}
