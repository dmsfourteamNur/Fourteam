package fourteam.swagger.parts;

import java.util.HashMap;
import org.json.JSONObject;

public class Info {

  String description;
  String version;
  String title;

  public Info() {
    this.description = "FuourTeam API description";
    this.version = "1.0.1";
    this.title = "FourTeam API";
  }

  public JSONObject toJSON() {
    JSONObject obj = new JSONObject();
    obj.put("description", this.description);
    obj.put("version", this.version);
    obj.put("title", this.title);
    return obj;
  }

  public HashMap<String, Object> toHasMap() {
    HashMap<String, Object> obj = new HashMap<String, Object>();
    obj.put("description", this.description);
    obj.put("version", this.version);
    obj.put("title", this.title);
    return obj;
  }
}
