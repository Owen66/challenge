package challenge.slave;

import challenge.data.WordCountRepository;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Slave implements Runnable {

    Connection connection;
    Session session;
    Queue queue;
    MessageConsumer consumer;

    public Slave(String url, WordCountRepository repo) {

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = session.createQueue("slaveQueue2");
            consumer = session.createConsumer(queue);
            consumer.setMessageListener(new SlaveMessageListener(repo));


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
