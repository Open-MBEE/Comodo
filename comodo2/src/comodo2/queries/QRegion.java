package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.engine.Config;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Vertex;

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

	/*
	 * 	def String getInitialStateName(Region r) {
		for (ps : r.allOwnedElements().filter(Pseudostate)) {
			if (ps.kind ==  PseudostateKind.INITIAL_LITERAL &&
				ps.container.owner == r.getParentState &&
				ps.outgoings.size == 1 &&
				ps.outgoings.head.target !== null) {
				return (ps.outgoings.head.target as State).getStateName
			}
		}
		return ""
	}
	 */
	public String getInitialStateName(final Region r) {
		for (final Pseudostate ps : Iterables.<Pseudostate>filter(r.allOwnedElements(), Pseudostate.class)) {
			if ((ps.getKind() == PseudostateKind.INITIAL_LITERAL) &&
			    Objects.equal(ps.getContainer().getOwner(), this.getParentState(r)) &&
				(ps.getOutgoings().size() == 1)) {
				Vertex v = ps.getOutgoings().get(0).getTarget();
				if (v != null) {
					return  mQState.getStateName((State)v);
				}
			}
		}
		return "";
	}

	public State getInitialState(final Region r) {
		for (final Pseudostate ps : Iterables.<Pseudostate>filter(r.allOwnedElements(), Pseudostate.class)) {
			if ((ps.getKind() == PseudostateKind.INITIAL_LITERAL) &&
			    Objects.equal(ps.getContainer().getOwner(), this.getParentState(r)) &&
				(ps.getOutgoings().size() == 1)) {
				Vertex v = ps.getOutgoings().get(0).getTarget();
				if (v != null) {
					return  (State)v;
				}
			}
		}
		return null;
	}

	public boolean isTopState(final Region r) {
		return Objects.equal(r.getOwner(), r.containingStateMachine());
	}
}
