package net.blog.entity;

import net.blog.model.AbstractModel;

import java.io.Serializable;
import java.util.Objects;

public class AbstractEntity<PK> extends AbstractModel implements Serializable {
    private static final long serialVersionUID = -2523366993961614475L;

    private PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
