package com.pluralsight.orderfulfillment.config;

import javax.inject.Inject;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.pluralsight.orderfulfillment.order.OrderItemMessageTranslator;
import com.pluralsight.orderfulfillment.order.OrderStatus;

@Configuration
public class IntegrationConfig extends CamelConfiguration {
	  @Inject
	   private javax.sql.DataSource dataSource;
	  
	  @Inject
	   private Environment environment;

	   @Bean
	   public javax.jms.ConnectionFactory jmsConnectionFactory() {
	      return new org.apache.activemq.ActiveMQConnectionFactory(
	            environment.getProperty("activemq.broker.url"));
	   }

	   @Bean(initMethod = "start", destroyMethod = "stop")
	   public org.apache.activemq.pool.PooledConnectionFactory pooledConnectionFactory() {
	      PooledConnectionFactory factory = new PooledConnectionFactory();
	      factory.setConnectionFactory(jmsConnectionFactory());
	      factory.setMaxConnections(Integer.parseInt(environment
	            .getProperty("pooledConnectionFactory.maxConnections")));
	      return factory;
	   }

	   @Bean
	   public org.apache.camel.component.jms.JmsConfiguration jmsConfiguration() {
	      JmsConfiguration jmsConfiguration = new JmsConfiguration();
	      jmsConfiguration.setConnectionFactory(pooledConnectionFactory());
	      return jmsConfiguration;
	   }

	   @Bean
	   public org.apache.activemq.camel.component.ActiveMQComponent activeMq() {
	      ActiveMQComponent activeMq = new ActiveMQComponent();
	      activeMq.setConfiguration(jmsConfiguration());
	      return activeMq;
	   }


	   /**
	    * SQL Component instance used for routing orders from the orders database
	    * and updating the orders database.
	    * 
	    * @return
	    */
	   @Bean
	   public org.apache.camel.component.sql.SqlComponent sql() {
	      org.apache.camel.component.sql.SqlComponent sqlComponent = new org.apache.camel.component.sql.SqlComponent();
	      sqlComponent.setDataSource(dataSource);
	      return sqlComponent;
	   }

	   @Bean
	   public org.apache.camel.builder.RouteBuilder newWebsiteOrderRoute() {
	      return new org.apache.camel.builder.RouteBuilder() {

	         @Override
	         public void configure() throws Exception {
	        	 System.out.println("hey your camel route strat");
	            // Send from the SQL component to the Log component.
//	            from(
//
//	            		"sql:"
//	                        + "select id from orders.\"ordertype\" where status = '"
//	                        + OrderStatus.NEW.getCode()
//	                        + "'"
//	                        + "?"
//	                        + "consumer.onConsume=update orders.\"ordertype\" set status = '"
//	                        + OrderStatus.PROCESSING.getCode()
//	                        + "' where id = :#id")
//	            .bean(OrderItemMessageTranslator.class, "transformToOrderItemMessage")
//	            .to("activemq:queue:ORDER_ITEM_PROCESSING");
	   
	         }
	      };
	   }
	   
	   @Bean
	   public org.apache.camel.builder.RouteBuilder fulfillmentCenterContentBasedRouter() {
	      return new org.apache.camel.builder.RouteBuilder() {
	         @Override
	         public void configure() throws Exception {
//	            org.apache.camel.builder.xml.Namespaces namespace = new org.apache.camel.builder.xml.Namespaces(
//	                  "o", "http://www.pluralsight.com/orderfulfillment/Order");
//	            // Send from the ORDER_ITEM_PROCESSING queue to the correct
//	            // fulfillment center queue.
//	            from("activemq:queue:ORDER_ITEM_PROCESSING")
//	                  .choice()
//	                  .when()
//	                  .xpath(
//	                        "/o:Order/o:OrderType/o:FulfillmentCenter = '"
//	                              + com.pluralsight.orderfulfillment.generated.FulfillmentCenter.ABC_FULFILLMENT_CENTER.value()
//	                              + "'", namespace)
//	                  .to("activemq:queue:ABC_FULFILLMENT_REQUEST")
//	                  .when()
//	                  .xpath(
//	                        "/o:Order/o:OrderType/o:FulfillmentCenter = '"
//	                              + com.pluralsight.orderfulfillment.generated.FulfillmentCenter.FULFILLMENT_CENTER_ONE.value()
//	                              + "'", namespace)
//	                  .to("activemq:queue:FC1_FULFILLMENT_REQUEST").otherwise()
//	                  .to("activemq:queue:ERROR_FULFILLMENT_REQUEST");
	         }
	      };
	   }
	   
	   /**
	    * Route builder to implement production to a RESTful web service. This route
	    * will first consume a message from the FC1_FULFILLMENT_REQUEST ActiveMQ
	    * queue. The message body will be an order in XML format. The message will
	    * then be passed to the fulfillment center one processor where it will be
	    * transformed from the XML to JSON format. Next, the message header content
	    * type will be set as JSON format and a message will be posted to the
	    * fulfillment center one RESTful web service. If the response is success,
	    * the route will be complete. If not, the route will error out.
	    * 
	    * @return
	    */
	   @Bean
	   public org.apache.camel.builder.RouteBuilder fulfillmentCenterOneRouter() {
	      return new org.apache.camel.builder.RouteBuilder() {
	         @Override
	         public void configure() throws Exception {
//	            from("activemq:queue:FC1_FULFILLMENT_REQUEST")
//	                  .beanRef("fulfillmentCenterOneProcessor",
//	                        "transformToOrderRequestMessage")
//	                  .setHeader(org.apache.camel.Exchange.CONTENT_TYPE,
//	                        constant("application/json"))
//	                  .to("http4://localhost:8090/services/orderFulfillment/processOrders");
	         }
	      };
	   }
	   
	   
}