package comodo2.queries;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
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
    EList<Stereotype> _appliedStereotypes = e.getAppliedStereotypes();
    boolean _tripleEquals = (_appliedStereotypes == null);
    if (_tripleEquals) {
      return false;
    }
    final Function1<Stereotype, Boolean> _function = (Stereotype s) -> {
      String _name = s.getName();
      return Boolean.valueOf(Objects.equal(_name, stereotypeName));
    };
    boolean _isEmpty = IterableExtensions.isEmpty(IterableExtensions.<Stereotype>filter(e.getAppliedStereotypes(), _function));
    return (!_isEmpty);
  }
}
