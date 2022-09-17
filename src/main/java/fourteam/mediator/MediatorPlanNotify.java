package Fourteam.mediator;

import Fourteam.extensions.DependencyInjection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MediatorPlanNotify {

  public static List<NotificationHandler<Notification>> getInstances(Class<?> messageType) {
    List<NotificationHandler<Notification>> instances = new ArrayList<>();
    return instances;
  }

  Method handleMethod;
  Class handlerInstanceBuilder;
  Object instance;
  IMediator mediator;

  public MediatorPlanNotify(
    Class<?> handlerType,
    String handlerMethodName,
    Class<?> messageType,
    IMediator mediator
  ) throws Exception {
    this.mediator = mediator;
    handlerInstanceBuilder = getBean(handlerType, messageType);
    instance = DependencyInjection.createInstance(handlerInstanceBuilder, mediator);
    handleMethod = handlerInstanceBuilder.getMethod(handlerMethodName, Notification.class);
  }

  private Class getBean(Class<?> handlerType, Class<?> messageType) {
    ArrayList<Class> mediators = IMediator.getHandlers();
    for (Class mediator : mediators) {
      String name = mediator.getGenericInterfaces()[0].getTypeName();
      name = name.replaceAll(">", "");
      String[] parts = name.split("<");
      String[] parts2 = parts[1].split(",");
      if (parts2[0].equals(messageType.getName())) {
        return mediator;
      }
    }
    return null;
  }

  public void invoke(Notification notification) throws Exception {
    handleMethod.invoke(instance, notification);
  }
}
