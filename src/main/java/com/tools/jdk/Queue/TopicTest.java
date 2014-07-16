package com.tools.jdk.Queue;


import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

public class TopicTest {
    public static void main(String[] args) throws Exception {       
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");  
        Connection connection = factory.createConnection();
        connection.start();      
        Topic topic= new ActiveMQTopic("topic_test");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);     
        // consumer1   
        MessageConsumer comsumer1 = session.createConsumer(topic);
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
        MessageConsumer comsumer2 = session.createConsumer(topic);
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
        MessageProducer producer = session.createProducer(topic);
        for(int i=0; i<10; i++){
            producer.send(session.createTextMessage("Message:" + i));
        }       
        Thread.sleep(3000);
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.stop();
            connection.close();
        }
    }
}


