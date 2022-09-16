
import org.junit.Assert;
import org.junit.Test;

import example.Controllers.TestController;
import fourteam.http.Rest;


public class FourTeam_Test {

    @Test
    public void FourTeam_Test_accept() {
        FourTeam.AddFourTeam();
        Rest.addController(TestController.class);
        Rest.start(8080);
        Assert.assertTrue(true);
    }
}
