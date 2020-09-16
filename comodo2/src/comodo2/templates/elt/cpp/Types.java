package comodo2.templates.elt.cpp;

import org.eclipse.uml2.uml.Type;

public class Types {
	public String typeName(final Type t) {
		if ((t == null)) {
			return "";
		}
		
		if (t.getName() != null) {
			switch (t.getName()) {
			case "Boolean":
				return "bool";
			case "String":
				return "std::string";
			case "Integer":
				return "int";
			case "Real":
				return "float";
			case "void":
				return "void";
			case "Int8":
				return "std::int8_t";
			case "Int16":
				return "std::int16_t";
			case "Int32":
				return "std::int32_t";
			case "UInt8":
				return "std::uint8_t";
			case "UInt16":
				return "std::uint16_t";
			case "UInt32":
				return "std::uint32_t";
			case "Float":
				return "float";
			case "Double":
				return "double";
			case "Timestamp":
				return "???";				
			default:
				return t.getName();
			}
		}
		
		return "";
	}
}
