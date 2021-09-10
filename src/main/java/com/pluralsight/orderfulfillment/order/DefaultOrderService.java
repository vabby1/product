package com.pluralsight.orderfulfillment.order;

import java.io.StringWriter;
import java.util.*;

import javax.inject.*;
import javax.transaction.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import com.pluralsight.orderfulfillment.catalog.*;
import com.pluralsight.orderfulfillment.customer.*;
import com.pluralsight.orderfulfillment.generated.FulfillmentCenter;
import com.pluralsight.orderfulfillment.generated.ObjectFactory;
import com.pluralsight.orderfulfillment.generated.OrderItemType;
import com.pluralsight.orderfulfillment.generated.OrderType;
import com.pluralsight.orderfulfillment.order.fulfillment.*;


@Service
public class DefaultOrderService implements OrderService {
   private static final Logger log = LoggerFactory
         .getLogger(DefaultOrderService.class);

   @Inject
   private OrderRepository orderRepository;

   @Inject
   private OrderItemRepository orderItemRepository;

   @Inject
   private FulfillmentProcessor fulfillmentProcessor;
   
   @Inject
   private CustomerRepository customerRepository;

   public List<Order> getOrderDetails() {
     List<Order> orders = new ArrayList<Order>();

      try {
    	 
       populateOrderDetails(orders, orderRepository.findAll());
      } catch (Exception e) {
    	  
    	  System.out.println("exception is"+e);
         log.error(
               "An error occurred while retrieving all orders: "
                     + e.getMessage(), e);
      }

      return orders;
   }

   public void processOrderFulfillment() {
      try {
         fulfillmentProcessor.run();
      } catch (Exception e) {
         log.error(
               "An error occurred during the execution of order fulfillment processing: "
                     + e.getMessage(), e);
      }
   }

   public List<Order> getOrderDetails(OrderStatus orderStatus, int fetchSize) {
      List<Order> orders = new ArrayList<Order>();

      try {
         populateOrderDetails(orders, orderRepository.findByStatus(
               orderStatus.getCode(), new PageRequest(0, fetchSize)));
      } catch (Exception e) {
         log.error("An error occurred while getting orders by order status: "
               + e.getMessage(), e);
      }

      return orders;
   }

   @Transactional(rollbackOn = Exception.class)
   public void processOrderStatusUpdate(List<Order> orders,
         OrderStatus orderStatus) throws Exception {
      List<Long> orderIds = new ArrayList<Long>();
      for (Order order : orders) {
         orderIds.add(order.getId());
      }
      orderRepository.updateStatus(orderStatus.getCode(),
            new Date(System.currentTimeMillis()), orderIds);
      orderItemRepository.updateStatus(orderStatus.getCode(),
            new Date(System.currentTimeMillis()), orderIds);
      for (Order order : orders) {
         order.setStatus(orderStatus.getCode());
      }
   }

   public List<OrderItem> getOrderItems(long id) {
      List<OrderItem> orderItems = new ArrayList<OrderItem>();

      try {
         List<OrderItemEntity> orderItemEntities = orderItemRepository
               .findByOrderId(id);
         populateOrderItems(orderItems, orderItemEntities);
      } catch (Exception e) {
         log.error(
               "An error occurred while retrieving order items for the order id |"
                     + id + "|: " + e.getMessage(), e);
      }
      return orderItems;
   }

   /**
    * Populate the list of orders based on order entity details.
    * 
    * @param orders
    * @param orderEntities
    */
   private void populateOrderDetails(List<Order> orders,
         Iterable<OrderEntity> orderEntities) {
      for (Iterator<OrderEntity> iterator = orderEntities.iterator(); iterator
            .hasNext();) {
         OrderEntity entity = iterator.next();
         CustomerEntity customerEntity = entity.getCustomer();
         Customer customer = new Customer(customerEntity.getId(),
               customerEntity.getFirstName(), customerEntity.getLastName(),
               customerEntity.getEmail());
         orders.add(new Order(entity.getId(), customer,
               entity.getOrderNumber(), entity.getTimeOrderPlaced(), entity
                     .getLastUpdate(), OrderStatus.getOrderStatusByCode(
                     entity.getStatus()).getDescription()));
      }
   }

