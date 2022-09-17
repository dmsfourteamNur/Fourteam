package Fourteam.mediator;

import Fourteam.JSON;

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
