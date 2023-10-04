package Fourteam.mediator;

import Fourteam.extensions.DependencyInjection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediatorPlanNotify {

  public static List<NotificationHandler<Object>> getInstances(Class<?> messageType) {
    List<NotificationHandler<Object>> instances = new ArrayList<>();
    return instances;
  }

  Method handleMethod;
  Class handlerInstanceBuilder;
  Object instance;
  IMediator mediator;

  public MediatorPlanNotify(
      Class<?> handlerType,
      String handlerMethodName,
      Notification message,
      IMediator mediator) throws Exception {
    this.mediator = mediator;
    // System.out.println("entro al mediatro plan notify");
    handlerInstanceBuilder = getBean(handlerType, message.getClass(), message);
    instance = DependencyInjection.createInstance(handlerInstanceBuilder, mediator);
    handleMethod = handlerInstanceBuilder.getMethod(handlerMethodName, message.getClass());
  }

  private Class getBean(Class<?> handlerType, Class<?> messageType, Notification message) {
    ArrayList<Class> mediators = IMediator.getHandlers();
    for (Class mediator : mediators) {

      String name = mediator.getGenericInterfaces()[0].getTypeName();
      Pattern p = Pattern.compile(".*?<(.*)?>");
      Matcher m = p.matcher(name);
      String name2="";

      if (m.matches()) {
        name2 = m.group(1);
      }

      // String name2 = name.replaceAll(">", "");
      // String[] parts = name2.split("<");
      // String[] parts2 = parts[1].split(",");
      // name2 = parts2[0];
      // System.out.println("");
      // System.out.println(name);
      // System.out.println(name2 + "  =  " + message.getGenericName());
      if (name2.equals(message.getGenericName())) {
		System.out.println("---");
		System.out.println(name2);
		System.out.println(message.getGenericName());
        return mediator;
      }

      // System.out.println("--start--");
      // ParameterizedType typ = (ParameterizedType)
      // mediator.getGenericInterfaces()[0];
      // Type tType = (typ).getActualTypeArguments()[0];
      // System.out.println("name");
      // System.out.println(name);
      // System.out.println(message.getGenericName());
      // try {
      // Class<?> EventClass = (Class<?>) tType;
      // if (EventClass.equals(message.getGenericName())) {
      // return mediator;
      // }
      // } catch (Exception e) {

      // // message.getClass().getFields()[0].get(message)

      // System.out.println(message.getClass());
      // // TODO: handle exception
      // }

      // System.out.println("--end--");
      // String name2 = EventClass.getName();

    }
    return null;
  }

  public void invoke(Notification notification) throws Exception {
    handleMethod.invoke(instance, notification);
  }
}
