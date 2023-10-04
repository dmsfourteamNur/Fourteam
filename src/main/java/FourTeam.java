import Fourteam.config.Config;
import Fourteam.console.console;

public class FourTeam {

  public static Class[] classes = {
    // http
    Fourteam.http.Rest.class,
    // mediator
    Fourteam.mediator.AggregateException.class,
    Fourteam.mediator.IMediator.class,
    Fourteam.mediator.NotificationHandler.class,
    Fourteam.mediator.Notification.class,
    Fourteam.mediator.Request.class,
    Fourteam.mediator.Response.class,
    // Class
    Fourteam.extensions.IServiceCollection.class,
    // DB
    Fourteam.db.DbContext.class,
    Fourteam.db.DbSet.class,
    Fourteam.db.ModelBuilder.class,
  };

  public static void AddFourTeam() {
    console.succes("-------------------------------------------------------------------------");
    console.succes("------------------------    FourTeam  Service    ------------------------");
    console.succes("-------------------------------------------------------------------------");

    Config.load();
    console.warning("[", FourTeam.class.getSimpleName(), "]", "Trying to load dependencies");
    for (Class clas : classes) {
      System.out.print(clas.getName() + "\r");
    }
    console.succes("[", FourTeam.class.getSimpleName(), "]", "Dependecies load succesfull!");
  }


}
