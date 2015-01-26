package kz.sdauka.orgamemanager.dao.impl;

import kz.sdauka.orgamemanager.dao.OperatorDAO;
import kz.sdauka.orgamemanager.entity.Operator;
import kz.sdauka.orgamemanager.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dauletkhan on 20.01.2015.
 */
public class OperatorDAOImpl implements OperatorDAO {
    private static final Logger LOG = Logger.getLogger(SessionDAOImpl.class);

    @Override
    public List<Operator> getOperators() throws SQLException {
        Session session = null;
        List<Operator> operators = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            operators = session.createCriteria(Operator.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Не удалось загрузить данные", "Ошибка загрузки данных", JOptionPane.OK_OPTION);
            LOG.error("Не удалось загрузить данные " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return operators;
    }
}
