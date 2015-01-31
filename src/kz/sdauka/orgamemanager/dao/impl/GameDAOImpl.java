package kz.sdauka.orgamemanager.dao.impl;

import kz.sdauka.orgamemanager.controllers.GamesFormCTRL;
import kz.sdauka.orgamemanager.dao.GameDAO;
import kz.sdauka.orgamemanager.entity.Game;
import kz.sdauka.orgamemanager.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dauletkhan on 22.01.2015.
 */
public class GameDAOImpl implements GameDAO {
    private static final Logger LOG = Logger.getLogger(GameDAOImpl.class);

    @Override
    public List<Game> getAllGames() throws SQLException {
        Session session = null;
        List<Game> games = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            games = session.createCriteria(Game.class).list();
        } catch (Exception e) {
            LOG.error("Не удалось загрузить данные " + e);
            Dialogs.create().owner(GamesFormCTRL.getStage()).title("Ошибка загрузки данных").message("Не удалось загрузить данные")
                    .showError();
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return games;
    }
}
