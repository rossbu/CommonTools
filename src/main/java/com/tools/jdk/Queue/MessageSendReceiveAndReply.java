package com.tools.jdk.Queue;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class MessageSendReceiveAndReply {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");  
        Connection connection = factory.createConnection();
        connection.start();    

        Queue queue = new ActiveMQQueue("testQueue");
        // 用来接受回复的 queue
        Queue replyQueue = new ActiveMQQueue("replyQueue");      
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Message message = session.createTextMessage("Andy");
        // 给 消息 设定 返回的 queue
        message.setJMSReplyTo(replyQueue);
      
        MessageProducer producer = session.createProducer(queue);
        // 发送 这个 消息
        producer.send(message);
        // 在 第一个 queue 上，消费这个消息：“Andy”
        MessageConsumer comsumer = session.createConsumer(queue);
        comsumer.setMessageListener(new MessageListener(){
            public void onMessage(Message m) {
                try {
                    // 往 replyQueue上，发送返回的消息
                    MessageProducer producer = session.createProducer(m.getJMSReplyTo());
                    producer.send(session.createTextMessage("Hello " + ((TextMessage) m).getText()));
                } catch (JMSException e1) {
                    e1.printStackTrace();
                }
            }
          
        });
        // 在 replyQueue上，消费 消息
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

