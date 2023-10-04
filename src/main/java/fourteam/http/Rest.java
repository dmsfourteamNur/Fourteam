package Fourteam.http;

import Fourteam.console.console;
import Fourteam.http.Exception.HttpException;
import Fourteam.swagger.parts.Document;
import Fourteam.swagger.parts.Path;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import org.jboss.com.sun.net.httpserver.HttpContext;
import org.jboss.com.sun.net.httpserver.HttpExchange;
import org.jboss.com.sun.net.httpserver.HttpServer;
import org.yaml.snakeyaml.Yaml;

public abstract class Rest {

  private static HashMap<String, Controller> controllers = new HashMap<String, Controller>();
  public static int PORT = 80;

  public static void addController(Class RestController) {
    try {
      Controller controller = new Controller(RestController);
      if (controllers.containsKey(controller.getRoute())) {
        throw new RuntimeException("Controller duplicated: " + controller.getRoute() + "");
      }
      controllers.put(controller.getRoute(), controller);
    } catch (Exception e) {
      // e.printStackTrace();
    }
  }

  public static void start() {
    start(PORT);
  }

  public static void start(int port) {
    PORT = port;
    HttpServer server;
    System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
    System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
    try {
      console.warning(
          "[",
          Rest.class.getSimpleName(),
          "]",
          "Trying to start the server on port " + port);
      server = HttpServer.create(new InetSocketAddress(port), 0);
      HttpContext context = server.createContext("/");
      context.setHandler(Rest::RestHandler);
      server.start();

      console.succes("[", Rest.class.getSimpleName(), "]", "Server running on port " + port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void RestHandler(HttpExchange t) throws IOException {

    t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
      t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
      //algo
      t.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
      t.sendResponseHeaders(200, 0);
      return;
    }
    // t.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
    // t.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET,
    // OPTIONS, PUT, DELETE");
    System.out.println(t.getRequestMethod());
    Response response = new Response();
    StringBuilder sb = new StringBuilder();
    InputStream ios = t.getRequestBody();
    int i;
    while ((i = ios.read()) != -1) {
      sb.append((char) i);
    }
    String data = sb.toString();
    onMessage(t, data, response);
    t.sendResponseHeaders(response.getCode(), response.toString().length());
    OutputStream os = t.getResponseBody();
    os.write(response.toString().getBytes());
    os.close();
  }

  private static void onMessage(HttpExchange t, String data, Response response) {
    String url = t.getRequestURI().toString();
    url = url.split("\\?")[0];
    Controller controller = null;
    for (String key : controllers.keySet()) {
      if (url.contains(key)) {
        controller = controllers.get(key);
        break;
      }
    }
    if (controller == null) {
      try {
        Download.handleRequest(t);
      } catch (IOException e) {
        response.setCode(HttpStatus.BAD_GATEWAY);
        response.setBody("Controller not found");
      }
      return;
    }
    try {
      controller.onMessage(t, data, response);
    } catch (Exception e) {
      e = getCauseRec(e);
      // if (e.getStackTrace().length == 0) {
      // System.out.println("No more stack trace after thrown.");
      // return;
      // }
      // e.getCause().printStackTrace();
      // // e.printStackTrace();
      if (e instanceof HttpException) {
        response.setCode(((HttpException) e).getCode());
        response.setBody(((HttpException) e).getMessage());
      } else {
        response.setBody(e.getMessage());
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
        e.printStackTrace();
      }
    }
  }

  public static Exception getCauseRec(Exception e) {
    if (e instanceof HttpException) {
      return e;
    }

    Exception a = (Exception) e.getCause();
    if (a == null) {
      return e;
    }
    return getCauseRec(a);
  }

  public static void createSwagger() {
    Document doc = new Document();
    for (String key : controllers.keySet()) {
      Controller controller = controllers.get(key);
      String tag = controller.getRoute();
      doc.addTag(tag, key + "_descripcion");
      controller
          .getActions()
          .iterator()
          .forEachRemaining(action -> {
            Path po = action.getPathSwagger(controller, tag);
            doc.addPath(po);
          });
    }
    PrintWriter writer;
    try {
      Yaml yaml = new Yaml();
      writer = new PrintWriter("swagger.yaml", "UTF-8");
      yaml.dump(doc.toHasMap(), writer);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // return doc.toJSON().toString();
  }
}
