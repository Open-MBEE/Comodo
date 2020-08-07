package comodo2.queries;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;

public class QSignal {
	public boolean hasParam(final Signal s) {
		if (s.getOwnedAttributes().isEmpty()) {
			return false;
		}
		int numParams =  s.getOwnedAttributes().size();
		if (hasReply(s)) {
			--numParams;
		}
		return numParams > 0;
	}

	public boolean hasReply(final Signal s) {
		for (Property p : s.getOwnedAttributes()) {
			if (p.getName().equalsIgnoreCase("reply")) {
				return true;
			}
		}
		return false;
	}

	public Type getFirstParamType(final Signal s) {
		for (Property p : s.getOwnedAttributes()) {
			if (p.getName().equalsIgnoreCase("reply") == false) {
				return p.getType();
			}
		}
		return null;
	}

	public Type getReplyType(final Signal s) {
		for (Property p : s.getOwnedAttributes()) {
			if (p.getName().equalsIgnoreCase("reply")) {
				return p.getType();
			}
		}
		return null;
	}

	public boolean isReplyTypePrimitive(final Signal s) {
		Type t = getReplyType(s);
		if (t != null) {
			return (t instanceof PrimitiveType);
		}
		return true;
	}

	public boolean isFirstParamTypePrimitive(final Signal s) {
		Type t = getFirstParamType(s);
		if (t != null) {
			return (t instanceof PrimitiveType);
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
