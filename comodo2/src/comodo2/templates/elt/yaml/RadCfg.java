package comodo2.templates.elt.yaml;

import com.google.common.collect.Iterables;
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

public class RadCfg implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	private QClass mQClass;

	@Inject
	private FilesHelper mFilesHelper;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into an RAD Events DSL.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if (mQClass.isToBeGenerated(e)) {
				String filename = (mFilesHelper.getRelativeConfigPath() + "config.yaml");
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(filename));
				fsa.generateFile(filename, this.generate(mQClass.getContainerPackageName(e)));
			}
		}
	}

	public CharSequence generate(final String modName) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadYamlCfg.stg");
			ST st = g.getInstanceOf("CfgFile");
			st.add("moduleName", modName);
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating Application Config file for " + modName + " module (" + throwable.getMessage() + ").");
		}
		return "";
	}
}
