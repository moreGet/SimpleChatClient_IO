package ch.get.common;

public enum WindowProperties {
	ROOT_LAYOUT_WIDTH(675.0),
	ROOT_LAYOUT_HEIGHT(420.0),
	
	INFO_LAYOUT_WIDTH(350.0),
	INFO_LAYOUT_HEIGHT(230.0);
	
	private double value;
	
	private WindowProperties(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}