package challenge.web;

import challenge.data.Results;
import challenge.data.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResultsController {

    @Autowired
    ResultsRepository results;

    @RequestMapping("/results")
    public ModelAndView results(){

        Results most = results.getMax();
        Results least = results.getMin();

        ModelAndView modelAndView = new ModelAndView("results");
        modelAndView.addObject("most", most );
        modelAndView.addObject("least", least );
        return modelAndView;
    }
}
