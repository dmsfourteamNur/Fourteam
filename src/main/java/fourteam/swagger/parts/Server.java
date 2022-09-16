package fourteam.swagger.parts;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server {

  String url;
  JSONArray variables;

  public Server(String _url) {
    this.url = _url;
    // this.variables = new JSONArray();
  }

  public JSONObject toJSON() {
    JSONObject obj = new JSONObject();
    obj.put("url", this.url);
    // obj.put("variables", this.variables);
    return obj;
  }

  public HashMap<String, Object> toHashMap() {
    HashMap<String, Object> obj = new HashMap<String, Object>();
    obj.put("url", this.url);
    return obj;
  }
}
