package kz.sdauka.orgamemanager.entity;

import javax.persistence.*;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
@Entity
@Table(name = "GAMES")
public class Game {
    private int id;
    private String path;
    private String name;
    private int time;
    private int cost;
    private String attribute;
    private String image;

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
    @Column(name = "PATH", nullable = false, insertable = true, updatable = true, length = 2147483647)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name = "NAME", nullable = false, insertable = true, updatable = true, length = 2147483647)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "TIME", nullable = false, insertable = true, updatable = true)
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Basic
    @Column(name = "COST", nullable = false, insertable = true, updatable = true)
    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "ATTRIBUTE", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Basic
    @Column(name = "IMAGE", nullable = false, insertable = true, updatable = true, length = 2147483647)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (cost != game.cost) return false;
        if (id != game.id) return false;
        if (time != game.time) return false;
        if (attribute != null ? !attribute.equals(game.attribute) : game.attribute != null) return false;
        if (image != null ? !image.equals(game.image) : game.image != null) return false;
        if (name != null ? !name.equals(game.name) : game.name != null) return false;
        if (path != null ? !path.equals(game.path) : game.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + time;
        result = 31 * result + cost;
        result = 31 * result + (attribute != null ? attribute.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }
}
