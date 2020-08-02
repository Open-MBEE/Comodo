package comodo2.templates.elt.waf;

import com.google.common.collect.Iterables;
import comodo2.queries.QInterface;
import comodo2.queries.QPackage;
import comodo2.utils.FilesHelper;
import java.util.HashMap;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

public class MalWscript implements IGenerator {
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
		StringConcatenation str = new StringConcatenation();
		str.append("\"\"\"" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@file" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@ingroup " + moduleName + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@copyright ESO - European Southern Observatory" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.newLine();
		str.append("@defgroup " + moduleName + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@ingroup " + parentName + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@brief " + moduleName + " CII/MAL interface module." + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("\"\"\"" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.newLine();
		str.append("from wtools.module import declare_malicd" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.newLine();
		str.append("declare_malicd()" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.newLine();
		return str;
	}
}
