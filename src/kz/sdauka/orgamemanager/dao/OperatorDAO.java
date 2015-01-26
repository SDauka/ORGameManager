package kz.sdauka.orgamemanager.dao;

import kz.sdauka.orgamemanager.entity.Operator;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dauletkhan on 19.01.2015.
 */
public interface OperatorDAO {
    public List<Operator> getOperators() throws SQLException;

}
