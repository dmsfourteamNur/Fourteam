import fourteam.config.Config;
import fourteam.console.console;

public class FourTeam {

  public static Class[] classes = {
      // http
      fourteam.http.Rest.class,
      // mediator
      fourteam.mediator.AggregateException.class,
      fourteam.mediator.IMediator.class,
      fourteam.mediator.NotificationHandler.class,
      fourteam.mediator.Notification.class,
      fourteam.mediator.Request.class,
      fourteam.mediator.Response.class,
      // Class
      fourteam.extensions.IServiceCollection.class,
      // DB
      fourteam.db.DbContext.class,
      fourteam.db.DbSet.class,
      fourteam.db.ModelBuilder.class,
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
