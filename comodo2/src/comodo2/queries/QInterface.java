package comodo2.queries;

import comodo2.engine.Config;
import javax.inject.Inject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Reception;

public class QInterface {
	@Inject
	private QStereotype mQStereotype;

	@Inject
	private QPackage mQPackage;

	public org.eclipse.uml2.uml.Package getContainerPackage(final Interface i) {
		return i.getNearestPackage();
	}

	public org.eclipse.uml2.uml.Package getContainerPackage(final Class c) {
		return c.getNearestPackage();
	}

	/**
	 * @param i UML Interface .
	 * @return true if i has <<cmdoInterface>> stereotype, false otherwise.
	 */
	public boolean isToBeGenerated(final Interface i) {
		if (mQStereotype.isComodoInterface(((Element) i)) == false) {
			return false;
		}
		return mQPackage.isParentComodoModule(i.getNearestPackage(), Config.getInstance().getCurrentModule());
	}

	/**
	 * @param c UML Class (e.g. SysML BlockInterface)
	 * @return true if c has <<cmdoInterface>> stereotype, false otherwise
	 */
	public boolean isToBeGenerated(final Class c) {
		if (mQStereotype.isComodoInterface(((Element) c)) == false) {
			return false;
		}
		return mQPackage.isParentComodoModule(c.getNearestPackage(), Config.getInstance().getCurrentModule());
	}

	public boolean isToBeGenerated(final Element e) {
		if (mQStereotype.isComodoInterface(e) == false) {
			return false;
		} 
		return mQPackage.isParentComodoModule(e.getNearestPackage(), Config.getInstance().getCurrentModule());
	}

	public boolean hasRequests(final Interface i) {
		for (final Reception r : i.getOwnedReceptions()) {
			if (mQStereotype.isComodoCommand(((Element) r.getSignal()))) {
				return true;
			}
		}
		return false;
	}

	public boolean hasRequests(final Class c) {
		for (final Reception r : c.getOwnedReceptions()) {
			if (mQStereotype.isComodoCommand(((Element) r.getSignal()))) {
				return true;
			}
		}
		return false;
	}

}
