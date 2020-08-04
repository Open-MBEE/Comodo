package comodo2.templates.elt.waf;

import com.google.common.collect.Iterables;
import comodo2.queries.QClass;
import comodo2.queries.QInterface;
import comodo2.queries.QPackage;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class RadWscript implements IGenerator {
	@Inject
	@Extension
	private QClass mQClass;

	@Inject
	@Extension
	private QInterface mQInterface;

	@Inject
	@Extension
	private QPackage mQPackage;

	@Inject
	@Extension
	private FilesHelper mFilesHelper;

	/**
	 * Transform <<cmdoModule>> UML package containing <<cmdoComponent>> UML Class
	 * into the WAF wscript file for RAD applications.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class c : _filter) {
			if (mQClass.isToBeGenerated(c)) {
				String ifModules = "";
				for (final Interface i : c.allRealizedInterfaces()) {
					if (mQInterface.hasRequests(i)) {
						ifModules = (ifModules + mQInterface.getContainerPackage(i).getName() + "-cxx ");
					}
				}
				String filename = mQClass.getContainerPackage(c).getName() + "/wscript";
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(filename));
				fsa.generateFile(filename, this.generate(mQClass.getContainerPackage(c).getName(), mQPackage.getContainerPackage(mQClass.getContainerPackage(c)).getName(), c.getName(), ifModules));
				return;
			}
		}
	}

	public CharSequence generate(final String moduleName, final String parentName, final String appName, final String ifModulesName) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadWaf.stg");
			ST st = g.getInstanceOf("WscriptAppl");
			st.add("moduleName", moduleName);
			st.add("parentName", parentName);			
			st.add("appName", appName);			
			st.add("ifModulesName", ifModulesName);			
			return st.render();
		} catch(Throwable throwable) {
			System.out.println("===>>>ERROR " + throwable.getMessage());
		}
		return "";		
	}
}
