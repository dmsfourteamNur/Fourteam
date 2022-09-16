

import example.Controllers.TestController;
import fourteam.http.Rest;

public class example {
    public static void main(String[] args) {
        FourTeam.AddFourTeam();
        Rest.addController(TestController.class);
        Rest.start(8081);
    }
}
