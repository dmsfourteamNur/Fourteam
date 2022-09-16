package fourteam.db;

// import core.DomainEvent;
// import core.Entity;
import fourteam.db.IDbSet.BooleanFunction;
import fourteam.mediator.Notification;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DbSet<T> {

  private List<Object> _events;
  private DbContext _context;

  private String _name;
  private Field _field;
  private Class<T> _type;

  public DbSet(DbContext context, Field field) {
    this._name = field.getName();
    this._field = field;
    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
    _type = (Class<T>) genericType.getActualTypeArguments()[0];
    _context = context;
    _events = new ArrayList<>();
  }

  public Class<T> get_type() {
    return _type;
  }

  public List<Object> get_events() {
    return _events;
  }

  private void addEvents(T obj) {
    System.out.println(obj);
    Field[] arr = obj.getClass().getFields();
    for (Field field : arr) {
      Type t = field.getGenericType();
      if (t instanceof ParameterizedType) {
        ParameterizedType pt = (ParameterizedType) t;
        Class clazz = (Class) pt.getActualTypeArguments()[0];
        System.out.println(clazz);
        Class[] interfaces = clazz.getInterfaces();
        for (Class cl : interfaces) {
          if (cl.getName().equals(Notification.class.getName())) {
            try {
              field.setAccessible(true);
              List<Object> notification = (List<Object>) field.get(obj);
              if (notification != null) {
                for (Object o : notification) {
                  _events.add((Notification) o);
                }
              }
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }

  public String getName() {
    return _name;
  }

  public void Update(T obj, BooleanFunction<T> fun) throws Exception {
    addEvents(obj);
    _context.Update(obj, fun, this);
  }

  public void Delete(BooleanFunction<T> fun) throws Exception {
    _context.Delete(fun, this);
  }

  public void Add(T obj) throws Exception {
    addEvents(obj);
    _context.Add(obj, this);
  }

  public T Single(BooleanFunction<T> fun) throws Exception {
    return (T) _context.Single(fun, this);
  }

  public List<T> All() throws Exception {
    return (List<T>) _context.All(this);
  }

  public List<T> Filter(BooleanFunction<T> fun) throws Exception {
    return (List<T>) _context.Filter(fun, this);
  }

  @Override
  public String toString() {
    return "[DbSet: " + _name + "]";
  }
}
