package com.example.jdk.Queue;


import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

public class QueueTest {

    public static void main(String[] args) throws Exception {       
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");  
        Connection connection = factory.createConnection();
        connection.start();       
        Queue queue = new ActiveMQQueue("testQueue");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // consumer1
        MessageConsumer comsumer1 = session.createConsumer(queue);
        comsumer1.setMessageListener(new MessageListener(){
            public void onMessage(Message m) {
                try {
                    System.out.println("Consumer1 get " + ((TextMessage)m).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // consumer2
        MessageConsumer comsumer2 = session.createConsumer(queue);
        comsumer2.setMessageListener(new MessageListener(){
            public void onMessage(Message m) {
                try {
                    System.out.println("Consumer2 get " + ((TextMessage)m).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
          
        });
        // producer
        MessageProducer producer = session.createProducer(queue);
        for(int i=0; i<10; i++){
            producer.send(session.createTextMessage("Message:" + i));
        }
       
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.stop();
            connection.close();
        }
    }
}