package Fourteam.db;

import Fourteam.db.Exception.DataBaseException;
import Fourteam.mediator.Notification;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// import core.DomainEvent;

public abstract class DbContext implements IDbContext {

  private List<DbSet> _dbSets;
  private Class _dbContextClass;

  public DbContext(Class dbContextClass) throws DataBaseException {
    _dbContextClass = dbContextClass;
    _dbSets = new ArrayList<>();
    this.instanceDbSet();
    onModelCreating(_dbSets);
  }

  public List<Object> getDomainEvents() {
    List<Object> events = new ArrayList<>();
    for (DbSet dbSet : _dbSets) {
      events.addAll(dbSet.get_events());
    }
    return events;
  }

  public void instanceDbSet() {
    Field[] fields = _dbContextClass.getFields();
    for (Field field : fields) {
      if (field.getType().getName().contains(Fourteam.db.DbSet.class.getName())) {
        try {
          DbSet db = new DbSet<>(this, field);
          field.set(this, db);
          _dbSets.add(db);
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
