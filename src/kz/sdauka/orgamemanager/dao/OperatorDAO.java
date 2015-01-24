package kz.sdauka.orgamemanager.dao;

import kz.sdauka.orgamemanager.entity.Operator;

import java.sql.SQLException;

/**
 * Created by Dauletkhan on 19.01.2015.
 */
public interface OperatorDAO {
    public Operator findOperatorByLogin(String login) throws SQLException;

}
