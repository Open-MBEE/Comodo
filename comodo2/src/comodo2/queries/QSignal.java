package comodo2.queries;

import com.google.common.base.Objects;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class QSignal {
	public boolean hasParam(final Signal s) {
		final Function1<Property, Boolean> _function = (Property e) -> {
			return Boolean.valueOf((!Objects.equal(e.getName(), "reply")));
		};
		return (IterableExtensions.isEmpty(IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function)) == false);
	}

	public boolean hasReply(final Signal s) {
		final Function1<Property, Boolean> _function = (Property e) -> {
			return Boolean.valueOf(Objects.equal(e.getName(), "reply"));
		};
		return (IterableExtensions.isEmpty(IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function)) == false);
	}

	public Type getFirstParamType(final Signal s) {
		final Function1<Property, Boolean> _function = (Property e) -> {
			return Boolean.valueOf((!Objects.equal(e.getName(), "reply")));
		};
		for (final Property a : IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function)) {
			return a.getType();
		}
		return null;
	}

	public Type getReplyType(final Signal s) {
		final Function1<Property, Boolean> _function = (Property e) -> {
			return Boolean.valueOf(Objects.equal(e.getName(), "reply"));
		};
		for (final Property a : IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function)) {
			return a.getType();
		}
		return null;
	}

	public boolean isReplyTypePrimitive(final Signal s) {
		final Function1<Property, Boolean> _function = (Property e) -> {
			return Boolean.valueOf(Objects.equal(e.getName(), "reply"));
		};
		for (final Property a : IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function)) {
			return (a.getType() instanceof PrimitiveType);
		}
		return true;
	}

	public boolean isFirstParamTypePrimitive(final Signal s) {
		final Function1<Property, Boolean> _function = (Property e) -> {
			String _name = e.getName();
			return Boolean.valueOf((!Objects.equal(_name, "reply")));
		};
		for (final Property a : IterableExtensions.<Property>filter(s.getOwnedAttributes(), _function)) {
			return (a.getType() instanceof PrimitiveType);
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
			return (s.getName().substring(0, i) + "::" + s.getName().substring((i + 1)));
		}
		return s.getName();
	}
}
