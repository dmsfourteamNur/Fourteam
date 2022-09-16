package fourteam.db;

import java.util.List;

import fourteam.db.Exception.DataBaseException;

public interface IDbContext extends IDbSet {
  public void Transaction() throws DataBaseException;

  public void Commit() throws DataBaseException;

  public void Rollback() throws DataBaseException;

  public boolean isConnected() throws DataBaseException;

  public void onModelCreating(List<DbSet> sets) throws DataBaseException;
}
