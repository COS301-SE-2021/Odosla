package code;

import java.io.Serializable;

public class ObjMessage implements Serializable {
    private String name;
    private String type;

    public ObjMessage(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return "Message{name: " + name + ", type: " + type + "}";
    }
}
