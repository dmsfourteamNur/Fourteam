package fourteam.swagger.parts;

import java.util.HashMap;

public class Parameter {

  String name;
  String in;
  boolean required;

  public Parameter(String name, String in, boolean required) {
    this.name = name;
    this.in = in;
    this.required = required;
  }

  public HashMap<String, Object> toHashMap() {
    HashMap<String, Object> obj = new HashMap<String, Object>();
    obj.put("name", this.name);
    obj.put("in", this.in);
    obj.put("required", this.required);
    HashMap<String, Object> schema = new HashMap<String, Object>();
    schema.put("type", "string");
    obj.put("schema", schema);
    return obj;
  }
}
