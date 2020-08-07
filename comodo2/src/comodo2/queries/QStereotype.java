package comodo2.queries;

import com.google.common.collect.Iterables;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

public class QStereotype {
	public boolean isComodoInterface(final Element e) {
		return this.hasStereotype(e, "cmdoInterface");
	}

	public boolean isComodoComponent(final Element e) {
		return this.hasStereotype(e, "cmdoComponent");
	}

	public boolean isComodoModule(final Element e) {
		return this.hasStereotype(e, "cmdoModule");
	}

	public boolean isComodoStructure(final Element e) {
		return this.hasStereotype(e, "cmdoStructure");
	}

	public boolean isComodoEnumeration(final Element e) {
		return this.hasStereotype(e, "cmdoEnumeration");
	}

	public boolean isComodoUnion(final Element e) {
		return this.hasStereotype(e, "cmdoUnion");
	}

	public boolean isComodoException(final Element e) {
		return this.hasStereotype(e, "cmdoException");
	}

	public boolean isComodoCommand(final Element e) {
		return this.hasStereotype(e, "cmdoCommand");
	}

	public boolean isComodoInternal(final Element e) {
		return this.hasStereotype(e, "cmdoInternal");
	}

	public boolean hasStereotype(final Element e, final String stereotypeName) {
		if ((e == null)) {
			return false;
		}
		if (e.getAppliedStereotypes() == null) {
			return false;
		}
		/*
		final Function1<Stereotype, Boolean> _function = (Stereotype s) -> {
			return Boolean.valueOf(Objects.equal(s.getName(), stereotypeName));
		};
		return !IterableExtensions.isEmpty(IterableExtensions.<Stereotype>filter(e.getAppliedStereotypes(), _function));
		*/
		for (Stereotype s : Iterables.<Stereotype>filter(e.getAppliedStereotypes(), Stereotype.class)) {
			if (s.getName().equals(stereotypeName)) {
				return true;
			}
		}
		return false;

	}
}
