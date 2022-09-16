package fourteam.massTransit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class IMassTransit {

  public ArrayList<Consumers> CONSUMERS;
  public RabbitMq rabbit;

  public IMassTransit() {
    this.CONSUMERS = new ArrayList<>();
  }

  public <T> Consumers<T> AddConsumer(Class<T> consumer) {
    Consumers<T> c = new Consumers<>(consumer);
    this.CONSUMERS.add(c);
    return c;
  }

  public interface UsingRabbitMqLambda<E, F> {
    void run(E context, F cfg);
  }

  public void UsingRabbitMq(UsingRabbitMqLambda<IMassTransit, RabbitMq> lambda) {
    RabbitMq rabbit = new RabbitMq(this);
    this.rabbit = rabbit;
    lambda.run(this, rabbit);
    try {
      rabbit.newConnection();
      for (Consumers consumers2 : CONSUMERS) {
        consumers2.declareExchange(rabbit);
        consumers2.declareQueue(rabbit);
        consumers2.basicConsume(rabbit);
      }
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }
}
