package org.acme.resource;


import org.acme.model.Purchase;
import org.acme.service.CreditService;
import org.acme.service.OrderService;
import org.apache.camel.CamelContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


@Path("/saga")
@ApplicationScoped
public class SagaResource {

    Logger logger = Logger.getLogger(SagaResource.class.getName());

    @Inject
    CamelContext context;
    @Inject
    OrderService orderService;
    @Inject
    CreditService creditService;


    @Path("/purchase")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchase(Purchase purchase) {
        buy(purchase);
        return Response.ok().build();
    }

    @Path("/balance")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response credit() {
        return Response.ok().entity(creditService.getCredit()).build();
    }

    @Path("/orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response orders() {
        return Response.ok().entity(orderService.getOrders()).build();
    }

    @Path("/reservations")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response reservations() {
        return Response.ok().entity(creditService.getReservations()).build();
    }

    private void buy(Purchase purchase) {

        context.createFluentProducerTemplate()
                .to("direct:saga").withHeader("amount", purchase.getAmount())
                .request();


    }
}