   private void populateOrderItems(List<OrderItem> orderItems,
         Iterable<OrderItemEntity> orderItemEntities) {
      for (Iterator<OrderItemEntity> iterator = orderItemEntities.iterator(); iterator
            .hasNext();) {
         OrderItemEntity entity = iterator.next();
         CatalogItemEntity catalogItemEntity = entity.getCatalogItem();
         CatalogItem catalogItem = new CatalogItem(catalogItemEntity.getId(),
               catalogItemEntity.getItemNumber(),
               catalogItemEntity.getItemName(), catalogItemEntity.getItemType());
         orderItems.add(new OrderItem(entity.getId(), catalogItem, entity
               .getStatus(), entity.getPrice(), entity.getLastUpdate(), entity
               .getQuantity()));
      }
   }

   @Override
   public String processCreateOrderMessage(Long id) throws Exception {
      // Retrieve the order from the database using the order ID passed.
      OrderEntity orderEntity = orderRepository.findOne(id);
      // Map the order database data to a schema generated Order.
      com.pluralsight.orderfulfillment.generated.Order order = buildOrderXmlType(orderEntity);
      System.out.println("odrder object"+order);

      // Marshall the Order into an XML string.
      JAXBContext context = JAXBContext
            .newInstance(com.pluralsight.orderfulfillment.generated.Order.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      StringWriter writer = new StringWriter();
      marshaller.marshal(order, writer);
      return writer.toString();
   }

  
   private com.pluralsight.orderfulfillment.generated.Order buildOrderXmlType(
         OrderEntity order) throws Exception {
      ObjectFactory objectFactory = new ObjectFactory();
      OrderType orderType = objectFactory.createOrderType();
      orderType.setFirstName(order.getCustomer().getFirstName());
      orderType.setLastName(order.getCustomer().getLastName());
      orderType.setEmail(order.getCustomer().getEmail());
      // Default to ABC_FULFILLMENT_CENTER. All web orders will be fulfilled
      // through this endpoint.
      orderType.setFulfillmentCenter(FulfillmentCenter.ABC_FULFILLMENT_CENTER);
      orderType.setOrderNumber(order.getOrderNumber());
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(order.getTimeOrderPlaced());
      orderType.setTimeOrderPlaced(DatatypeFactory.newInstance().newXMLGregorianCalendar(
            cal));
      for (OrderItemEntity orderItem : order.getOrderItems()) {
         OrderItemType orderItemType = objectFactory.createOrderItemType();
         orderItemType.setItemNumber(orderItem.getCatalogItem().getItemNumber());
         orderItemType.setPrice(orderItem.getPrice());
         orderItemType.setQuantity(orderItem.getQuantity());
         orderType.getOrderItems().add(orderItemType);
      }
      com.pluralsight.orderfulfillment.generated.Order orderElement = objectFactory
            .createOrder();
      orderElement.setOrderType(orderType);
      return orderElement;
   }

@Override
public List<CustomerEntity> getCustDetails() {
	 List<CustomerEntity> CUST ;
	//long CUST= customerRepository.count();
	 
	// Customer c = null;
	 
//	 ArrayList<Customer> customer = new ArrayList<Customer>();
	
	 CUST=customerRepository.findAll();
	 
//	 Iterator<CustomerEntity> itr = CUST.iterator();
//	 while (itr.hasNext()) {
//		// System.out.println("1111111111111111111111111111111"+itr.next().getId());
//	 System.out.println("1111111111111111111111111111111"+itr.next().getFirstName());
//				 System.out.println("1111111111111111111111111111111"+itr.next().getLastName());
//		 System.out.println("1111111111111111111111111111111"+itr.next().getEmail());
//		 
////		  c.setId(itr.next().getId());
//		  c.setFirstName(itr.next().getFirstName());
//		  c.setLastName(itr.next().getLastName());
//		  c.setEmail(itr.next().getEmail());
//		 System.out.println("222222222222222222222222222222222"+c);
//		// System.out.println("222222222222222222222222222222222"+c.toString());
//		 
//	 }

	return CUST;
}


}
