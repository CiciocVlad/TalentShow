package talent.model;

import java.io.Serializable;

public class Entity<ID> implements Serializable {
    private static final long serialVersionID = 574784894372L;
    private ID id;

    public ID getId() {
        return this.id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
