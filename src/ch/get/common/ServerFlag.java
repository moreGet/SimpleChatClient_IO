package ch.get.common;

public enum ServerFlag {
	JOIN(0),
	QUIT(1),
	SEND(2);
	
	private int flagValue;
	
	private ServerFlag(int value) {
		this.flagValue = value;
	}
	
	public int getFlagValue() {
		return flagValue;
	}
}