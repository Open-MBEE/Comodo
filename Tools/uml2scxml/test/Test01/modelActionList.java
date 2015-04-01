
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.scxml.model.CustomAction;

public class modelActionList {

	List<CustomAction> getActions() {

		List<CustomAction> customActions = new ArrayList<CustomAction>();
		// Build the list of custom actions and map every action to a class
		//("http://my.custom-actions.domain/CUSTOM", myCustomAction, MyCustomAction.class)

		CustomAction ca1 = new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "DoThisAgain",
				CADoThisAgain.class);
		customActions.add(ca1);

		CustomAction ca2 = new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "DoThis",
				CADoThis.class);
		customActions.add(ca2);

		CustomAction cae1 = new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "DoThatAgain",
				CADoThatAgain.class);
		customActions.add(cae1);

		CustomAction cae2 = new CustomAction(
				"http://my.custom-actions.domain/CUSTOM", "DoThat",
				CADoThat.class);
		customActions.add(cae2);

		return customActions;
	}
}
