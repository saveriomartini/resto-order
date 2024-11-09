package ch.hearc.ig.orderresto.business;

public class AbstractRestoObject implements RestoObject {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
