package org.acme.service;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.model.Order;
import org.acme.model.Status;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
@RegisterForReflection
public class OrderService {
    private Set<Order> orders = Collections.synchronizedSet(new HashSet<>());

    public void createOrder(String id) {
        orders.add(new Order(id, Status.RESERVED));
    }

    public void cancelOrder(String id) {
        Optional<Order> order = orders.stream()
                .filter(o -> o.getId().equals(id))
                .findAny();
        if(order.isPresent()) {
            order.get().setStatus(Status.CANCEL);
            orders.add(order.get());
        }

    }

    public synchronized List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}
