package ca.csl.gifthub.web.app.modules;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IdListForm extends ControllerForm {

    List<Integer> ids;

}
