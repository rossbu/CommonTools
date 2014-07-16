package com.tools.jdk.Queue;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class DeliveryModeSendTest {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
  
        Connection connection = factory.createConnection();
        connection.start();
      
        Queue queue = new ActiveMQQueue("testQueue");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
              
        MessageProducer producer = session.createProducer(queue);
         // 这里 设置了 persistent， 在关掉这个connection后，consumer仍然可以收到这条消息。
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.send(session.createTextMessage("A persistent Message"));
        // 这里 设置了 non_persistent， 在关掉这个connection后，consumer就收不到这条消息了。
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.send(session.createTextMessage("A non persistent Message"));
      
        System.out.println("Send messages sucessfully!");
    }
}
