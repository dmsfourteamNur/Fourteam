package Fourteam.massTransit;

import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import Fourteam.config.Config;
import Fourteam.extensions.DependencyInjection;
import Fourteam.mediator.IMediator;
import Fourteam.mediator.Notification;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Consumers<T> {

  public String Name;
  public String ExchangeName;
  public String QueueName;
  public Class<T> consumer;
  public Class<?> EventClass;

  public Consumers(Class<T> consumer) {
    this.consumer = consumer;
    Type type = consumer.getGenericSuperclass();
    Type tType = ((ParameterizedType) type).getActualTypeArguments()[0];
    this.EventClass = (Class<?>) tType;
    this.Name = EventClass.getName();
    this.QueueName = Config.ServiceName + "." + this.consumer.getName();
    this.ExchangeName = EventClass.getName();
  }

  public interface MSConsumers<E> {
    void run(E str);
  }

  public void Endpoint(MSConsumers<Consumers<T>> endpoint) {
    endpoint.run(this);
  }

  public void declareExchange(RabbitMq rabbitMq) {
    try {
      rabbitMq.getChannel().exchangeDeclare(this.ExchangeName, "fanout");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void declareQueue(RabbitMq rabbitMq) {
    try {
      rabbitMq.getChannel().queueDeclare(this.QueueName, false, false, false, null);
      rabbitMq.getChannel().queueBind(this.QueueName, this.ExchangeName, "");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void basicConsume(RabbitMq rabbit) throws IOException {
    DeliverCallback callback = (consumetTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      Gson gson = new Gson();
      Notification event = (Notification) gson.fromJson(message, this.EventClass);
      IMediator m = new IMediator();
      try {
        IConsumer obj = (IConsumer) DependencyInjection.createInstance(this.consumer, m);
        obj.Consume(event);
      } catch (Exception e) {
        e.printStackTrace();
      }
    };
    rabbit
      .getChannel()
      .basicConsume(
        this.QueueName,
        true,
        callback,
        oncancel -> {
          System.out.println("Entro on cancel");
        }
      );
  }
}
