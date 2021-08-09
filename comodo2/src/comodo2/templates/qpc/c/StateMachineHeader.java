package comodo2.templates.qpc.c;

import javax.inject.Inject;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import comodo2.queries.QClass;
import comodo2.queries.QStateMachine;
import comodo2.templates.qpc.model.CurrentGeneration;
import comodo2.templates.qpc.traceability.FileDescriptionHeader;
import comodo2.utils.FilesHelper;

public class StateMachineHeader implements IGenerator {
	
	@Inject
	private QClass mQClass;

	@Inject
	private QStateMachine mQStateMachine;
	
	@Inject
	private FilesHelper mFilesHelper;
	
	@Inject
	private FileDescriptionHeader mFileDescHeader;

	public CurrentGeneration current;

	/**
	 * Generates the header file for the State Machine source file.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		
		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof org.eclipse.uml2.uml.Class) {
				org.eclipse.uml2.uml.Class c = (org.eclipse.uml2.uml.Class)e; 
				if ((mQClass.isToBeGenerated(c) && mQClass.hasStateMachines(c))) {
					for (final StateMachine sm : mQClass.getStateMachines(c)) {
						// Sets current generation context
						current = new CurrentGeneration(c.getName(), sm.getName());

						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(current.getSmQualifiedName())));
						fsa.generateFile(mFilesHelper.toHFilePath(current.getSmQualifiedName()), this.generate(sm, current));						
					}
				}				
			}
		}
	}


	public CharSequence generate(StateMachine sm, CurrentGeneration current) {
        STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineHeader.stg");
		ST st = g.getInstanceOf("StateMachineHeader");
		st.add("fileDescriptionHeader", mFileDescHeader.generateFileDescriptionHeader(current.getClassName(), sm.getName(), true));
		st.add("className", current.getClassName());
		st.add("smQualifiedName", current.getSmQualifiedName());
		st.add("smQualifiedNameUpperCase", current.getSmQualifiedName().toUpperCase());

		// StringTemplate is able to run a forEach on lists, and get the "name" attribute with getName()
		// which is defined for a State element. See https://github.com/antlr/stringtemplate4/blob/master/doc/templates.md
		st.add("statesList", mQStateMachine.getAllStatesSorted(sm));
		st.add("historyPseudostatesList", mQStateMachine.getAllHistoryPseudostates(sm));
		st.add("timeEventList", mQStateMachine.getAllStatesWithTimeEvents(sm));

        return st.render();
	}

}
