package Fourteam.http;

import Fourteam.console.console;
import Fourteam.extensions.DependencyInjection;
import Fourteam.http.Exception.HttpCodeException;
import Fourteam.http.Exception.HttpException;
import Fourteam.http.annotation.RequestMapping;
import Fourteam.http.annotation.RestController;
import Fourteam.mediator.IMediator;
import Fourteam.mediator.Mediator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.jboss.com.sun.net.httpserver.HttpExchange;

public class Controller {

  private String route;
  private Class controller;
  private ArrayList<Action> actions;

  public Controller(Class controller) throws Exception {
    Annotation annotation = controller.getAnnotation(RestController.class);
    if (!(annotation instanceof RestController)) {
      throw new RuntimeException("El controlador no tiene la anotacion RestController");
    }
    annotation = controller.getAnnotation(RequestMapping.class);
    this.route = controller.getName();

    if ((annotation instanceof RequestMapping)) {
      this.route = ((RequestMapping) annotation).value();
    }
    console.log("[", "Rest", "]", "Controller loaded on route='", this.route, "'");
    this.controller = controller;
    actions = new ArrayList<Action>();
    this.initMethods();
  }

  public ArrayList<Action> getActions() {
    return actions;
  }

  private void initMethods() {
    Method[] methods = controller.getMethods();
    // System.out.println("--------------------------------------");
    // System.out.println("[CONTROLLER] " + this.route);

    for (Method method : methods) {
      Action action = null;
      try {
        action = new Action(method);
      } catch (Exception e) {
        if (e instanceof HttpCodeException) {
          continue;
        } else {
          e.printStackTrace();
        }
      }
      if (action == null) {
        continue;
      }
      actions.add(action);
      System.out.println("" + action.getType() + ":\t" + this.route + action.getRoute());
    }
    // System.out.println("--------------------------------------");
  }

  public String getRoute() {
    return route;
  }

  public void onMessage(HttpExchange t, String data, Response response) throws Exception {
    String path = t.getRequestURI().getPath();
    path = path.split("\\?")[0];
    path = path.substring(path.indexOf(route) + route.length());
    Method method = null;
    String requestMethod = t.getRequestMethod();
    if (path.length() == 0) {
      path = "/";
    }
    boolean exito = false;
    for (Action action : actions) {
      if (action.equal(requestMethod, path)) {
        Constructor<?> cos = this.controller.getConstructor(new Class[] { Mediator.class });
        IMediator m = new IMediator();
        action.onMessage(t, response, path, data, cos.newInstance(m));
        exito = true;
        break;
      }
    }
    if (!exito) {
      response.setCode(HttpStatus.BAD_GATEWAY);
      response.setBody("Method not found");
      return;
    }
  }
}
