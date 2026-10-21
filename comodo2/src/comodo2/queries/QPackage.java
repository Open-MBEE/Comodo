package comodo2.queries;

import com.google.common.base.Objects;
import jakarta.inject.Inject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.TreeIterator;
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

	/**
	 * @param input UML Model.
	 * @return The list of COMODO modules included in the model.
	 */
	public Iterable<org.eclipse.uml2.uml.Package> getAllComodoModules(final Resource input) {
		BasicEList<org.eclipse.uml2.uml.Package> res = new BasicEList<org.eclipse.uml2.uml.Package>();
		if (input != null && input.getContents() != null) {
			final TreeIterator<EObject> allContents = input.getAllContents();
			while (allContents.hasNext()) {
				EObject e = allContents.next();
				if (e instanceof org.eclipse.uml2.uml.Package) {
					org.eclipse.uml2.uml.Package p = (org.eclipse.uml2.uml.Package)e;
					if (mQStereotype.isComodoModule(p)) {
						res.add(p);
					}
				}
			}
		}
		return res;
	}

	public boolean containsComodoModule(Iterable<org.eclipse.uml2.uml.Package> cmdoModules, String moduleName) {
		if (moduleName == null) return false;
		for (org.eclipse.uml2.uml.Package p : cmdoModules) {
			if (p.getName() != null && p.getName().equals(moduleName)) {
				return true;
			}
		}
		return false;
	}
}
