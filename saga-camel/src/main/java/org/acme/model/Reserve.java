package org.acme.model;

public class Reserve {
    String id;
    Status status;
    Integer amount;

    public Reserve(String id, Status status, Integer amount) {
        this.id = id;
        this.status = status;
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
