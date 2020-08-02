package comodo2.queries;

import com.google.common.base.Objects;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class QSignal {
  public boolean hasParam(final Signal s) {
    final Function1<Property, Boolean> _function = (Property e) -> {
      String _name = e.getName();
      return Boolean.valueOf((!Objects.equal(_name, "reply")));
    };
    boolean _isEmpty = IterableExtensions.isEmpty(IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function));
    return (_isEmpty == false);
  }
  
  public boolean hasReply(final Signal s) {
    final Function1<Property, Boolean> _function = (Property e) -> {
      String _name = e.getName();
      return Boolean.valueOf(Objects.equal(_name, "reply"));
    };
    boolean _isEmpty = IterableExtensions.isEmpty(IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function));
    return (_isEmpty == false);
  }
  
  public Type getFirstParamType(final Signal s) {
    final Function1<Property, Boolean> _function = (Property e) -> {
      String _name = e.getName();
      return Boolean.valueOf((!Objects.equal(_name, "reply")));
    };
    Iterable<Property> _filter = IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function);
    for (final Property a : _filter) {
      return a.getType();
    }
    return null;
  }
  
  public Type getReplyType(final Signal s) {
    final Function1<Property, Boolean> _function = (Property e) -> {
      String _name = e.getName();
      return Boolean.valueOf(Objects.equal(_name, "reply"));
    };
    Iterable<Property> _filter = IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function);
    for (final Property a : _filter) {
      return a.getType();
    }
    return null;
  }
  
  public boolean isReplyTypePrimitive(final Signal s) {
    final Function1<Property, Boolean> _function = (Property e) -> {
      String _name = e.getName();
      return Boolean.valueOf(Objects.equal(_name, "reply"));
    };
    Iterable<Property> _filter = IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function);
    for (final Property a : _filter) {
      Type _type = a.getType();
      return (_type instanceof PrimitiveType);
    }
    return true;
  }
  
  public boolean isFirstParamTypePrimitive(final Signal s) {
    final Function1<Property, Boolean> _function = (Property e) -> {
      String _name = e.getName();
      return Boolean.valueOf((!Objects.equal(_name, "reply")));
    };
    Iterable<Property> _filter = IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function);
    for (final Property a : _filter) {
      Type _type = a.getType();
      return (_type instanceof PrimitiveType);
    }
    return true;
  }
  
  /**
   * @return from signal name "StdCmds.Init", "StdCmds"
   */
  public String namePrefix(final Signal s) {
    int i = s.getName().indexOf(".");
    if ((i >= 0)) {
      return s.getName().substring(0, (i - 1));
    }
    return s.getName();
  }
  
  /**
   * @return from signal name "StdCmds.Init", "Init"
   */
  public String nameWithoutPrefix(final Signal s) {
    int i = s.getName().indexOf(".");
    if ((i >= 0)) {
      return s.getName().substring((i + 1));
    }
    return s.getName();
  }
  
  /**
   * @return from signal name "StdCmds.Init", "StdCmds::Init"
   */
  public String nameWithNamespace(final Signal s) {
    int i = s.getName().indexOf(".");
    if ((i >= 0)) {
      String _substring = s.getName().substring(0, i);
      String _plus = (_substring + "::");
      String _substring_1 = s.getName().substring((i + 1));
      return (_plus + _substring_1);
    }
    return s.getName();
  }
}
