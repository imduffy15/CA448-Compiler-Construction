import java.util.*;

public class STC {
    private Token type;
    private Token identifier;
    private String scope;
    private boolean function;
    private Map<String, Object> data;

    public STC(Token identifier, String scope) {
        this.identifier = identifier;
        this.scope = scope;
        this.data = new HashMap<String, Object>();
		this.function = false;
    }

	public STC(Token identifier, String scope, boolean function) {
		this.identifier = identifier;
		this.scope = scope;
		this.data = new HashMap<String, Object>();
		this.function = true;
	}

    public STC(Token identifier, Token type, String scope) {
    	this.identifier = identifier;
        this.type = type;
        this.scope = scope;
        this.data = new HashMap<String, Object>();
		this.function = false;
    }

	public STC(Token identifier, Token type, String scope, boolean function) {
		this.identifier = identifier;
		this.type = type;
		this.scope = scope;
		this.data = new HashMap<String, Object>();
		this.function = function;
	}

	public boolean isFunction() {
		return function;
	}

	public void setFunction(boolean function) {
		this.function = function;
	}

	Token getType() {
    	return type;
    }

    void setType(Token type) {
    	this.type = type;
    }

    Token getIdentifier() {
    	return identifier;
    }

    void setIdentifier(Token identifier) {
    	this.identifier = identifier;
    }

    String getScope() {
        return scope;
    }

    Map getData() {
        return data;
    }

    Object getData(String key) {
        return data.get(key);
    }

    void addData(String key, Object obj) {
        data.put(key, obj);
    }

    void setScope(String scope) {
        this.scope = scope;
    }

	@Override
	public String toString() {
		return type + " " + identifier + " " + scope + " " + data;
	}
}
