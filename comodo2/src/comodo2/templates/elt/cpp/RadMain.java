package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.queries.QInterface;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class RadMain implements IGenerator {
	
	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	private QClass mQClass;

	@Inject
	private QInterface mQInterface;

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
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath("main")));
				fsa.generateFile(mFilesHelper.toCppFilePath("main"), this.generateSource(Config.getInstance().getCurrentModule(), "main", e));
			}
		}
	}

	/**
	 * main.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String fileName, final org.eclipse.uml2.uml.Class c) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppMain.stg");
			ST st = g.getInstanceOf("MainCpp");
			st.add("moduleName", moduleName);
			st.add("fileName", fileName);
			st.add("moreIncludes", printMoreIncludes(moduleName, c));
			st.add("fullyQualifiedStateNames", Config.getInstance().generateFullyQualifiedStateNames());	
			st.add("ifRegistration", printIfRegistration(moduleName, c));	
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating Main file for " + moduleName + " module (" + throwable.getMessage() + ").");
		}
		return "";

	}

	public CharSequence printMoreIncludes(final String moduleName, final org.eclipse.uml2.uml.Class c) {
		String s = "";
		for(final Interface i : c.allRealizedInterfaces()) {
			if (mQInterface.hasRequests(i)) {
				s += "#include <" +	moduleName + "/" + mFilesHelper.toFileName(i.getName()) + "Impl.hpp>\n";
			}
		}
		return s;
	}

	public CharSequence printIfRegistration(final String moduleName, final org.eclipse.uml2.uml.Class c) {
		String s = "";
		for (final Interface i1 : c.allRealizedInterfaces()) {
			if (mQInterface.hasRequests(i1)) {
				String s1 = mQInterface.getContainerPackage(i1).getName() + "::Async" + i1.getName();
				s += "mal_replier.RegisterService<" + s1 + ">(\"" + i1.getName() + "\",\n";
				s += "    std::make_shared<" + moduleName + "::" + i1.getName() + "Impl>(state_machine));\n";
			}
		}
		return s;
	}

}
