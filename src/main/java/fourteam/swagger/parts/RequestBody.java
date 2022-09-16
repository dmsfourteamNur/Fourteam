package fourteam.swagger.parts;

import java.util.HashMap;

public class RequestBody {

  String contentType;

  public HashMap<String, Object> toHashMap() {
    HashMap<String, Object> obj = new HashMap<String, Object>();
    HashMap<String, Object> content = new HashMap<String, Object>();
    HashMap<String, Object> appJson = new HashMap<String, Object>();
    HashMap<String, Object> schema = new HashMap<String, Object>();
    obj.put("content", content);
    content.put("application/json", appJson);
    appJson.put("schema", schema);
    schema.put("type", "object");

    return obj;
  }
}
