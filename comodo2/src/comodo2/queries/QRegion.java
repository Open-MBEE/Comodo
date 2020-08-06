package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.engine.Config;
import comodo2.queries.QState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class QRegion {
	@Inject
	@Extension
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
			if ((((Objects.equal(ps.getKind(), PseudostateKind.INITIAL_LITERAL) && 
					Objects.equal(ps.getContainer().getOwner(), this.getParentState(r))) && 
					(ps.getOutgoings().size() == 1)) && 
					(IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget() != null))) {
				return mQState.getStateName(((State) IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget()));
			}
		}
		return "";
	}

	public boolean isTopState(final Region r) {
		return Objects.equal(r.getOwner(), r.containingStateMachine());
	}
}
