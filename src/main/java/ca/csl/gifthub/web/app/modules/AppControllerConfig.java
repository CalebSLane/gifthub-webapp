package ca.csl.gifthub.web.app.modules;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AppControllerConfig {

    private static final Logger LOG = LogManager.getLogger(AppControllerConfig.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(false);
        binder.registerCustomEditor(String.class, stringTrimmer);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(HttpServletRequest request, Exception exception, Model model) {
        LOG.error("Exception occured at: " + request.getRequestURL());
        LOG.debug(exception);
        model.addAttribute("exception", exception);
        return "error/generic-error";
    }

}
