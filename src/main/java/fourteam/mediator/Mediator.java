package fourteam.mediator;

import fourteam.http.Exception.HttpException;
import java.lang.reflect.InvocationTargetException;

public interface Mediator {
  public <T, E> Response<E> send(Request<T> request) throws Exception;

  public Response notify(Notification notification) throws Exception;
}
