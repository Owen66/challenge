package challenge.master;

import challenge.data.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jms.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Master implements Runnable {

    Connection connection;
    Session session;
    Queue queue;
    MessageProducer producer;
    String fileName;
    RegisteredNodeRepository registeredNodeRepository;
    WordCountRepositoryCustom wordCountRepositoryCustom;
    ResultsRepository resultsRepository;

    public Master(String url, RegisteredNodeRepository registeredNodeRepository, WordCountRepositoryCustom wordCountRepositoryCustom, ResultsRepository reesultsRepository) {

        this.registeredNodeRepository = registeredNodeRepository;
        this.resultsRepository = reesultsRepository;
        this.wordCountRepositoryCustom = wordCountRepositoryCustom;

        try {

            Thread broker = new Thread(new BrokerRunner(url));
            broker.setDaemon(true);
            broker.start();

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = session.createQueue("slaveQueue2");
            connection.start();

            producer = session.createProducer(queue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFileToProcess(String fileName) {
        this.fileName = fileName;
    }

    private void processFile() {
        try {
            int x = 1;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            boolean done = false;
            while (!done) {
                List<String> chunk = new ArrayList<>();
                int chunckSize = 1000000;
                for(int i=0; i < chunckSize; i++) {
                    String line = reader.readLine();
                    if(line != null) {
                        chunk.add(line);
                    }else {
                        done = true;
                    }
                }
                System.out.println("Send: " + x);
                sendChunck(chunk);
                x++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finsihed reading file");
    }

    private void sendChunck(List<String> payload) {
        try {
            ObjectMessage msg = session.createObjectMessage();
            msg.setObject((Serializable)payload);
            producer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void reduceWordCountToResults() {
        List<Results> results = wordCountRepositoryCustom.getResults();
        resultsRepository.save(results);
    }

    @Override
    public void run() {
        processFile();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reduceWordCountToResults();
        registeredNodeRepository.deleteAll();
    }
}