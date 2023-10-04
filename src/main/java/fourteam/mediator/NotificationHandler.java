package Fourteam.mediator;

public interface NotificationHandler<T> {
  public void handle(T notification);
}
