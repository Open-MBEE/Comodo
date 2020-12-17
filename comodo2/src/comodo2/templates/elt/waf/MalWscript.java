package comodo2.templates.elt.waf;

import comodo2.queries.QInterface;
import comodo2.queries.QPackage;
import comodo2.utils.FilesHelper;
import java.util.HashMap;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
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
		HashMap<String, String> icdIncludedModules = new HashMap<String, String>();

		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof Interface) {
				Interface i = (Interface)e; 
				if ((mQInterface.isToBeGenerated(i) && mQInterface.hasRequests(i))) {
					org.eclipse.uml2.uml.Package p = mQInterface.getContainerPackage(i);
					icdModules.put(p.getName(), mQPackage.getContainerPackage(p).getName());
					/*
					 * check for dependencies with other interfaces
					 */
					String useModulesStr = "";
					boolean hasOne = false;
					for (PackageImport pi : p.getPackageImports()) {
						if (pi.getImportedPackage() != null) {
							if (hasOne == false) {
								useModulesStr += "use=[" + "'" + pi.getImportedPackage().getName() + "'";
								hasOne = true;
							} else {
								useModulesStr += ", '" + pi.getImportedPackage().getName() + "'";
							}
						}
					}
					if (hasOne) {
						useModulesStr += "]";
					}
					icdIncludedModules.put(p.getName(), useModulesStr);					
				}
			}
		}

		for (final String m : icdModules.keySet()) {
			String p = (m + "/wscript");
			mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(p));
			fsa.generateFile(p, this.generate(m, icdModules.get(m), icdIncludedModules.get(m)));
		}
	}

	public CharSequence generate(final String moduleName, final String parentName, final String usedModules) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadWaf.stg");
			
			ST st = g.getInstanceOf("WscriptMal");
			st.add("moduleName", moduleName);
			st.add("parentName", parentName);			
			st.add("usedModules", usedModules);			
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating wscript file for " + moduleName + " module (" + throwable.getMessage() + ").");			
		}
		return "";		
	}
}
