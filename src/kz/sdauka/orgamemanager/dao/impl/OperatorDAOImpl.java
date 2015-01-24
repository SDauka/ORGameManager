package kz.sdauka.orgamemanager.dao.impl;

import kz.sdauka.orgamemanager.dao.OperatorDAO;
import kz.sdauka.orgamemanager.entity.Operator;
import kz.sdauka.orgamemanager.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Created by Dauletkhan on 20.01.2015.
 */
public class OperatorDAOImpl implements OperatorDAO {

    @Override
    public Operator findOperatorByLogin(String login) throws SQLException {
        Session session = null;
        Operator operator = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Operator operator where operator.login = :login").setString("login", login);
            operator = (Operator) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошбика проверки авторизации", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return operator;
    }
}
