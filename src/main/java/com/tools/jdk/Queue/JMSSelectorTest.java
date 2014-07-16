package com.tools.jdk.Queue;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class JMSSelectorTest {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");

        Connection connection = factory.createConnection();
        connection.start();

        Queue queue = new ActiveMQQueue("testQueue");

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageConsumer comsumerA = session.createConsumer(queue, "receiver = 'A'");
        comsumerA.setMessageListener(new MessageListener() {
            public void onMessage(Message m) {
                try {
                    System.out.println("ConsumerA get " + ((TextMessage) m).getText());
                } catch (JMSException e1) {
                }
            }
        });

        MessageConsumer comsumerB = session.createConsumer(queue,  "receiver = 'B'");
        comsumerB.setMessageListener(new MessageListener() {
            public void onMessage(Message m) {
                try {
                    System.out.println("ConsumerB get " + ((TextMessage) m).getText());
                } catch (JMSException e) {
                }
            }
        });

        MessageProducer producer = session.createProducer(queue);
        for (int i = 0; i < 10; i++) {
            String receiver = (i % 3 == 0 ? "A" : "B");
            TextMessage message = session.createTextMessage("Message" + i + ", receiver:" + receiver);
            message.setStringProperty("receiver", receiver);
            producer.send(message);
        }
    }
}