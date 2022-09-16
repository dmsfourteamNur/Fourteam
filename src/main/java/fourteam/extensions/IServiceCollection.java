package fourteam.extensions;

import fourteam.massTransit.IMassTransit;
import fourteam.massTransit.IPublishEndpoint;
import fourteam.mediator.IMediator;
import fourteam.mediator.Mediator;
import java.util.HashMap;

public class IServiceCollection {

  private static HashMap<Class, Class> TrasientMap;
  private static HashMap<Class, Class> ScopedMap;
  private static HashMap<Class, Object> SingletonMap;

  // TRANSIENT
  private static HashMap<Class, Class> getInstanceTransient() {
    if (TrasientMap == null) {
      TrasientMap = new HashMap<>();
    }
    return TrasientMap;
  }

  public static void AddTransient(Class in, Class to) {
    getInstanceTransient().put(in, to);
  }

  public static Class GetTransient(Class in) {
    return getInstanceTransient().get(in) == null ? null : getInstanceTransient().get(in);
  }

  // SCOPED
  private static HashMap<Class, Class> getInstanceScoped() {
    if (ScopedMap == null) {
      ScopedMap = new HashMap<>();
    }
    return ScopedMap;
  }

  public static void AddScoped(Class in, Class to) {
    getInstanceScoped().put(in, to);
  }

  public static Class GetScoped(Class in) {
    return getInstanceScoped().get(in) == null ? null : getInstanceScoped().get(in);
  }

  // Singleton
  private static HashMap<Class, Object> getInstanceSingleton() {
    if (SingletonMap == null) {
      SingletonMap = new HashMap<>();
    }
    return SingletonMap;
  }

  public static void AddSingleton(Class in) {
    try {
      getInstanceSingleton().put(in, DependencyInjection.createInstance(in, null));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Object GetSingleton(Class in) {
    return getInstanceSingleton().get(in) == null ? null : getInstanceSingleton().get(in);
  }

  public static void AddMediator() {
    AddScoped(Mediator.class, IMediator.class);
  }

  public interface MSFunction<E> {
    void run(E str);
  }

  public static void AddMassTransit(MSFunction<IMassTransit> func) {
    IServiceCollection.AddSingleton(IMassTransit.class);
    IServiceCollection.AddSingleton(IPublishEndpoint.class);
    IMassTransit mass = (IMassTransit) IServiceCollection.GetSingleton(IMassTransit.class);
    func.run(mass);
    // AddScoped(Mediator.class, IMediator.class);
  }
}
