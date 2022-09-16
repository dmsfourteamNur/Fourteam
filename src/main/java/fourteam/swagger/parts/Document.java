package fourteam.swagger.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Document {

  String openapi;
  Info info;
  List<Server> servers;
  List<Tag> tags;
  List<Path> paths;

  public Document() {
    this.openapi = "3.0.0";
    this.info = new Info();
    servers = new ArrayList<Server>();
    paths = new ArrayList<Path>();
    tags = new ArrayList<Tag>();
    servers.add(new Server("http://localhost/api/"));
    // paths.add(new Path("/", "get", "usuario"));
  }

  public void addTag(String name, String description) {
    tags.add(new Tag(name, description));
  }

  public void addPath(Path p) {
    paths.add(p);
  }

  public HashMap<String, Object> toHasMap() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("openapi", openapi);
    map.put("info", info.toHasMap());
    map.put("servers", getServers());
    map.put("tags", getTags());
    map.put("paths", getPaths());
    return map;
  }

  // public JSONObject toJSON() {
  // JSONObject obj = new JSONObject();
  // obj.put("openapi", this.openapi);
  // obj.put("info", this.info.toJSON());
  // obj.put("tags", this.getTags());
  // obj.put("servers", getServers());
  // obj.put("paths", getPaths());
  // return obj;
  // }

  public Map<String, Object> getPaths() {
    HashMap<String, Object> obj = new HashMap<String, Object>();
    paths
      .iterator()
      .forEachRemaining(a -> {
        if (obj.containsKey(a.path)) {
          Map<String, Object> map = (Map<String, Object>) obj.get(a.path);
          map.put(a.method, a.toHashMap().get(a.method));
          obj.put(a.path, map);
        } else {
          obj.put(a.path, a.toHashMap());
        }
      });
    return obj;
  }

  public List<HashMap<String, Object>> getTags() {
    List<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
    tags
      .iterator()
      .forEachRemaining(a -> {
        arr.add(a.toHasMap());
      });
    return arr;
  }

  public List<HashMap<String, Object>> getServers() {
    List<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
    servers
      .iterator()
      .forEachRemaining(a -> {
        arr.add(a.toHashMap());
      });
    return arr;
  }
}
