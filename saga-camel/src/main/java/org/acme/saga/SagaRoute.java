package org.acme.saga;

import org.acme.service.CreditService;
import org.acme.service.OrderService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.CamelSagaService;
import org.apache.camel.saga.InMemorySagaService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SagaRoute extends RouteBuilder {
    @Inject
    OrderService orderService;

    @Inject
    CreditService creditService;

    @Override
    public void configure() throws Exception {

        CamelSagaService sagaService = new InMemorySagaService();
        getContext().addService(sagaService);

        from("direct:saga")
                .saga().propagation(SagaPropagation.REQUIRES_NEW)
                .log("Creating a new order")
                    .to("direct:createOrder").log("Taking the credit")
                    .to("direct:reserveCredit").log("Finalizing")
                    .to("direct:finalize").log("Done!");

        // Order service

        from("direct:createOrder").saga().propagation(SagaPropagation.MANDATORY)
                .compensation("direct:cancelOrder")
                .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
                .transform().body()
                .bean(orderService, "createOrder").log("Order ${body} created");

        from("direct:cancelOrder").transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
                .bean(orderService, "cancelOrder").log("Order ${body} cancelled");

        // Credit service

        from("direct:reserveCredit").saga().propagation(SagaPropagation.MANDATORY)
                .compensation("direct:refundCredit")
                .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
                .bean(creditService, "reserveCredit").log("Credit ${header.amount} reserved in action ${body}");
        from("direct:refundCredit").transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
                .bean(creditService, "refundCredit").log("Credit for action ${body} refunded");

        // Final actions
        from("direct:finalize").saga().propagation(SagaPropagation.MANDATORY).choice()
                .when(header("fail").isEqualTo(true)).to("saga:COMPENSATE").end();

    }
}
