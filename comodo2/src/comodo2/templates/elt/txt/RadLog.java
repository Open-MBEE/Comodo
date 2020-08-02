package comodo2.templates.elt.txt;

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

public class RadLog implements IGenerator {
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
			if (mQClass.isToBeGenerated(e)) {
				String filename = mFilesHelper.getRelativeConfigPath() + "log.properties";
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(filename));
				fsa.generateFile(filename, this.generate(mQClass.getContainerPackageName(e)));
			}
		}
	}

	public CharSequence generate(final String modName) {
		StringConcatenation str = new StringConcatenation();
		str.append("log4cplus.rootLogger=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.logger.rootLogger.rad=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.logger.rootLogger.rad.sm=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.logger.rootLogger." + modName  + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.appender.console=log4cplus::ConsoleAppender" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.appender.console.layout=log4cplus::PatternLayout" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.appender.console.layout.ConversionPattern=[%D{%H:%M:%S:%q}][%-5p][%c] %m%n" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
	}
}
