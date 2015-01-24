package kz.sdauka.orgamemanager.dao.impl;

import kz.sdauka.orgamemanager.dao.GameDAO;
import kz.sdauka.orgamemanager.entity.Game;
import kz.sdauka.orgamemanager.utils.HibernateUtil;
import org.hibernate.Session;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dauletkhan on 22.01.2015.
 */
public class GameDAOImpl implements GameDAO {
    @Override
    public List<Game> getAllGames() throws SQLException {
        Session session = null;
        List<Game> games = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            games = session.createCriteria(Game.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Не удалось загрузить данные", "Ошибка загрузки данных'", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return games;
    }
}
