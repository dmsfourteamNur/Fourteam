package Fourteam.mediator;

import Fourteam.extensions.DependencyInjection;
import Fourteam.http.Exception.HttpException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MediatorPlanRequest<T, E> {

  Method handleMethod;
  Class handlerInstanceBuilder;
  Object instance;
  IMediator mediator;

  public MediatorPlanRequest(
    Class<?> handlerType,
    String handlerMethodName,
    Class<?> messageType,
    IMediator mediator
  ) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
    this.mediator = mediator;
    handlerInstanceBuilder = getBean(handlerType, messageType);
    try {
      instance = DependencyInjection.createInstance(handlerInstanceBuilder, mediator);
    } catch (Exception e) {
      e.printStackTrace();
    }
    handleMethod = handlerInstanceBuilder.getMethod(handlerMethodName, messageType);
  }

  private Class getBean(Class<?> handlerType, Class<?> messageType) throws ClassNotFoundException {
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

    throw new ClassNotFoundException("Handler not found. Did you forget to register this?");
  }

  public E invoke(Request<T> request) throws Exception {
    return (E) handleMethod.invoke(instance, request);
  }
}
