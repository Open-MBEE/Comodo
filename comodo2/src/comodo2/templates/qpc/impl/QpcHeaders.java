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
import comodo2.templates.qpc.model.CurrentGeneration;
import comodo2.templates.qpc.traceability.FileDescriptionHeader;
import comodo2.utils.FilesHelper;



public class QpcHeaders implements IGenerator {

	public CurrentGeneration current;

	@Inject
	private FilesHelper mFilesHelper;

	@Inject
	private QClass mQClass;

	@Inject
	private QStateMachine mQStateMachine;

	@Inject
	private Utils mUtils;
	
	@Inject
	private FileDescriptionHeader mFileDescHeader;


    /**
	 * Generates headers file for execution of the state machine.
	 * This contains, among others, signal and state enumerations.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {


		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class c : _filter) {
			if ((mQClass.isToBeGenerated(c) && mQClass.hasStateMachines(c))) {
				TreeSet<String> signalNames = new TreeSet<String>();
				for (final StateMachine sm : mQClass.getStateMachines(c)) {

					current = new CurrentGeneration(c.getName(), sm.getName());

					Iterables.<String>addAll(signalNames, mQStateMachine.getAllSignalNames(sm));
					
					fsa.generateFile(mFilesHelper.toQmImplFilePath(current.getSmQualifiedName() + "_states.h"), this.generateStatesHeader(current, mQStateMachine.getAllStatesQualifiedName(sm)));						
				}
				fsa.generateFile(mFilesHelper.toQmImplFilePath(current.getClassName() + "_statechart_signals.h"), this.generateSignalsHeader(current.getClassName(), signalNames));						

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

		st.add("fileDescriptionHeader", mFileDescHeader.generateFileDescriptionHeader(className, null, false));
		st.add("className", className);
		st.add("classNameUpperCase", className.toUpperCase());
		st.add("signalsEnumDefinition", signalsEnumString);

		return st.render();
	}

	/**
	 * Generates the header file for the enumeration of states
	 */
	public CharSequence generateStatesHeader(final CurrentGeneration current, final Iterable<String> statesQualifiedNames){
		String statesEnumString = "";

		statesEnumString += current.getSmQualifiedName().toUpperCase() + "__TOP__, /* Top = 0 */\n";
		for (String stateQualifiedName : statesQualifiedNames) {
			statesEnumString += mUtils.formatStateName(stateQualifiedName, current.getSmQualifiedName()) + ",\n";
		}

		STGroup g = new STGroupFile("resources/qpc_tpl/QpcHeaders.stg");
		ST st = g.getInstanceOf("StatesHeader");

		st.add("fileDescriptionHeader", mFileDescHeader.generateFileDescriptionHeader(current.getClassName(), current.getSmName(), false));
		st.add("smQualifiedName", current.getSmQualifiedName());
		st.add("smQualifiedNameUpperCase", current.getSmQualifiedName().toUpperCase());
		st.add("statesEnumDefinition", statesEnumString);
		
		return st.render();
	}

}
