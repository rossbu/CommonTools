package com.example.jdk.Queue;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class MessageSendReceiveAndReply {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");  
        Connection connection = factory.createConnection();
        connection.start();    

        Queue queue = new ActiveMQQueue("testQueue");
        Queue replyQueue = new ActiveMQQueue("replyQueue");      
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Message message = session.createTextMessage("Andy");
        message.setJMSReplyTo(replyQueue);
      
        MessageProducer producer = session.createProducer(queue);
        producer.send(message);
        MessageConsumer comsumer = session.createConsumer(queue);
        comsumer.setMessageListener(new MessageListener(){
            public void onMessage(Message m) {
                try {
                    MessageProducer producer = session.createProducer(m.getJMSReplyTo());
                    producer.send(session.createTextMessage("Hello " + ((TextMessage) m).getText()));
                } catch (JMSException e1) {
                    e1.printStackTrace();
                }
            }
          
        });

        MessageConsumer comsumer2 = session.createConsumer(replyQueue);
        comsumer2.setMessageListener(new MessageListener(){
            public void onMessage(Message m) {
                try {
                    System.out.println(((TextMessage) m).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

