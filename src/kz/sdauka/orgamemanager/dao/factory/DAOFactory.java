package kz.sdauka.orgamemanager.dao.factory;

import kz.sdauka.orgamemanager.dao.GameDAO;
import kz.sdauka.orgamemanager.dao.OperatorDAO;
import kz.sdauka.orgamemanager.dao.SessionDAO;
import kz.sdauka.orgamemanager.dao.impl.GameDAOImpl;
import kz.sdauka.orgamemanager.dao.impl.OperatorDAOImpl;
import kz.sdauka.orgamemanager.dao.impl.SessionDAOImpl;

/**
 * Created by Dauletkhan on 19.01.2015.
 */
public class DAOFactory {

    private static OperatorDAO operatorsDAO = null;
    private static GameDAO gameDAO = null;
    private static DAOFactory instance = null;
    private static SessionDAO sessionDAO = null;

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public SessionDAO getSessionDAO() {
        if (sessionDAO == null) {
            sessionDAO = new SessionDAOImpl();
        }
        return sessionDAO;
    }


    public GameDAO getGamesDAO() {
        if (gameDAO == null) {
            gameDAO = new GameDAOImpl();
        }
        return gameDAO;
    }

    public OperatorDAO getOperatorsDAO() {
        if (operatorsDAO == null) {
            operatorsDAO = new OperatorDAOImpl();
        }
        return operatorsDAO;
    }


//    public AdminDAO getAdminDAO() {
//        if (adminDAO == null) {
//            adminDAO = new AdminDAOImpl();
//        }
//        return adminDAO;
//    }
}
