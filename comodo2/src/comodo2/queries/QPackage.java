package comodo2.queries;

import com.google.common.base.Objects;
import comodo2.queries.QStereotype;
import javax.inject.Inject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class QPackage {
	@Inject
	@Extension
	private QStereotype mQStereotype;

	/**
	 * @param p Package where to look for the cmdoComponent classes.
	 * @return The cmdoComponent classes inside the given package.
	 */
	public Iterable<Element> getComponents(final org.eclipse.uml2.uml.Package p) {
		final Function1<Element, Boolean> _function = (Element c) -> {
			return Boolean.valueOf(mQStereotype.isComodoComponent(c));
		};
		return IterableExtensions.<Element>filter(p.allOwnedElements(), _function);
	}

	/**
	 * @param p Package where to look for the cmdoComponent classes.
	 * @return The first cmdoComponent class inside the given package.
	 */
	public Element getFirstComponent(final org.eclipse.uml2.uml.Package p) {
		final Function1<Element, Boolean> _function = (Element c) -> {
			return Boolean.valueOf(mQStereotype.isComodoComponent(c));
		};
		return IterableExtensions.<Element>head(IterableExtensions.<Element>filter(p.allOwnedElements(), _function));
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
