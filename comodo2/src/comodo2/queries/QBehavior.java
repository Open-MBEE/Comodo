package comodo2.queries;

import com.google.common.collect.Iterables;

import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.xtend2.lib.StringConcatenation;



public class QBehavior {

    /**
	 * @param b : Behavior
	 * @return The string of all the opaque actions within the behavior
	 */
	public CharSequence getBehaviorCodeString (final Behavior b){
		StringConcatenation str = new StringConcatenation();
		
		// this adds the code from any Opaque Action within the Entry behavior to QM's <entry> code
		for (final OpaqueAction opaqueAction : Iterables.<OpaqueAction>filter(b.getOwnedElements(), OpaqueAction.class)) {
			str.newLineIfNotEmpty();
            str.append(opaqueAction.getBodies().get(0));
		}

        return str;
	}
}
