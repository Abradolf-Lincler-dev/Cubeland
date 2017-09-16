package events;

public class MessageEvent extends Event {
	public String message;
	
	public MessageEvent(Type type, String message) {
		super(type);
		this.message = message;
	}

}
