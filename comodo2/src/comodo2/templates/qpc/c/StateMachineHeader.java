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
import comodo2.utils.FilesHelper;

public class StateMachineHeader implements IGenerator {
	
	@Inject
	private QClass mQClass;
	
	@Inject
	private FilesHelper mFilesHelper;
	


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
						String smQualifiedName = c.getName() + "_" + sm.getName();
						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(sm.getName())));
						fsa.generateFile(mFilesHelper.toHFilePath(smQualifiedName), this.generate(smQualifiedName, c.getName()));						
					}
				}				
			}
		}
	}


	public CharSequence generate(String smQualifiedName, String className) {
        STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineHeader.stg");
		ST st = g.getInstanceOf("StateMachineHeader");
		st.add("className", className);
		st.add("smQualifiedName", smQualifiedName);
		st.add("smQualifiedNameUpperCase", smQualifiedName.toUpperCase());

        return st.render();
	}

}