package fourteam.mediator;

import fourteam.JSON;

public class Response<T> {

  public T data;
  public int status;
  public Exception exception;

  public boolean hasException() {
    return exception != null;
  }

  @Override
  public String toString() {
    return JSON.getInstance().toJson(this);
  }
}
