package comodo2.templates.elt.waf;

import com.google.common.collect.Iterables;
import comodo2.queries.QInterface;
import comodo2.queries.QPackage;
import comodo2.utils.FilesHelper;
import java.util.HashMap;
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

public class MalWscript implements IGenerator {
	
	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	private QInterface mQInterface;

	@Inject
	private QPackage mQPackage;

	@Inject
	private FilesHelper mFilesHelper;

	/**
	 * Transform <<cmdoModule>> UML package containing <<cmdoInterface>> UML Interface
	 * into the WAF wscript file for MAL/ICD.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		HashMap<String, String> icdModules = new HashMap<String, String>();
		for (final Interface e : Iterables.<Interface>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), Interface.class)) {
			if ((mQInterface.isToBeGenerated(e) && mQInterface.hasRequests(e))) {
				icdModules.put(mQInterface.getContainerPackage(e).getName(), mQPackage.getContainerPackage(mQInterface.getContainerPackage(e)).getName());
			}
		}
		for (final String m : icdModules.keySet()) {
			String p = (m + "/wscript");
			mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(p));
			fsa.generateFile(p, this.generate(m, icdModules.get(m)));
		}
	}

	public CharSequence generate(final String moduleName, final String parentName) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadWaf.stg");
			
			ST st = g.getInstanceOf("WscriptMal");
			st.add("moduleName", moduleName);
			st.add("parentName", parentName);			
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating wscript file for " + moduleName + " module (" + throwable.getMessage() + ").");			
		}
		return "";		
	}
}
