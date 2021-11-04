package com.kineteco;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderService {
   private static final Logger LOGGER = Logger.getLogger(OrderService.class);

   private final KinetecoProductRanking productRanking = new KinetecoProductRanking(10);

   @Incoming("orders")
   @Outgoing("order-stats")
   public Multi<Iterable<ProductOrderStats>> computeProductsStats(Multi<ManufactureOrder> orders) {
      LOGGER.info("orders incoming");
      return orders
            .group().by(order -> order.sku)
            .onItem().transformToMultiAndMerge(group ->
                  group
                        .onItem().scan(ProductOrderStats::new, this::incrementScore))
            .onItem().transform(productRanking::onNewStat)
            .invoke(() -> LOGGER.info("Manufacture order received. Stats of top orders computed %s"));
   }

   private ProductOrderStats incrementScore(ProductOrderStats stats, ManufactureOrder order) {
      stats.sku = order.sku;
      stats.units++;
      return stats;
   }
}
