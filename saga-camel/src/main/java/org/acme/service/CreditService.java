package org.acme.service;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.model.Reserve;
import org.acme.model.Status;
import org.apache.camel.Header;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@RegisterForReflection
public class CreditService {

    private Map<String, Reserve> reservations = Collections.synchronizedMap(new HashMap<>());
    private int totalCredit;

    public CreditService() {
        this.totalCredit = 100;
    }

    public void reserveCredit(String id, @Header("amount") int amount) {
        int credit = getCredit();
        reservations.put(id, new Reserve(id, Status.RESERVED, amount));
        if (amount > credit) {
            throw new IllegalStateException("Insufficient credit");
        }
    }

    public void refundCredit(String id) {
        Reserve reserve = reservations.get(id);
        reserve.setStatus(Status.CANCEL);
        reservations.put(id, reserve);
    }

    public synchronized int getCredit() {
        return totalCredit - reservations.values()
                .stream()
                .filter(reserve -> Objects.nonNull(reserve.getAmount()))
                .filter(reserve -> reserve.getStatus().equals(Status.RESERVED))
                .mapToInt(Reserve::getAmount).sum();
    }

    public synchronized List<Reserve> getReservations() {
        return reservations.values().stream().collect(Collectors.toList());
    }
}
