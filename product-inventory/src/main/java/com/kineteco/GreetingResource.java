package com.kineteco;

import com.kineteco.model.ProductInventory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProductInventory hello() {
        ProductInventory productInventory = new ProductInventory();
        productInventory.setSku("123");
        return productInventory;
    }
}