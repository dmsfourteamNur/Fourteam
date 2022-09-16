package fourteam.db;

import java.util.List;

import fourteam.db.Exception.DataBaseException;

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
