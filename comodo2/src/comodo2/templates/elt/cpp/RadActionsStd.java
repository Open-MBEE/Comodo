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
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class RadActionsStd implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	private QClass _qClass;

	@Inject
	private FilesHelper _filesHelper;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into a RAD ActionMgr class.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			boolean _isToBeGenerated = this._qClass.isToBeGenerated(e);
			if (_isToBeGenerated) {
				this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath("actionsStd")));
				fsa.generateFile(this._filesHelper.toHppFilePath("actionsStd"), this.generateHeader(Config.getInstance().getCurrentModule(), "ActionsStd"));
				this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath("actionsStd")));
				fsa.generateFile(this._filesHelper.toCppFilePath("actionsStd"), this.generateSource(Config.getInstance().getCurrentModule(), "ActionsStd"));
			}
		}
	}

	/**
	 * actionsStd.hpp
	 */
	public CharSequence generateHeader(final String moduleName, final String className) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppActionsStd.stg");
			ST st = g.getInstanceOf("ActionsStdHeader");
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
	 * actionsStd.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String className) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppActionsStd.stg");
			ST st = g.getInstanceOf("ActionsStdSource");
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
