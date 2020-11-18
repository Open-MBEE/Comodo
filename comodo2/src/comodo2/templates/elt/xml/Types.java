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
				return "double";
			case "void":
				return "void";
			case "Int8":
				return "int8_t";
			case "Int16":
				return "int16_t";
			case "Int32":
				return "int32_t";
			case "UInt8":
				return "uint8_t";
			case "UInt16":
				return "uint16_t";
			case "UInt32":
				return "uint32_t";
			case "Float":
				return "float";
			case "Double":
				return "double";
			case "DateTime":
				return "timedate";
			case "Timestamp":
				return "timestamp";
			default:
				return t.getName();
			}
		} 
		return "";
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
