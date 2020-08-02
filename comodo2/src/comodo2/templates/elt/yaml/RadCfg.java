package comodo2.templates.elt.yaml;

import com.google.common.collect.Iterables;
import comodo2.queries.QClass;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

public class RadCfg implements IGenerator {
	@Inject
	@Extension
	private QClass mQClass;

	@Inject
	@Extension
	private FilesHelper mFilesHelper;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into an RAD Events DSL.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			boolean _isToBeGenerated = mQClass.isToBeGenerated(e);
			if (_isToBeGenerated) {
				String _relativeConfigPath = mFilesHelper.getRelativeConfigPath();
				String filename = (_relativeConfigPath + "config.yaml");
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(filename));
				fsa.generateFile(filename, this.generate(mQClass.getContainerPackageName(e)));
			}
		}
	}

	public CharSequence generate(final String modName) {
		StringConcatenation str = new StringConcatenation();
		str.append("cfg.req.endpoint    : \"zpb.rr://127.0.0.1:12081/\" # IP address and port used to accept requests" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("cfg.db.timeout_sec  : 2              # timeout in seconds when connecting to runtime DB"  + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("cfg.sm.scxml        : \"" + modName + "/sm.xml\"" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("cfg.log.properties  : \"" + modName + "/log.properties\"" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}
}
