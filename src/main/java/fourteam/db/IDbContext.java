package Fourteam.db;

import Fourteam.db.Exception.DataBaseException;
import java.util.List;

public interface IDbContext extends IDbSet {
  public void Transaction() throws DataBaseException;

  public void Commit() throws DataBaseException;

  public void Rollback() throws DataBaseException;

  public boolean isConnected() throws DataBaseException;

  public void onModelCreating(List<DbSet> sets) throws DataBaseException;
}
