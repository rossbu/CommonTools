package com.example.jdk.Queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TCPQueueSender {
    private static final int SEND_NUMBER = 5;
    public static void main(String[] args) {        
        ConnectionFactory connectionFactory;        
        Connection connection = null;        
        Session session;        
        Destination destination;        
        MessageProducer producer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        try {            
            connection = connectionFactory.createConnection();            
            connection.start();            
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);            
            destination = session.createQueue("zcy_queue");            
            producer = session.createProducer(destination);            
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);            
            sendMessage(session, producer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer)
            throws Exception {
        for (int i = 1; i <=SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("This is the ActiveMq Message " + i);        
            System.out.println("system out " + i);
            producer.send(message);
        }
    }
}