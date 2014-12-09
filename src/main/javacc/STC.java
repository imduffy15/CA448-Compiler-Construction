public class STC {
    private String type;
    private String identifier;
    private String value;

    public STC(String type, String identifier) {
    	this.type = type;
    	this.identifier = identifier;
    }

    public STC(String type, String identifier, String value) {
		this.type = type;
		this.identifier = identifier;
		this.value = value;
    }

    String getType() {
    	return type;
    }

    void setType(String type) {
    	this.type = type;
    }

    String getIdentifier() {
    	return identifier;
    }

    void setIdentifier(String identifier) {
    	this.identifier = identifier;
    }

    String getValue() {
    	return value;
    }

    void setValue(String value) {
    	this.value = value;
    }

	@Override
	public String toString() {
		return "Identifier: " + identifier + " Type: " + type + " value: " + value;
	}
}
