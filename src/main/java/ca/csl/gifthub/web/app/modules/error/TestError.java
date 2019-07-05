package ca.csl.gifthub.web.app.modules.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors/test")
public class TestError {

    @GetMapping(value = "/null")
    public void testNull() {
        throw new NullPointerException();
    }

}
