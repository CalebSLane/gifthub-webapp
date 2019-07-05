package ca.csl.gifthub.web.app.modules.account;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.csl.gifthub.core.model.account.User;
import ca.csl.gifthub.core.persistence.service.account.UserService;
import ca.csl.gifthub.web.app.modules.AppController;
import ca.csl.gifthub.web.app.modules.IdListForm;
import ca.csl.gifthub.web.app.modules.home.HomeController;
import ca.csl.gifthub.web.app.navigation.Navigation;
import ca.csl.gifthub.web.app.navigation.Navigation.Type;
import ca.csl.gifthub.web.app.navigation.NavigationException;

@Controller
@RequestMapping("/" + UserController.USERS_PATH)
public class UserController extends AppController {

    private static final Logger LOG = LogManager.getLogger(UserController.class);

    public static final String USERS_PATH = "users";

    private static final String LIST_USERS_URL = "";
    private static final String DELETE_USERS_URL = "/delete";
    private static final String CREATE_USER_URL = "/new";
    private static final String DELETE_USER_URL = "/{id}/delete";
    private static final String SHOW_USER_URL = "/{id}";
    private static final String EDIT_USER_URL = "/{id}";

    private static final String SHOW_USER_VIEW = "account/show-user";
    private static final String ADD_USER_VIEW = "account/add-user";
    private static final String LIST_USERS_VIEW = "account/list-users";

    public static final Navigation REDIRECT_LIST_USERS = new Navigation(USERS_PATH + LIST_USERS_URL, Type.REDIRECT);
    public static final Navigation FORWARD_LIST_USERS = new Navigation(USERS_PATH + LIST_USERS_URL, Type.FORWARD);
    public static final Navigation REDIRECT_CREATE_USER = new Navigation(USERS_PATH + CREATE_USER_URL, Type.REDIRECT);
    public static final Navigation REDIRECT_SHOW_USER = new Navigation(USERS_PATH + SHOW_USER_URL, Type.REDIRECT);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = LIST_USERS_URL)
    public String userList(Model model) throws NavigationException {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("deleteUserForm", new IdListForm());
        try {
            modelMap.put("users", this.userService.getAll());
        } catch (RuntimeException e) {
            LOG.error("could not list page of users", e);
            return HomeController.FORWARD_HOME.nav();
        }
        model.addAllAttributes(modelMap);
        return LIST_USERS_VIEW;
    }

    @GetMapping(value = CREATE_USER_URL)
    public String displayCreateNewUser(Model model) {
        UserForm form = new UserForm();
        model.addAttribute("userForm", form);
        return ADD_USER_VIEW;
    }

    @PostMapping(value = CREATE_USER_URL)
    public String createNewUser(@ModelAttribute("userForm") @Valid UserForm form, BindingResult bindingResult,
            Model model) throws NavigationException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm", form);
            return ADD_USER_VIEW;
        }

        Integer id;
        try {
            User user = this.userService.addNewUser(form.getUsername(), form.getPassword(), form.getEmail());
            id = user.getId();
        } catch (ConstraintViolationException e) {
            bindingResult.reject("error.add.user", new Object[] {}, "An error occured adding new user");
            model.addAttribute("userForm", form);
            return ADD_USER_VIEW;
        }

        return REDIRECT_SHOW_USER.nav(String.valueOf(id));
    }

    @PostMapping(value = DELETE_USERS_URL)
    public String deleteUsers(@ModelAttribute("deleteUserForm") @Valid IdListForm form) throws NavigationException {
        try {
            this.userService.removeAllUsers(form.getIds());
        } catch (RuntimeException e) {
            LOG.error("Could not complete delete of users", e);
            return LIST_USERS_VIEW;
        }

        return REDIRECT_LIST_USERS.nav();
    }

    @GetMapping(value = SHOW_USER_URL)
    public String showUser(@PathVariable int id, Model model) throws NavigationException {
        UserForm form = new UserForm();
        User user;
        try {
            user = this.userService.getById(id);
        } catch (RuntimeException e) {
            LOG.error("could not fetch user with id: " + id);
            return FORWARD_LIST_USERS.nav();
        }
        user.maskPassword();

        form.setUserId(user.getId());
        form.setEmail(user.getEmail());
        form.setPassword(user.getPassword());
        form.setUsername(user.getUsername());

        model.addAttribute("userForm", form);
        model.addAttribute("user", user);
        return SHOW_USER_VIEW;
    }

    @PostMapping(value = EDIT_USER_URL)
    public String editUser(@PathVariable int id, @ModelAttribute("userForm") @Valid UserForm form,
            BindingResult bindingResult, Model model) throws NavigationException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", this.userService.getById(id));
            model.addAttribute("userForm", form);
            return SHOW_USER_VIEW;
        }

        try {
            this.userService.editExistingUser(id, form.getUsername(), form.getPassword(), form.getEmail());
        } catch (RuntimeException e) {
            LOG.error("could not update user with id: " + id);
            model.addAttribute("user", this.userService.getById(id));
            model.addAttribute("userForm", form);
            return SHOW_USER_VIEW;
        }
        return REDIRECT_SHOW_USER.nav(String.valueOf(id));
    }

    @PostMapping(value = DELETE_USER_URL)
    public String deleteUser(@PathVariable int id, Model model) {
        try {
            this.userService.removeUser(id);
        } catch (RuntimeException e) {
            LOG.error("could not delete user with id: " + id, e);
            return SHOW_USER_VIEW;
        }
        return REDIRECT_LIST_USERS.toString();
    }

}
