package comp3111.examsystem.service;

// changed the original Comparable<Member> to Comparable<Entity>
public class Entity implements java.io.Serializable, Comparable<Entity> {
    protected Long id = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entity() {
        super();
    }

    public Entity(Long id) {
        super();
        this.id = id;
    }

    // changed the original compareTo(Member o) to compareTo(Entity o)
    public int compareTo(Entity o) {
        return Long.compare(this.id, o.id);
    }
}
