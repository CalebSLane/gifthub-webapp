package ca.csl.gifthub.web.app.modules.login;

import ca.csl.gifthub.web.app.modules.ControllerForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginForm extends ControllerForm {

    private String username;

    private String password;

}
