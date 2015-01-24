package kz.sdauka.orgamemanager.dao;

import kz.sdauka.orgamemanager.entity.Session;
import kz.sdauka.orgamemanager.entity.SessionDetails;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dauletkhan on 22.01.2015.
 */
public interface SessionDAO {

    public void setSession(Session session) throws SQLException;

    public void updateSession(Session session) throws SQLException;

    public Session getSession() throws SQLException;

    public List getAllSessionDetails(int sessionID) throws SQLException;

    public void setSessionDetails(SessionDetails sessionDetails) throws SQLException;
}
