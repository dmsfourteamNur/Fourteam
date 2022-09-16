package fourteam.swagger.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

public class Path {

  String path;
  String method;
  String summary;
  String description;
  String operationId;
  RequestBody requestBody;
  List<Parameter> parameters;
  List<Response> responses;
  List<String> tags;

  public Path(String path, String method) {
    this.path = path;
    this.method = method;

    this.operationId = UUID.randomUUID().toString();
    this.responses = new ArrayList<Response>();
    this.parameters = new ArrayList<Parameter>();
    this.tags = new ArrayList<String>();
    this.responses.add(new Response(200));
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void addTag(String tag) {
    tags.add(tag);
  }

  public void addParameter(Parameter parameter) {
    parameters.add(parameter);
  }

  public void setRequestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
  }

  public HashMap<String, Object> toHashMap() {
    HashMap<String, Object> p = new HashMap<String, Object>();
    HashMap<String, Object> obj = new HashMap<String, Object>();
    obj.put("summary", this.summary);
    if (this.description != null) {
      obj.put("description", this.description);
    }
    if (this.parameters.size() > 0) {
      obj.put("parameters", this.getParameters());
    }
    if (this.requestBody != null) {
      obj.put("requestBody", this.requestBody.toHashMap());
    }
    obj.put("operationId", this.operationId);
    obj.put("responses", this.getResponses());
    obj.put("tags", this.tags);

    p.put(this.method, obj);
    return p;
  }

  public HashMap<String, Object> getResponses() {
    HashMap<String, Object> obj = new HashMap<String, Object>();
    responses
      .iterator()
      .forEachRemaining(a -> {
        obj.put(a.code + "", a.toHashMap());
      });
    return obj;
  }

  public List<HashMap<String, Object>> getParameters() {
    List<HashMap<String, Object>> obj = new ArrayList<HashMap<String, Object>>();
    parameters
      .iterator()
      .forEachRemaining(a -> {
        obj.add(a.toHashMap());
      });
    return obj;
  }
}
