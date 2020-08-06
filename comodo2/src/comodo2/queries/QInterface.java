package comodo2.queries;

import comodo2.engine.Config;
import comodo2.queries.QPackage;
import comodo2.queries.QStereotype;
import javax.inject.Inject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.xtext.xbase.lib.Extension;

public class QInterface {
	@Inject
	@Extension
	private QStereotype mQStereotype;

	@Inject
	@Extension
	private QPackage mQPackage;

	public org.eclipse.uml2.uml.Package getContainerPackage(final Interface i) {
		return i.getNearestPackage();
	}

	public boolean isToBeGenerated(final Interface i) {
		if (mQStereotype.isComodoInterface(((Element) i)) == false) {
			return false;
		}
		return mQPackage.isParentComodoModule(i.getNearestPackage(), Config.getInstance().getCurrentModule());
	}

	public boolean hasRequests(final Interface i) {
		for (final Reception r : i.getOwnedReceptions()) {
			if (mQStereotype.isComodoCommand(((Element) r.getSignal()))) {
				return true;
			}
		}
		return false;
	}
}
