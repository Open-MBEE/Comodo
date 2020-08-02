package comodo2.templates.elt.cpp;

import org.eclipse.uml2.uml.Type;

@SuppressWarnings("all")
public class Types {
  public String typeName(final Type t) {
    String _name = t.getName();
    if (_name != null) {
      switch (_name) {
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
        default:
          return t.getName();
      }
    } else {
      return t.getName();
    }
  }
}
