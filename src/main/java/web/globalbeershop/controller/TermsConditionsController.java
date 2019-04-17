package web.globalbeershop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TermsConditionsController {

    @GetMapping("/terms-conditions")
    public String terms() {
        return "terms-conditions";
    }
}
