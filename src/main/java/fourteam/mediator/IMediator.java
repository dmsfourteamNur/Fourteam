package fourteam.mediator;

import fourteam.http.Exception.HttpException;
import fourteam.http.HttpStatus;
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
    response.data = plan.invoke(request);
    return response;
  }

  @Override
  public Response<Void> notify(Notification notification) {
    Response<Void> response = new Response<>();
    MediatorPlanNotify plan;
    try {
      plan = new MediatorPlanNotify(NotificationHandler.class, "handle", notification.getClass(), this);
      plan.invoke(notification);
    } catch (Exception e) {
      response.exception = (Exception) e;
    }
    return response;
  }
}
