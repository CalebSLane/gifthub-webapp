package ca.csl.gifthub.web.app.modules.account;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import ca.csl.gifthub.core.model.account.PasswordValidator.HashStatus;
import ca.csl.gifthub.core.model.account.ValidPassword;
import ca.csl.gifthub.core.model.account.ValidUsername;
import ca.csl.gifthub.web.app.modules.ControllerForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserForm extends ControllerForm implements Serializable {

    private static final long serialVersionUID = 7971374217518911817L;

    private Integer userId;
    @ValidUsername
    private String username;
    @ValidPassword(status = HashStatus.UNHASHED)
    private String password;
    @NotBlank
    @Email
    private String email;

}
