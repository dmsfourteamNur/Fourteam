package Fourteam.mediator;

import Fourteam.http.Exception.HttpException;
import Fourteam.http.HttpStatus;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IMediator implements Mediator {

  private static ArrayList<Class> handlerClass;

  public static ArrayList<Class> getHandlers() {
    if (handlerClass == null) {
      handlerClass = new ArrayList<Class>();
    }
    return handlerClass;
  }

  public static void registerHandler(Class handler) {
    if (getHandlers().contains(handler)) {
      return;
    }
    getHandlers().add(handler);
  }

  private HashMap<Class, Object> ScopedInstances;

  public IMediator() {
    ScopedInstances = new HashMap<Class, Object>();
    ScopedInstances.put(Mediator.class, this);
  }

  public Object getScopedInstance(Class c) {
    return ScopedInstances.get(c);
  }

  public void setScopedInstance(Class c, Object instance) {
    ScopedInstances.put(c, instance);
  }

  @Override
  public <T, E> Response<E> send(Request<T> request) throws Exception {
    Response<E> response = new Response<>();
    MediatorPlanRequest<T, E> plan;
    plan = new MediatorPlanRequest<>(RequestHandler.class, "handle", request.getClass(), this);
    if(plan == null) return null;
    response.data = plan.invoke(request);
    return response;
  }

  // @Override
  // public <T> Response<Void> notify(Notification<T> notification) throws Exception {
  //   Response<Void> response = new Response<>();
  //   MediatorPlanNotify plan;
  //   plan = new MediatorPlanNotify(NotificationHandler.class, "handle", notification, this);
  //   if(plan == null) return null;
  //   plan.invoke(notification);
  //   return response;
  // }

  @Override
  public Response notify(Notification notification) throws Exception {
    Response<Void> response = new Response<>();
    MediatorPlanNotify plan;
    plan = new MediatorPlanNotify(NotificationHandler.class, "handle", notification, this);
    if(plan == null) return null;
    plan.invoke(notification);
    return response;
  }
}
