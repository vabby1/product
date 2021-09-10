package com.pluralsight.orderfulfillment.order;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMessageTranslator {
	
	public OrderItemMessageTranslator()
	{
		System.out.println("constuctor");
	}

   private static final Logger log = LoggerFactory
         .getLogger(OrderItemMessageTranslator.class);

   @Inject
   private OrderService orderService;
   
   public String doSomething(Object obj)
   {
	   System.out.println("dosomething"+ obj);
	return null;
	   
   }

   public String transformToOrderItemMessage(Map<String, Object> orderIds) {
      String output = null;
      
      System.out.println("hey################## man your ref bean call"+orderIds);
      try {
         if (orderIds == null) {
            throw new Exception(
                  "Order id was not bound to the method via integration framework.");
         }
         if (!orderIds.containsKey("id")) {
            throw new Exception("Could not find a valid key of 'id' for the order ID.");
         }
         if (orderIds.get("id") == null || !(orderIds.get("id") instanceof Long)) {
            throw new Exception("The order ID was not correctly provided or formatted.");

         }

         output = orderService.processCreateOrderMessage((Long) orderIds.get("id"));
         System.out.println("output kai hai"+output);
      } catch (Exception e) {
         log.error("Order processing failed: " + e.getMessage(), e);
      }
      return output;
   }
}
