package comodo2.templates.elt.yaml;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.queries.QClass;
import comodo2.queries.QInterface;
import comodo2.queries.QSignal;
import comodo2.queries.QStereotype;
import comodo2.queries.QTransition;
import comodo2.utils.FilesHelper;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

public class RadEv implements IGenerator {
	@Inject
	@Extension
	private QClass mQClass;

	@Inject
	@Extension
	private QTransition mQTransition;

	@Inject
	@Extension
	private QStereotype mQStereotype;

	@Inject
	@Extension
	private QSignal mQSignal;

	@Inject
	@Extension
	private QInterface mQInterface;

	@Inject
	@Extension
	private Types mTypes;

	@Inject
	@Extension
	private FilesHelper mFilesHelper;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into an RAD Events DSL.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if (mQClass.isToBeGenerated(e)) {
				for (final Interface i : e.allRealizedInterfaces()) {
					mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toRadEvFilePath(i.getName())));
					fsa.generateFile(mFilesHelper.toRadEvFilePath(i.getName()), this.generate(e, i));
				}
			}
		}
	}

	public CharSequence generate(final org.eclipse.uml2.uml.Class c, final Interface i) {
		StringConcatenation str = new StringConcatenation();
		str.append("# Events definitions for " + c.getName() + " application." + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("version: \"1.0\"" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("namespace: " + i.getName() + StringConcatenation.DEFAULT_LINE_DELIMITER);
		if (mQInterface.hasRequests(i)) {
			str.append("includes:" + StringConcatenation.DEFAULT_LINE_DELIMITER);
			str.append("    " + "- boost/exception_ptr.hpp" + StringConcatenation.DEFAULT_LINE_DELIMITER);
			str.append("    " + "- rad/mal/request.hpp" + StringConcatenation.DEFAULT_LINE_DELIMITER);
			str.append("    " + "- " + mFilesHelper.title(mQInterface.getContainerPackage(i).getName()) + ".hpp" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		}
		str.newLine();
		str.append("events:" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("    " + exploreEvents(getAllSignals(i), mQInterface.getContainerPackage(i).getName()), "    ");
		str.newLine();
		return str;
	}

	/**
	 * This function finds all signals of an Interfaces.
	 */
	public HashSet<Signal> getAllSignals(final Interface i) {
		HashSet<Signal> allSignals = new HashSet<Signal>();
		final Function1<Reception, String> _function = (Reception e) -> {
			return e.getName();
		};
		List<Reception> _sortBy = IterableExtensions.<Reception, String>sortBy(i.getOwnedReceptions(), _function);
		for (final Reception r : _sortBy) {
			allSignals.add(r.getSignal());
		}
		return allSignals;
	}

	/**
	 * This function finds all signals of the class's realized Interfaces.
	 */
	public HashSet<Signal> getAllSignals(final org.eclipse.uml2.uml.Class c) {
		HashSet<Signal> allSignals = new HashSet<Signal>();
		final Function1<Interface, Boolean> _function = (Interface e) -> {
			return Boolean.valueOf(mQInterface.isToBeGenerated(e));
		};
		Iterable<Interface> _filter = IterableExtensions.<Interface>filter(c.allRealizedInterfaces(), _function);
		for (final Interface i : _filter) {
			final Function1<Reception, String> _function_1 = (Reception e) -> {
				return e.getName();
			};
			List<Reception> _sortBy = IterableExtensions.<Reception, String>sortBy(i.getOwnedReceptions(), _function_1);
			for (final Reception r : _sortBy) {
				allSignals.add(r.getSignal());
			}
		}
		return allSignals;
	}

	/**
	 * This function finds all signals of a State Machine.
	 */
	public HashSet<Signal> getAllSignals(final StateMachine sm) {
		HashSet<Signal> allSignals = new HashSet<Signal>();
		Iterable<Transition> _filter = Iterables.<Transition>filter(sm.allOwnedElements(), Transition.class);
		for (final Transition t : _filter) {
			Signal _firstEvent = mQTransition.getFirstEvent(t);
			boolean _tripleNotEquals = (_firstEvent != null);
			if (_tripleNotEquals) {
				allSignals.add(mQTransition.getFirstEvent(t));
			}
		}
		return allSignals;
	}

	/**
	 * Looks for events in the state machine.
	 */
	public CharSequence exploreEvents(final HashSet<Signal> allSignals, final String ifModName) {
		StringConcatenation str = new StringConcatenation();
		for(final Signal s : allSignals) {
			str.append(mQSignal.nameWithoutPrefix(s) + ":" + StringConcatenation.DEFAULT_LINE_DELIMITER);
			if ((mQStereotype.isComodoCommand(((Element) s)) && mQSignal.hasReply(s))) {
				str.append("    " + "payload: " + explorePayload(s, ifModName), "    ");
				str.newLineIfNotEmpty();
			}
			str.append("    " + "doc: event documentation to be added" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		}
		return str;
	}

	public CharSequence explorePayload(final Signal s, final String ifModName) {
		StringConcatenation str = new StringConcatenation();
		if (mQSignal.hasParam(s)) {
			str.append(printPayload(mQSignal.isReplyTypePrimitive(s), mTypes.typeName(mQSignal.getReplyType(s)), mQSignal.isFirstParamTypePrimitive(s), mTypes.typeName(mQSignal.getFirstParamType(s)), ifModName));
		} else {
			str.append(printPayload(mQSignal.isReplyTypePrimitive(s), mTypes.typeName(mQSignal.getReplyType(s)), false, "", ifModName));
		}
		str.newLineIfNotEmpty();
		return str;
	}

	public CharSequence printPayload(final boolean isReplyTypePrimitive, final String replyTypeName, final boolean isParamTypePrimitive, final String paramTypeName, final String ifModName) {
		String str = "rad::cii::Request<";
		if (!Objects.equal(replyTypeName, "")) {
			if (isReplyTypePrimitive) {
				String _str = str;
				str = (_str + replyTypeName);
			} else {
				String _str_1 = str;
				str = (_str_1 + (((("std::shared_ptr<" + ifModName) + "::") + replyTypeName) + ">"));
			}
		}
		if (!Objects.equal(paramTypeName, "")) {
			String _str_2 = str;
			str = (_str_2 + ", ");
			if (isParamTypePrimitive) {
				String _str_3 = str;
				str = (_str_3 + paramTypeName);
			} else {
				String _str_4 = str;
				str = (_str_4 + (((("std::shared_ptr<" + ifModName) + "::") + paramTypeName) + ">"));
			}
		}
		String _str_5 = str;
		str = (_str_5 + ">");
		return str;
	}
}
