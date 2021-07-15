package comodo2.templates.qpc.impl;

import java.util.TreeSet;

import javax.inject.Inject;

import com.google.common.collect.Iterables;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import comodo2.queries.QClass;
import comodo2.queries.QStateMachine;
import comodo2.templates.qpc.Utils;
import comodo2.utils.FilesHelper;



public class QpcHeaders implements IGenerator {


	@Inject
	private FilesHelper mFilesHelper;

	@Inject
	private QClass mQClass;

	@Inject
	private QStateMachine mQStateMachine;

	@Inject
	private Utils mUtils;


    /**
	 * Generates headers file for execution of the state machine.
	 * This contains, among others, signal and state enumerations.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {


		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if ((mQClass.isToBeGenerated(e) && mQClass.hasStateMachines(e))) {
				TreeSet<String> signalNames = new TreeSet<String>();
				for (final StateMachine sm : mQClass.getStateMachines(e)) {

					String smQualifiedName = e.getName() + "_" + sm.getName();
					Iterables.<String>addAll(signalNames, mQStateMachine.getAllSignalNames(sm));
					
					fsa.generateFile(mFilesHelper.toQmImplFilePath(smQualifiedName + "_states.h"), this.generateStatesHeader(smQualifiedName, mQStateMachine.getAllStatesQualifiedName(sm)));						
				}
				fsa.generateFile(mFilesHelper.toQmImplFilePath(e.getName() + "_statechart_signals.h"), this.generateSignalsHeader(e.getName(), signalNames));						

			}
		}


	}

	/**
	 * Generates the header file for the enumeration of signals
	 */
	public CharSequence generateSignalsHeader(final String className, final TreeSet<String> signalNames){
		String str = "";

		//str += printIncludes();

		str +=  "enum " + className + "_statechart_signals {\n" +
				"	/* \"During\" signal */\n" +
				"	DURING = Q_USER_SIG,\n\n" + 
				"	/* User defined signals */\n" ;

		for (String signalName : signalNames) {
			str += "	" + mUtils.formatSignalName(signalName, className) + ",\n";
		}
		
		str +=  "\n	/* Maximum signal id */\n" +
				"	Q_BAIL_SIG = 0x7FFFFFF-1 /* Internal: terminate region/submachine */,\n" +
				"	MAX_SIG    = 0x7FFFFFF   /* Last possible ID! */\n";
		str += "};\n";

		return str;
	}

	/**
	 * Generates the header file for the enumeration of states
	 */
	public CharSequence generateStatesHeader(final String smQualifiedName, final Iterable<String> statesQualifiedNames){
		String str = "";

		//str += printIncludes();

		str +=  "typedef enum " + smQualifiedName + "_state {\n";
		str += "	" + smQualifiedName.toUpperCase() + "__TOP__, /* Top = 0 */\n";
		for (String stateQualifiedName : statesQualifiedNames) {
			str += "	" + mUtils.formatStateName(stateQualifiedName, smQualifiedName) + ",\n";
		}
		
		str += "} " + smQualifiedName + "_states;\n";

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

}
