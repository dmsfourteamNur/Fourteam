package fourteam.swagger.parts;

import java.util.HashMap;

public class Tag {

  String name;
  String description;

  public Tag(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public HashMap<String, Object> toHasMap() {
    HashMap<String, Object> obj = new HashMap<String, Object>();
    obj.put("name", this.name);
    obj.put("description", this.description);
    return obj;
  }
}
