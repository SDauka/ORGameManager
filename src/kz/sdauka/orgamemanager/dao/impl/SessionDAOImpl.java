package kz.sdauka.orgamemanager.dao.impl;

import kz.sdauka.orgamemanager.controllers.GamesFormCTRL;
import kz.sdauka.orgamemanager.dao.SessionDAO;
import kz.sdauka.orgamemanager.entity.Session;
import kz.sdauka.orgamemanager.entity.SessionDetails;
import kz.sdauka.orgamemanager.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;
import org.hibernate.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dauletkhan on 22.01.2015.
 */
public class SessionDAOImpl implements SessionDAO {
    private static final Logger LOG = Logger.getLogger(SessionDAOImpl.class);

    @Override
    public List getAllSessionDetails(int sessionID) throws SQLException {
        org.hibernate.Session session = null;
        List sessionDetails = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("from SessionDetails sessionDetails where sessionDetails.sessionBySessionId = :session_id").setInteger("session_id", sessionID);
            sessionDetails = (List<SessionDetails>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Не удалось загрузить данные " + e);
            Dialogs.create().owner(GamesFormCTRL.getStage()).title("Ошибка загрузки данных").message("Не удалось загрузить данные")
                    .showError();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return sessionDetails;
    }

    @Override
    public void setSessionDetails(SessionDetails sessionDetails) throws SQLException {
        org.hibernate.Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(sessionDetails);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Не удалось добавить подробные данные сессии. " + e);
            Dialogs.create().owner(GamesFormCTRL.getStage()).title("Ошибка при вставке").message("Не удалось добавить подробные данные сессии")
                    .showError();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void setSession(Session session) throws SQLException {
        org.hibernate.Session session1 = null;
        try {
            session1 = HibernateUtil.getSessionFactory().openSession();
            session1.beginTransaction();
            session1.save(session);
            session1.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Не удалось добавить данные сессии. " + e);
            Dialogs.create().owner(GamesFormCTRL.getStage()).title("Ошибка при вставке").message("Не удалось добавить данные сессии")
                    .showError();
        } finally {
            if (session1 != null && session1.isOpen()) {

                session1.close();
            }
        }
    }

    @Override
    public void updateSession(Session session) throws SQLException {
        org.hibernate.Session session1 = null;
        try {
            session1 = HibernateUtil.getSessionFactory().openSession();
            session1.beginTransaction();
            session1.update(session);
            session1.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Не удалось обновить данные сессии. " + e);
            Dialogs.create().owner(GamesFormCTRL.getStage()).title("Ошибка при обновлении").message("Не удалось обновить данные сессии")
                    .showError();
        } finally {
            if (session1 != null && session1.isOpen()) {
                session1.close();
            }
        }
    }

    @Override
    public Session getSession() throws SQLException {
        org.hibernate.Session session1 = null;
        Session session = null;
        try {
            session1 = HibernateUtil.getSessionFactory().openSession();
            session1.beginTransaction();
            Query query = session1.createQuery("from Session session order by session.startTime DESC");
            query.setMaxResults(1);
            session = (Session) query.uniqueResult();
            session1.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Вывод последней добавленной сессии не удалась. " + e);
            Dialogs.create().owner(GamesFormCTRL.getStage()).title("Ошбика проверки авторизации").message("Вывод последней добавленной сессии не удалась")
                    .showError();
        } finally {
            if (session1 != null && session1.isOpen()) {
                session1.close();
            }
        }
        return session;
    }
}
