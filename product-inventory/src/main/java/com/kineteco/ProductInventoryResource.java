package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ProductInventory;
import com.kineteco.service.ProductInventoryService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;

@Path("/products")
public class ProductInventoryResource {
    private static final Logger LOGGER = Logger.getLogger(ProductInventoryResource.class);

    @Inject
    ProductInventoryService productInventoryService;

    @Inject
    ProductInventoryConfig productInventoryConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/health")
    public String health() {
        LOGGER.debug("health called");
        return productInventoryConfig.greetingMessage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{sku}")
    public Response inventory(@PathParam("sku") String sku) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = productInventoryService.getBySku(sku);
        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(productInventory).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ProductInventory> listInventory() {
        LOGGER.debug("Product inventory list");
        return productInventoryService.listInventory();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductInventory productInventory) {
        LOGGER.debugf("create %s", productInventory);
        productInventoryService.addProductInventory(productInventory);
        return Response.created(URI.create(productInventory.getSku())).build();
    }

    @PUT
    @Path("/{sku}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("sku") String sku, ProductInventory productInventory) {
        LOGGER.debugf("update %s", productInventory);
        productInventoryService.updateProductInventory(sku, productInventory);
        return Response.accepted(productInventory).build();
    }

    @DELETE
    @Path("/{sku}")
    public Response delete(@PathParam("sku") String sku) {
        LOGGER.debugf("delete by sku %s", sku);
        productInventoryService.delete(sku);
        return Response.accepted().build();
    }

    @PATCH
    @Path("/{sku}")
    public Response updateStock(@PathParam("sku") String sku, @QueryParam("stock") Integer stock) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = productInventoryService.stockUpdate(sku, stock);
        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.accepted(productInventory).build();
    }
}
