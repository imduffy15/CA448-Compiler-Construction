public class STC {
    String type;
    String value;

    public STC(String type, String value) {
	this.type = type;
	this.value = value;
    }

	@Override
	public String toString() {
		return "Type: " + type + " value: " + value;
	}
}
