package kz.sdauka.orgamemanager.entity;


import javax.persistence.*;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
@Entity
@Table(name = "ADMIN", schema = "PUBLIC", catalog = "ORMANAGER")
public class Admin {
    private int id;
    private String password;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = false, insertable = true, updatable = true, length = 15)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Admin admin = (Admin) o;

        if (id != admin.id) return false;
        if (password != null ? !password.equals(admin.password) : admin.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
