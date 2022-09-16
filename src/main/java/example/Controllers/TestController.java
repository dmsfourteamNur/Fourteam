package example.Controllers;

import fourteam.http.Exception.HttpException;
import fourteam.http.annotation.*;
import fourteam.mediator.Mediator;
import fourteam.mediator.Response;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

  private Mediator _mediator;

  public TestController(Mediator mediator) {
    this._mediator = mediator;
  }

  @GetMapping("/")
  public List<TestIten> getAll() throws HttpException {
    ArrayList<TestIten> list = new ArrayList<>();
    list.add(new TestIten("ricky"));
    list.add(new TestIten("pedro"));
    return list;
  }

  public class TestIten {
    String nombre;

    public TestIten(String nombre) {
      this.nombre = nombre;
    }

  }
}
