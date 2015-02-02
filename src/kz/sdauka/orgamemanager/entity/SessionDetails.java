package kz.sdauka.orgamemanager.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
@Entity
@Table(name = "SESSION_DETAILS", schema = "PUBLIC", catalog = "ORMANAGER")
public class SessionDetails {
    private int id;
    private Timestamp startTime;
    private String workTime;
    private Session sessionBySessionId;
    private String gameName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "START_TIME", nullable = false, insertable = true, updatable = true)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "WORK_TIME", nullable = true, insertable = true, updatable = true)
    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Basic
    @Column(name = "GAME_NAME", nullable = true, insertable = true, updatable = true)
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionDetails that = (SessionDetails) o;

        if (id != that.id) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }


    @ManyToOne
    @JoinColumn(name = "SESSION_ID", referencedColumnName = "ID", nullable = false)
    public Session getSessionBySessionId() {
        return sessionBySessionId;
    }

    public void setSessionBySessionId(Session sessionBySessionId) {
        this.sessionBySessionId = sessionBySessionId;
    }
}
