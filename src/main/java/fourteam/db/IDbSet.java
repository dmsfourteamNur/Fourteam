package Fourteam.db;

import Fourteam.db.Exception.DataBaseException;
import java.util.List;

public interface IDbSet<T> {
  public interface BooleanFunction<E> {
    boolean run(E str);
  }

  public void Add(T obj, DbSet<T> dbSet) throws DataBaseException;

  public void Update(T obj, BooleanFunction<T> fun, DbSet<T> dbSet) throws DataBaseException;

  public void Delete(BooleanFunction<T> fun, DbSet<T> dbSet) throws DataBaseException;

  public List<T> All(DbSet<T> dbSet) throws DataBaseException;

  public T Single(BooleanFunction<T> fun, DbSet<T> dbSet) throws DataBaseException;

  public List<T> Filter(BooleanFunction<T> fun, DbSet<T> dbSet) throws DataBaseException;
}
