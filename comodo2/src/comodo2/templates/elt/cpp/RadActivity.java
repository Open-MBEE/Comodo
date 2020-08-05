package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.queries.QStateMachine;
import comodo2.utils.FilesHelper;
import java.util.TreeSet;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class RadActivity implements IGenerator {
	
	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	@Extension
	private QClass mQClass;

	@Inject
	@Extension
	private QStateMachine mQStateMachine;

	@Inject
	@Extension
	private FilesHelper mFilesHelper;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into an RAD do-activities C++ class.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if ((mQClass.isToBeGenerated(e) && mQClass.hasStateMachines(e))) {
				Iterable<StateMachine> _stateMachines = mQClass.getStateMachines(e);
				for (final StateMachine sm : _stateMachines) {
					TreeSet<String> _allActivityNames = mQStateMachine.getAllActivityNames(sm);
					for (final String activityName : _allActivityNames) {
						boolean skip = mFilesHelper.skipFile(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath(activityName)));
						if (skip == false) {
							mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath(activityName)));
							fsa.generateFile(mFilesHelper.toHppFilePath(activityName), this.generateHeader(Config.getInstance().getCurrentModule(), activityName));
						}
						if (skip == false) {
							mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath(activityName)));
							fsa.generateFile(mFilesHelper.toCppFilePath(activityName), this.generateSource(Config.getInstance().getCurrentModule(), activityName));
						}
					}
				}
			}
		}
	}

	/**
	 * DoActivity.hpp
	 */
	public CharSequence generateHeader(final String moduleName, final String className) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppActivity.stg");
			ST st = g.getInstanceOf("ActivityHeader");
			st.add("moduleName", moduleName);
			st.add("moduleNameUpperCase", moduleName.toUpperCase());
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classNameUpperCase", className.toUpperCase());	
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating header file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";		
	}

	/**
	 * DoActivity.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String className) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppActivity.stg");
			ST st = g.getInstanceOf("ActivitySource");
			st.add("moduleName", moduleName);
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classNameFileName", mFilesHelper.toFileName(className));				
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating source file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";
	}
}
