package ca.csl.gifthub.web.app.modules.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.csl.gifthub.web.app.modules.AppController;
import ca.csl.gifthub.web.app.navigation.Navigation;
import ca.csl.gifthub.web.app.navigation.Navigation.Type;

@Controller
@RequestMapping(value = { "/", "/home" })
public class HomeController extends AppController {

    private static final String HOME_URL = "/home";
    public static final Navigation REDIRECT_HOME = new Navigation(HOME_URL, Type.REDIRECT);
    public static final Navigation FORWARD_HOME = new Navigation(HOME_URL, Type.FORWARD);

    @GetMapping
    public String showHomePage() {
        return "home";
    }

}
