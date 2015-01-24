package kz.sdauka.orgamemanager.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dauletkhan on 22.01.2015.
 */
public interface GameDAO {
    public List getAllGames() throws SQLException;
}
