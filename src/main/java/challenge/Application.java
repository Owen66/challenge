package challenge;

import challenge.data.*;
import challenge.master.Master;
import challenge.slave.Slave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner{

    @Autowired
    RegisteredNodeRepository registeredNodeRepository;

    @Autowired
    WordCountRepositoryCustom wordCountRepositoryCustom;

    @Autowired
    WordCountRepository wordCountRepository;

    @Autowired
    ResultsRepository resultsRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        String fileName = "c://test/dump.txt";

        List<RegisteredNode> nodes = registeredNodeRepository.findAll();

        if(nodes.isEmpty()) {
            String host = InetAddress.getLocalHost().toString();
            String port = "61616";
            registeredNodeRepository.save(new RegisteredNode(host, port));

            String url = "tcp://localhost:" + port;
            Master master = new Master(url, registeredNodeRepository, wordCountRepositoryCustom, resultsRepository);
            master.setFileToProcess(fileName);
            new Thread(master).start();
            Slave slave = new Slave(url, wordCountRepository);
            new Thread(slave).start();
        }
        else{
            RegisteredNode masterNode = nodes.get(0);

            String host;
            if(masterNode.getHost().equals(InetAddress.getLocalHost().toString())) {
                host = "localhost";
            }else {
                host = masterNode.getHost();
            }

            String port = masterNode.getPort();
            String url = "tcp://" + host + ":" + port;
            new Thread(new Slave(url, wordCountRepository)).start();
        }
    }
}
