package challenge.slave;

import challenge.data.WordCount;
import challenge.data.WordCountRepository;

import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SlaveMessageListener implements MessageListener {

    WordCountRepository repo;
    Map<String,Integer> wordCount = new HashMap<>();
    int x = 1;

    public SlaveMessageListener(WordCountRepository repo) {
        this.repo = repo;
    }

    @Override
    public void onMessage(Message message) {

        ObjectMessage objectMessage = (ObjectMessage) message;
        List<String> chunk = null;
        try {
            chunk = (ArrayList) objectMessage.getObject();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        map(chunk);
        System.out.println("Received : " + x);
        x++;
    }

    private void map(List<String> chunk) {
        try {
            for(String line : chunk) {
                List<String> tmp = extractWords(line);
                for(String word : tmp) {
                    int i = 1;
                    if(wordCount.containsKey(word))  i = wordCount.get(word) + 1;
                    wordCount.put(word,i);
                }
            }
            wordCount.entrySet().forEach(entry -> repo.save(new WordCount(entry.getKey(),entry.getValue())));
            wordCount.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> extractWords(String text){
        String pattern = "([a-zA-Z]+)";
        Pattern p = Pattern.compile(pattern, Pattern.UNICODE_CASE);
        Matcher m = p.matcher(text);
        List<String> matchedWords = new ArrayList<>();
        while (m.find()){
            matchedWords.add(m.group());
        }
        return matchedWords;
    }
}
