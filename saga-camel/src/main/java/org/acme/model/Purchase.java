package org.acme.model;

import io.quarkus.runtime.annotations.RegisterForReflection;


@RegisterForReflection
public class Purchase {

    int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
