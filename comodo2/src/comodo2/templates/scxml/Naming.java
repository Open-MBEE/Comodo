package comodo2.templates.scxml;

import java.util.Arrays;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class Naming {
	/**
	 * Strip off Model name
	 */
	public String packageName(final PackageableElement it) {
		String qnNearestPck = it.getNearestPackage().getQualifiedName();
		int i = qnNearestPck.indexOf("::");
		return qnNearestPck.substring(i + 2).replace("::", ".");
	}

	public String packagePath(final PackageableElement it) {
		return this.packageName(it).replace(".", "/");
	}

	protected String _typeName(final PrimitiveType it) {
		if (it.getName() != null) {
			switch (it.getName()) {
			case "String":
				return "String";
			case "Integer":
				return "Integer";
			case "Real":
				return "Double";
			case "Unlimited Natural":
				return "Long";
			default:
				return it.getName();
			}
		} else {
			return it.getName();
		}
	}

	protected String _typeName(final Type it) {
		return (packageName(it) + "." + it.getName());
	}

	protected String _typeName(final Property it) {
		if (it.isMultivalued()) {
			String collectionType = "";
			if (it.isUnique()) {
				collectionType = "java.util.Set";
			} else {
				collectionType = "java.util.List";
			}
			return collectionType;
		} else {
			return this.typeName(it.getType());
		}
	}

	public String typeName(final Element it) {
		if (it instanceof PrimitiveType) {
			return _typeName((PrimitiveType)it);
		} else if (it instanceof Property) {
			return _typeName((Property)it);
		} else if (it instanceof Type) {
			return _typeName((Type)it);
		} else {
			throw new IllegalArgumentException("Unhandled parameter types: " +
					Arrays.<Object>asList(it).toString());
		}
	}
}
