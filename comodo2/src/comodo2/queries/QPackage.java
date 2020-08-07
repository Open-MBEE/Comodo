package comodo2.queries;

import com.google.common.base.Objects;
import javax.inject.Inject;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.uml2.uml.Element;

public class QPackage {
	@Inject
	private QStereotype mQStereotype;

	/**
	 * @param p Package where to look for the cmdoComponent classes.
	 * @return The cmdoComponent classes inside the given package.
	 */
	public Iterable<Element> getComponents(final org.eclipse.uml2.uml.Package p) {
		BasicEList<Element> res = new BasicEList<Element>();
		for (Element e : p.allOwnedElements()) {
			if (mQStereotype.isComodoComponent(e)) {
				res.add(e);
			}
		}
		return res;
	}

	/**
	 * @param p Package where to look for the cmdoComponent classes.
	 * @return The first cmdoComponent class inside the given package.
	 */
	public Element getFirstComponent(final org.eclipse.uml2.uml.Package p) {
		for (Element e : p.allOwnedElements()) {
			if (mQStereotype.isComodoComponent(e)) {
				return e;
			}
		}
		return null;
	}

	public boolean isParentComodoModule(final org.eclipse.uml2.uml.Package p, final String moduleName) {
		if ((p == null)) {
			return false;
		}
		if ((Objects.equal(p.getName(), moduleName) && mQStereotype.isComodoModule(((Element) p)))) {
			return true;
		}
		return this.isParentComodoModule(p.getNestingPackage(), moduleName);
	}

	public org.eclipse.uml2.uml.Package getContainerPackage(final org.eclipse.uml2.uml.Package p) {
		return p.getNearestPackage();
	}
}
