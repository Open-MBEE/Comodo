package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.engine.Config;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

public class QRegion {
	@Inject
	private QState mQState;

	public State getParentState(final Region r) {
		return r.getState();
	}

	public String getFullyQualifiedName(final Region r) {
		if (isTopState(r)) {
			return r.getName();
		}
		if (r.getOwner() == null) {
			return r.getName();
		}
		return (mQState.getFullyQualifiedName(this.getParentState(r)) + ":" + r.getName());
	}

	public String getRegionName(final Region r) {
		if (Config.getInstance().generateFullyQualifiedStateNames()) {
			return getFullyQualifiedName(r);
		}
		return r.getName();
	}

	public String getInitialStateName(final Region r) {
		for (final Pseudostate ps : Iterables.<Pseudostate>filter(r.allOwnedElements(), Pseudostate.class)) {
			if ((ps.getKind() == PseudostateKind.INITIAL_LITERAL) &&
			    Objects.equal(ps.getContainer().getOwner(), this.getParentState(r)) &&
				(ps.getOutgoings().size() == 1)) {
				Transition t = ps.getOutgoings().get(0);
				if (t != null) {
					return  mQState.getStateName((State)t);
				}
			}
		}
		return "";
	}

	public boolean isTopState(final Region r) {
		return Objects.equal(r.getOwner(), r.containingStateMachine());
	}
}
