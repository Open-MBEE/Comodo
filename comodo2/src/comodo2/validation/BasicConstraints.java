package comodo2.validation;

import java.util.Collections;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

public class BasicConstraints extends AbstractDeclarativeValidator {
	private UMLPackage pck = UMLPackage.eINSTANCE;

	@Override
	protected List<EPackage> getEPackages() {
		return Collections.<EPackage>unmodifiableList(CollectionLiterals.<EPackage>newArrayList(UMLPackage.eINSTANCE));
	}

	@Override
	public boolean isLanguageSpecific() {
		return false;
	}

	@Check(CheckType.NORMAL)
	public void check_Model(final Model it) {
		if (it.getName() == null) {
			this.error("Model must have a name", it, this.pck.getNamedElement_Name());
		}
	}

	@Check(CheckType.NORMAL)
	public void check_Class(final org.eclipse.uml2.uml.Class it) {
		if (it.getAllAttributes().isEmpty()) {
			this.warning("Class " + it.getName() + " does not have any attributes", it, this.pck.getNamedElement_Name());
		}
	}

	@Check(CheckType.NORMAL)
	public void check_Property(final Property it) {
		if (it.getName() == null) {
			this.error("Property must have a name", it, this.pck.getNamedElement_Name());
		} else {
			if (Character.isUpperCase(it.getName().charAt(0))) {
				this.warning("Property name \'" + it.getName() + "\' should start with a lower case", it, this.pck.getNamedElement_Name());
			}
		}
	}
}
