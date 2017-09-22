# challenge

This uses a master/slave architecture and ActiveMQ to distribute work from the master to the slaves using remote chunking. The words are counted and stored in Mongodb for futher analysis using a kind or crude map/reduce.

To use the challenge execute the jar on any number or servers using the following options.

usage:
 -a,--activemq <arg>   activemq address [host:port] - use master address when running embedded!
 -e                    run embedded activemq
 -m,--mongo <arg>      mongodb address [host:port]
 -s,--source <arg>     file to process
 -S                    run as a slave

Example:

Master with embedded activemq: java -jar challenge -e -a localhost:61616 -s myfile.xml -m localhost:27017

Slave: java -jar challenge -S -a localhost:61616 -s myfile.xml -m localhost:27017

The results will be available on the web e.g. localhost:8080/results.

However, please be advised that this is currently not working and the port needs to be recovered from the console [Sorry!!!]

Other limitations, the embedded ActiveMQ is unstalble. It workd much better with a dedicated stand alone instance of ActiveMQ. Slave can be bounced but anything they are working on will be lost. If the master goes down you need to start over as it will not recover. Also, the mongodb is localhost:27017 regardless of what is entered as a command line arg. I wanted to correct this but ran out of time. 
