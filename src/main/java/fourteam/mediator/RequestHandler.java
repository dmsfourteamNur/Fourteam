package Fourteam.mediator;

import Fourteam.http.Exception.HttpException;

public interface RequestHandler<T, R> {
  public R handle(T request) throws Exception;
}
