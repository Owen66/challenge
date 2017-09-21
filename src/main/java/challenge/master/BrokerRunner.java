package challenge.master;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

public class BrokerRunner implements Runnable{

    BrokerService broker;

    public BrokerRunner(String url) {
        try {
            broker = BrokerFactory.createBroker(new URI("broker:(" + url + ")"));
            broker.setUseJmx(true);
            broker.getManagementContext().setConnectorPort(9999);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            broker.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
