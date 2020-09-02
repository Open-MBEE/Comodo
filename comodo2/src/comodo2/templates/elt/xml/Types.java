package comodo2.templates.elt.xml;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class Types {

	public boolean isPrimitiveType(final Property p) {
		return (p.getType() instanceof PrimitiveType);
	}

	public String typeName(final Type t) {
		if ((t == null)) {
			return "";
		}

		if (t.getName() != null) {
			switch (t.getName()) {
			case "Boolean":
				return "boolean";
			case "String":
				return "string";
			case "Integer":
				return "int32_t";
			case "Real":
				return "float";
			case "void":
				return "void";
			default:
				return t.getName();
			}
		} else {
			return t.getName();
		}
	}

	public String typeName(final Property p) {
		if (p == null) {
			return "";
		}
		if (p.getType() == null) {
			return "";
		}
		if (isPrimitiveType(p)) {
			return typeName(((PrimitiveType) p.getType()));
		}
		return p.getType().getName();
	}
}
