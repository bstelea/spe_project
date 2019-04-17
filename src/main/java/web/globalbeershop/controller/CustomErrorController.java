package web.globalbeershop.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "An error occured";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.FORBIDDEN.value()){
                message = "We do not have access to this content";
            }
            else if(statusCode == HttpStatus.NOT_FOUND.value()) {
                message = "We couldn't find what you are looking for";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                message = "Something went wrong on our side, please try again later";
            }
            model.addAttribute("error", statusCode);
        }
        model.addAttribute("errorMessage", message);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}