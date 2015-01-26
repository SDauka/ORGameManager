package kz.sdauka.orgamemanager.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
@Entity
@Table(name = "SESSION", schema = "PUBLIC", catalog = "ORMANAGER")
public class Session {
    private int id;
    private Date day;
    private Timestamp startTime;
    private Timestamp stopTime;
    private int countStart;
    private int sum;
    private String operator;

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
    @Column(name = "DAY", nullable = false, insertable = true, updatable = true)
    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
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
    @Column(name = "STOP_TIME", nullable = true, insertable = true, updatable = true)
    public Timestamp getStopTime() {
        return stopTime;
    }

    public void setStopTime(Timestamp stopTime) {
        this.stopTime = stopTime;
    }

    @Basic
    @Column(name = "COUNT_START", nullable = true, insertable = true, updatable = true)
    public int getCountStart() {
        return countStart;
    }

    public void setCountStart(int countStart) {
        this.countStart = countStart;
    }

    @Basic
    @Column(name = "SUM", nullable = true, insertable = true, updatable = true)
    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Basic
    @Column(name = "OPERATOR", nullable = true, insertable = true, updatable = true)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (countStart != session.countStart) return false;
        if (id != session.id) return false;
        if (sum != session.sum) return false;
        if (day != null ? !day.equals(session.day) : session.day != null) return false;
        if (startTime != null ? !startTime.equals(session.startTime) : session.startTime != null) return false;
        if (stopTime != null ? !stopTime.equals(session.stopTime) : session.stopTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (stopTime != null ? stopTime.hashCode() : 0);
        result = 31 * result + countStart;
        result = 31 * result + sum;
        return result;
    }

}
