package Fourteam.mediator;


public interface Mediator {
  public <T, E> Response<E> send(Request<T> request) throws Exception;

  public Response notify(Notification notification) throws Exception;
}
