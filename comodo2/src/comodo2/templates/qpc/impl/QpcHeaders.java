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
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

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
		String signalsEnumString = "";
		
		for (String signalName : signalNames) {
			signalsEnumString += mUtils.formatSignalName(signalName, className) + ",\n";
		}

		STGroup g = new STGroupFile("resources/qpc_tpl/QpcHeaders.stg");
		ST st = g.getInstanceOf("SignalsHeader");

		st.add("className", className);
		st.add("classNameUpperCase", className.toUpperCase());
		st.add("signalsEnumDefinition", signalsEnumString);

		return st.render();
	}

	/**
	 * Generates the header file for the enumeration of states
	 */
	public CharSequence generateStatesHeader(final String smQualifiedName, final Iterable<String> statesQualifiedNames){
		String statesEnumString = "";

		statesEnumString += smQualifiedName.toUpperCase() + "__TOP__, /* Top = 0 */\n";
		for (String stateQualifiedName : statesQualifiedNames) {
			statesEnumString += mUtils.formatStateName(stateQualifiedName, smQualifiedName) + ",\n";
		}

		STGroup g = new STGroupFile("resources/qpc_tpl/QpcHeaders.stg");
		ST st = g.getInstanceOf("StatesHeader");

		st.add("smQualifiedName", smQualifiedName);
		st.add("smQualifiedNameUpperCase", smQualifiedName.toUpperCase());
		st.add("statesEnumDefinition", statesEnumString);
		
		return st.render();
	}

}
