package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.stringtemplate.v4.*;

public class RadConfig implements IGenerator {
	
	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	private QClass mQClass;

	@Inject
	private FilesHelper mFilesHelper;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into a RAD ActionMgr class.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if (mQClass.isToBeGenerated(e)) {
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath("config")));
				fsa.generateFile(mFilesHelper.toHppFilePath("config"), this.generateHeader(Config.getInstance().getCurrentModule(), "Config"));
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath("config")));
				fsa.generateFile(mFilesHelper.toCppFilePath("config"), this.generateSource(Config.getInstance().getCurrentModule(), "Config"));
			}
		}
	}

	/**
	 * config.hpp
	 */
	public CharSequence generateHeader(final String moduleName, final String className) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppConfig.stg");
			ST st = g.getInstanceOf("ConfigHeader");
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
	 * config.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String className) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppConfig.stg");
			ST st = g.getInstanceOf("ConfigSource");
			st.add("moduleName", moduleName);
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating source file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";
	}
}
