package challenge.web;

import challenge.data.Results;
import challenge.data.ResultsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResultsController {

    @Autowired
    ResultsRepositoryCustom results;

    @RequestMapping("/results")
    public String results(Model model){

        Results max = results.getMax();
        Results min = results.getMin();

        model.addAttribute("most",max);
        model.addAttribute("least", min);
        return "results";
    }
}
