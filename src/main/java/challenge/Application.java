package challenge;

import challenge.data.*;
import challenge.master.Master;
import challenge.slave.Slave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.commons.cli.*;
import org.apache.commons.cli.BasicParser;

@SpringBootApplication
public class Application implements CommandLineRunner{

    @Autowired
    WordCountRepositoryCustom wordCountRepositoryCustom;

    @Autowired
    WordCountRepository wordCountRepository;

    @Autowired
    ResultsRepository resultsRepository;


    public static void main(String[] args) {

        Options options = new Options();

        Option source = new Option("s", "source",true,"file to process");
        source.setRequired(true);
        options.addOption(source);

        Option mongo = new Option("m", "mongo",true,"mongodb address [host:port]");
        mongo.setRequired(true);
        options.addOption(mongo);

        Option isSlaveOption = new Option("S", "run as a slave");
        isSlaveOption.setRequired(false);
        options.addOption(isSlaveOption);

        Option activemq = new Option("a", "activemq",true,"activemq address [host:port] - use master address when running embedded!");
        activemq.setRequired(true);
        options.addOption(activemq);

        Option embedded = new Option("e", "run embedded activemq");
        embedded.setRequired(false);
        options.addOption(embedded);

        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        Options options = new Options();

        Option source = new Option("s", "source",true,"file to process");
        source.setRequired(true);
        options.addOption(source);

        Option mongo = new Option("m", "mongo",true,"mongodb address [host:port]");
        mongo.setRequired(true);
        options.addOption(mongo);

        Option isSlaveOption = new Option("S", "run as a slave");
        isSlaveOption.setRequired(false);
        options.addOption(isSlaveOption);

        Option activemq = new Option("a", "activemq",true,"activemq address [host:port] - use master address when running embedded!");
        activemq.setRequired(true);
        options.addOption(activemq);

        Option embedded = new Option("e", "run embedded activemq");
        embedded.setRequired(false);
        options.addOption(embedded);

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, strings);

        String fileName = cmd.getOptionValue("s");
        String mongoAddress = cmd.getOptionValue("m");
        String mqAddress = cmd.getOptionValue("a");

        String[] mq = mqAddress.split(":");
        String host = mq[0];
        String port = mq[1];

        String url = "tcp://" + host + ":" + port;

        boolean isEmbedded = cmd.hasOption("e");
        boolean isSlave = cmd.hasOption("S");

        if(!isSlave) {
            wordCountRepository.deleteAll();
            resultsRepository.deleteAll();
            Master master = new Master(url, wordCountRepositoryCustom, resultsRepository, isEmbedded);
            master.setFileToProcess(fileName);
            new Thread(master).start();
            Slave slave = new Slave(url, wordCountRepository);
            new Thread(slave).start();
        }
        else{

            new Thread(new Slave(url, wordCountRepository)).start();
        }
    }
}
