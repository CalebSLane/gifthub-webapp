package ca.csl.gifthub.web.app.modules.login;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ca.csl.gifthub.web.app.modules.AppController;

@Controller
public class LoginController extends AppController {

    @GetMapping(value = "/login")
    public String showLoginPage(Principal principal, Model model) {
        if (principal != null) {
            return "redirect:/home";
        }
        LoginForm form = new LoginForm();
        model.addAttribute("mainForm", form);
        return "login";
    }

}
