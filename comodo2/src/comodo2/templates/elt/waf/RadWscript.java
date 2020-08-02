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
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

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
		StringConcatenation str = new StringConcatenation();
		str.append("\"\"\"" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@file" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@ingroup " + moduleName + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@copyright ESO - European Southern Observatory" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.newLine();
		str.append("@defgroup " + moduleName + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@ingroup " + parentName + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("@brief " + moduleName + " Application module." + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("\"\"\""  + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.newLine();
		str.append("from wtools.module import declare_cprogram" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.newLine();
		str.append("declare_cprogram(target=\'" + appName + "\'," + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("                 " + "features=\'radgen\'," + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("                 " + "use=\'log4cplus yaml-cpp hiredis protobuf xerces-c rad.cpp.core rad.cpp.services rad.cpp.events rad.cpp.sm rad.cpp.utils rad.cpp.mal " + ifModulesName + "\')");
		str.newLineIfNotEmpty();
		return str;
	}
}
