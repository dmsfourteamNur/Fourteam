package fourteam.http;

import fourteam.JSON;
import fourteam.http.Exception.HttpCodeException;
import fourteam.http.Exception.HttpException;
import fourteam.http.annotation.DeleteMapping;
import fourteam.http.annotation.GetMapping;
import fourteam.http.annotation.PathVariable;
import fourteam.http.annotation.PostMapping;
import fourteam.http.annotation.PutMapping;
import fourteam.http.annotation.RequestBody;
import fourteam.mediator.Request;
import fourteam.swagger.parts.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jboss.com.sun.net.httpserver.HttpExchange;

public class Action {

  enum ActionType {
    GET,
    POST,
    PUT,
    DELETE,
  }

  private ActionType type;
  private Method method;
  private String route;
  private ArrayList<String> params;

  public Action(Method method) throws HttpCodeException {
    this.method = method;
    Annotation annotation = method.getAnnotation(GetMapping.class);
    if (annotation instanceof GetMapping) {
      GetMapping customAnnotation = (GetMapping) annotation;
      this.route = createRoute(customAnnotation.value());
      this.type = ActionType.GET;
      return;
    }
    annotation = method.getAnnotation(PostMapping.class);
    if (annotation instanceof PostMapping) {
      PostMapping customAnnotation = (PostMapping) annotation;
      this.route = createRoute(customAnnotation.value());
      this.type = ActionType.POST;
      return;
    }
    annotation = method.getAnnotation(PutMapping.class);
    if (annotation instanceof PutMapping) {
      PutMapping customAnnotation = (PutMapping) annotation;
      this.route = createRoute(customAnnotation.value());
      this.type = ActionType.PUT;
      return;
    }
    annotation = method.getAnnotation(DeleteMapping.class);
    if (annotation instanceof DeleteMapping) {
      DeleteMapping customAnnotation = (DeleteMapping) annotation;
      this.route = createRoute(customAnnotation.value());
      this.type = ActionType.DELETE;
      return;
    }
    throw new HttpCodeException("El metodo no tiene la anotacion GetMapping o PostMapping");
  }

  private static final Pattern p = Pattern.compile("\\{(.*?)\\}");

  public String createRoute(String route) {
    Matcher m = p.matcher(route);
    this.params = new ArrayList<>();
    while (m.find()) {
      String param = m.group(1);
      this.params.add(param);
    }
    return route;
  }

  public boolean equal(String method, String _route) {
    if (this.type != ActionType.valueOf(method)) {
      return false;
    }
    if (this.params.size() > 0) {
      String[] r_route = _route.split("/");
      String[] m_route = this.route.split("/");
      if (r_route.length != m_route.length) {
        return false;
      }
      for (int i = 0; i < m_route.length; i++) {
        if (m_route[i].startsWith("{") && m_route[i].endsWith("}")) {
          continue;
        }
        if (!m_route[i].equals(r_route[i])) {
          return false;
        }
      }
    } else {
      if (!this.route.equals(_route)) {
        return false;
      }
    }

    return true;
  }

  // instance es el Controller
  public void onMessage(
      HttpExchange t,
      Response response,
      String path,
      String data,
      Object instance)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, HttpException {
    Parameter[] parameters = this.method.getParameters();
    // Class[] paramTypes = this.method.getParameterTypes();
    ArrayList<Object> values = new ArrayList<Object>();
    int i_p_v = -1;

    String[] arrp = this.route.split("/");
    ArrayList<String> lis = new ArrayList<>();
    for (String s : arrp) {
      lis.add(s);
    }

    for (Parameter parameter : parameters) {
      Object value = null;
      Annotation annotation = parameter.getAnnotation(PathVariable.class);
      if (annotation instanceof PathVariable) {
        i_p_v++;

        int i = lis.indexOf("{" + this.params.get(i_p_v) + "}");
        if (i == -1) {
          throw new RuntimeException("No se encontro el parametro " + this.params.get(i_p_v));
        }
        value = path.split("/")[i];
        values.add(parseValue(value, parameter.getType()));
        continue;
      }
      annotation = parameter.getAnnotation(RequestBody.class);
      if (annotation instanceof RequestBody) {
        values.add(parseValue(data, parameter.getType()));
        continue;
      }

      values.add(null);
    }

    // try {
    Object resp;
    resp = invoke(instance, values.toArray());
    response.setCode(HttpStatus.OK);
    if (resp instanceof fourteam.mediator.Response) {
      fourteam.mediator.Response r = (fourteam.mediator.Response) resp;
      r.status = response.getCode();
      response.setBody(r.toString());
    } else {
      response.setBody(parseValueToString(resp, this.method.getReturnType()));
    }
  }

