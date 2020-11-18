package comodo2.queries;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import javax.inject.Inject;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.StateMachine;

public class QClass {
	@Inject
	private QStereotype mQStereotype;

	@Inject
	private QPackage mQPackage;

	/**
	 * This function check whether a class has associated
	 * state machine as classifier behavior.
	 * 
	 * @param c Class with/without classifier behavior.
	 * @return true If the given Class has a State Machine, false otherwise.
	 */
	public boolean hasStateMachines(final org.eclipse.uml2.uml.Class c) {
		for (Element e : c.allOwnedElements()) {
			if (e instanceof StateMachine) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This function returns the list of state machines defining
	 * a class' classifier behavior.
	 * 
	 * @param c Class with classifier behavior.
	 * @return The list of State Machines associated to the given Class.
	 */
	public Iterable<StateMachine> getStateMachines(final org.eclipse.uml2.uml.Class c) {
		BasicEList<StateMachine> res = new BasicEList<StateMachine>();
		for (Element e : c.allOwnedElements()) {
			if (e instanceof StateMachine) {
				res.add((StateMachine)e);
			}
		}
		return res;
	}

	/**
	 * This function returns the first state machine defining a
	 * class' classifier behavior.
	 * 
	 * @param c Class with classifier behavior.
	 * @return The first State Machine associated to the given Class.
	 */
	public StateMachine getFirstStateMachine(final org.eclipse.uml2.uml.Class c) {
		return Iterables.<StateMachine>getFirst(getStateMachines(c), null);
	}

	public org.eclipse.uml2.uml.Package getContainerPackage(final org.eclipse.uml2.uml.Class c) {
		return c.getNearestPackage();
	}

	public String getContainerPackageName(final org.eclipse.uml2.uml.Class c) {
		if (c.getNearestPackage() == null) {
			return "";
		}
		return c.getNearestPackage().getName();
	}

	public boolean isToBeGenerated(final org.eclipse.uml2.uml.Class c) {
		if (mQStereotype.isComodoComponent(((Element) c)) == false) {
			return false;
		}
		return mQPackage.isParentComodoModule(c.getNearestPackage(), Config.getInstance().getCurrentModule());
	}
}
