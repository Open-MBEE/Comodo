package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class RadDbInterface implements IGenerator {
	@Inject
	@Extension
	private QClass mQClass;

	@Inject
	@Extension
	private FilesHelper mFilesHelper;

	private STGroup mTemplates = new STGroupFile("resources/tpl/EltRadCppDbInterface.stg");
	
	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into a RAD ActionMgr class.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if (mQClass.isToBeGenerated(e)) {
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath("dbInterface")));
				fsa.generateFile(mFilesHelper.toHppFilePath("dbInterface"), this.generateHeader(Config.getInstance().getCurrentModule(), "DbInterface"));
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath("dbInterface")));
				fsa.generateFile(mFilesHelper.toCppFilePath("dbInterface"), this.generateSource(Config.getInstance().getCurrentModule(), "DbInterface"));
			}
		}
	}

	/**
	 * dbInterface.hpp
	 */
	public CharSequence generateHeader(final String moduleName, final String className) {
		try {
			//STGroup g = new STGroupFile("resources/tpl/EltRadCppDbInterface.stg");
			ST st = mTemplates.getInstanceOf("DbInterfaceHeader");
			st.add("moduleName", moduleName);
			st.add("moduleNameUpperCase", moduleName.toUpperCase());
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classNameUpperCase", className.toUpperCase());	
			return st.render();
		} catch(Throwable throwable) {
			System.out.println("===>>>ERROR " + throwable.getMessage());
		}		
		return "";
	}

	/**
	 * dbInterface.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String className) {
		try {
			//STGroup g = new STGroupFile("resources/tpl/EltRadCppDbInterface.stg");
			ST st = mTemplates.getInstanceOf("DbInterfaceSource");
			st.add("moduleName", moduleName);
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			return st.render();
		} catch(Throwable throwable) {
			System.out.println("===>>>ERROR " + throwable.getMessage());
		}
		return "";						
	}
}
