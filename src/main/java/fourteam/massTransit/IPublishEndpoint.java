package fourteam.massTransit;

import com.google.gson.Gson;
import fourteam.mediator.Notification;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IPublishEndpoint {

  private IMassTransit massTransit;

  public IPublishEndpoint(IMassTransit massTransit) {
    this.massTransit = massTransit;
  }

  public <T> void Publish(Notification object) {
    try {
      String exchange = object.getClass().getName();
      Gson gson = new Gson();
      String message = gson.toJson(object);
      this.massTransit.rabbit.getChannel().exchangeDeclare(exchange, "fanout");
      this.massTransit.rabbit.getChannel()
        .basicPublish(exchange, "", null, message.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
