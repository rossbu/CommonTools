package com.tools.jdk.Queue;



import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class DeliveryModeReceiveTest {

    public static void main(String[] args) throws Exception {
       
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");

        Connection connection = factory.createConnection();
        connection.start();

        Queue queue = new ActiveMQQueue("testQueue");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageConsumer comsumer = session.createConsumer(queue);
        comsumer.setMessageListener(new MessageListener(){
            public void onMessage(Message m) {
                try {
                    System.out.println("Consumer get " + ((TextMessage)m).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread.sleep(2000);
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.stop();
            connection.close();
        }
    }
}