  public Object invoke(Object instance, Object... arg)
      throws HttpException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (this.method == null) {
      return null;
    }
    if (!this.method.trySetAccessible()) {
      return null;
    }

    return this.method.invoke(instance, arg);
  }

  public String parseValueToString(Object value, Class<?> type) {
    return JSON.getInstance().toJson(value);
  }

  public Object parseValue(Object value, Class<?> type) {
    if (type == String.class) {
      return value.toString();
    }
    if (type == int.class) {
      return Integer.parseInt(value.toString());
    }
    if (type == long.class) {
      return Long.parseLong(value.toString());
    }
    if (type == double.class) {
      return Double.parseDouble(value.toString());
    }
    if (type == boolean.class) {
      return Boolean.parseBoolean(value.toString());
    }
    if (type == Date.class) {
      return new Date(Long.parseLong(value.toString()));
    }
    if (type == BigDecimal.class) {
      return new BigDecimal(value.toString());
    }
    if (type == BigInteger.class) {
      return new BigInteger(value.toString());
    }
    if (type == byte[].class) {
      return value.toString().getBytes();
    }
    if (type == Byte.class) {
      return Byte.parseByte(value.toString());
    }
    Class[] i = type.getInterfaces();
    if (i.length > 0) {
      if (i[0].getName().equals(Request.class.getName())) {
        return createRequest(type, value);
      }
    }
    return JSON.getInstance().fromJson(value.toString(), type);
  }

  public Object createRequest(Class type, Object value) {
    Object instance;
    try {
      Constructor[] constructors = type.getConstructors();
      for (Constructor constructor : constructors) {
        ArrayList<Object> values = new ArrayList<>();
        Class[] paramTypes = constructor.getParameterTypes();
        for (Class paramt : paramTypes) {
          values.add(JSON.getInstance().fromJson(value.toString(), paramt));
        }
        instance = constructor.newInstance(values.toArray());
        return instance;
      }
      instance = type.getConstructor().newInstance();
    } catch (
        InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchMethodException
        | SecurityException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getMethodSwagger() {
    return type.name().toLowerCase();
  }

  public Method getMethod() {
    return method;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public ActionType getType() {
    return type;
  }

  public void setType(ActionType type) {
    this.type = type;
  }

  public Path getPathSwagger(Controller controller, String tag) {
    String path = controller.getRoute() + getRoute();
    Path po = new Path(path, getMethodSwagger());
    String name = getMethod().getName();

    Parameter[] parameters = this.method.getParameters();
    int cant_params = 0;
    for (Parameter parameter : parameters) {
      Annotation annotation = parameter.getAnnotation(PathVariable.class);
      if (annotation instanceof PathVariable) {
        fourteam.swagger.parts.Parameter pars = new fourteam.swagger.parts.Parameter(
            this.params.get(cant_params),
            "path",
            true);
        cant_params++;
        po.addParameter(pars);
      }
      annotation = parameter.getAnnotation(RequestBody.class);
      if (annotation instanceof RequestBody) {
        po.setRequestBody(new fourteam.swagger.parts.RequestBody());
      }
    }
    po.setOperationId(tag + "_" + name);
    po.setSummary(tag + " " + name);
    po.addTag(tag);
    return po;
  }
}
