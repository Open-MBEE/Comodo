package comodo2.templates.qf.qm;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QBehavior;
import comodo2.queries.QClass;
import comodo2.queries.QRegion;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.queries.QTransition;
import comodo2.utils.FilesHelper;
import comodo2.utils.StateComparator;
import comodo2.utils.TransitionComparator;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;



public class QmImplFiles implements IGenerator {


	@Inject
	private FilesHelper mFilesHelper;

	@Inject
	private QClass mQClass;

	@Inject
	private QStateMachine mQStateMachine;

	// %1$s is impl_name      %2$s is funciton_name        "%%s" is escaping %s, which we want in the output
	final private String GUARD_FUNCTION_SOURCE_TEMPLATE = "" +
		"bool %1$s_impl_%2$s () {\n" +
		"	bool rv = this.%2$s;\n" +
		"	printf(\"%%s.%2$s() == %%s\", \"MISSING_ARGUMENT\");\n" +
		"	return rv;\n" +
		"};\n";

	final private String ACTION_FUNCTION_SOURCE_TEMPLATE = "" +
		"void %1$s_impl_%2$s () {\n" +
		"	printf(\"%%s.%2$s() default action implementation invoked\\n\", \"MISSING_ARGUMENT\");\n" +
		"}\n";

    /**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into a Quantum Framework XML file for the Quantum Modeler.
	 * 
	 * The UML Class should:
	 * - be inside a UML Package with stereotype cmdoModule
	 * - the cmdoModule name should have been provided in the configuration
	 * - have stereotype cmdoComponent
	 * - have an associated UML State Machine
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {


		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if ((mQClass.isToBeGenerated(e) && mQClass.hasStateMachines(e))) {
				TreeSet<String> actionNames = new TreeSet<String>();
				TreeSet<String> guardNames = new TreeSet<String>();
				for (final StateMachine sm : mQClass.getStateMachines(e)) {

					Iterables.<String>addAll(actionNames, mQStateMachine.getAllActionNames(sm));
					Iterables.<String>addAll(guardNames, mQStateMachine.getAllGuardNames(sm));
					
					fsa.generateFile(mFilesHelper.toQmImplFilePath(sm.getName() + "_impl.c"), this.generateImplSource(sm.getName(), actionNames, guardNames));						
					fsa.generateFile(mFilesHelper.toQmImplFilePath(sm.getName() + "_impl.h"), this.generateImplHeader(sm.getName(), actionNames, guardNames));						
				}

			}
		}


	}

	/**
	 * Generates the source file for the implementation of actions and guards in the model
	 */
	public CharSequence generateImplSource(final String smName, final TreeSet<String> actionNames, final TreeSet<String> guardNames){
		String str = "";

		str += printIncludes();

		for (String guardName : guardNames) {
			str += String.format(GUARD_FUNCTION_SOURCE_TEMPLATE, smName, getFunctionName(guardName));
			str += "\n";
		}

		for (String actionName : actionNames) {
			str += String.format(ACTION_FUNCTION_SOURCE_TEMPLATE, smName, getFunctionName(actionName));
			str += "\n";
		}

		return str;
	}

	/**
	 * Generates the header file for the implementation of behaviors and guards in the model
	 */
	public CharSequence generateImplHeader(final String smName, final TreeSet<String> actionNames, final TreeSet<String> guardNames){
		String str = "";

		str += printIncludes();

		str += "typedef struct " + smName + "_impl {\n" +
			"	char machineName[128];\n" +
			"	QActive *active;\n\n";

		for (String guardName : guardNames) {
			str += String.format("	bool %s;\n", getFunctionName(guardName));
		}

		str += "};\n\n";

		for (String guardName : guardNames) {
			str += String.format("bool %s_impl_%s();\n", smName, getFunctionName(guardName));
		}

		for (String actionName : actionNames) {
			str += String.format("void %s_impl_%s();", smName, getFunctionName(actionName));
			str += "\n";
		}
		
		return str;
	}


	public CharSequence printGuardFunction(final String guardName){
		String str = "";

		

		return str;
	}

	public CharSequence printActionFunction(final String guardName){
		String str = "";

		return str;
	}

	public CharSequence printIncludes(){
		String str = "";

		str += "#include <stdio.h>\n";
		str += "#include <stdlib.h>\n";
		str += "#include <string.h>\n";
		str += "#include <assert.h>\n";
		str += "#include <stdbool.h>\n";

		str += "\n\n";

		return str;
	}

	public CharSequence getFunctionName(String str){
		// TODO: this is very brittle and shall be replaced with a more robust regex
		// It basically only takes what's before the first set of parentheses
		return str.replaceAll("(?s)\\(\\).*","");
	}
}
