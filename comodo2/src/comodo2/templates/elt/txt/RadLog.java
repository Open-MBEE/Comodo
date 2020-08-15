package comodo2.templates.elt.txt;

import comodo2.queries.QClass;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class RadLog implements IGenerator {
	
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
/*		
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if (mQClass.isToBeGenerated(e)) {
				String filename = mFilesHelper.getRelativeConfigPath() + "log.properties";
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(filename));
				fsa.generateFile(filename, this.generate(mQClass.getContainerPackageName(e)));
			}
		}
*/		
		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof org.eclipse.uml2.uml.Class) {
				org.eclipse.uml2.uml.Class c = (org.eclipse.uml2.uml.Class)e; 
				if (mQClass.isToBeGenerated(c)) {
					String filename = mFilesHelper.getRelativeConfigPath() + "log.properties";
					mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(filename));
					fsa.generateFile(filename, generate(mQClass.getContainerPackageName(c)));
				}
			}
		}		
	}

	public CharSequence generate(final String modName) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadTxtLog.stg");
			ST st = g.getInstanceOf("LogProperties");
			st.add("moduleName", modName);
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating log.properties file for " + modName + " module (" + throwable.getMessage() + ").");
		}
		return "";
/*
 * Xtend2		
 */
/*
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("log4cplus.rootLogger=INFO, console");
    _builder.newLine();
    _builder.append("log4cplus.logger.rootLogger.rad=INFO, console");
    _builder.newLine();
    _builder.append("log4cplus.logger.rootLogger.rad.sm=INFO, console");
    _builder.newLine();
    _builder.append("log4cplus.logger.rootLogger.");
    _builder.append(modName);
    _builder.append("=INFO, console");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("log4cplus.appender.console=log4cplus::ConsoleAppender");
    _builder.newLine();
    _builder.append("log4cplus.appender.console.layout=log4cplus::PatternLayout");
    _builder.newLine();
    _builder.append("log4cplus.appender.console.layout.ConversionPattern=[%D{%H:%M:%S:%q}][%-5p][%c] %m%n");
    _builder.newLine();
    return _builder;
		
 */
/*
 * Java		
 */
/*		
		return 
		"log4cplus.rootLogger=INFO, console\n" +
		"log4cplus.logger.rootLogger.rad=INFO, console\n" + 
		"log4cplus.logger.rootLogger.rad.sm=INFO, console\n" +
		"log4cplus.logger.rootLogger." + modName + "=INFO, console\n" +
		"log4cplus.appender.console=log4cplus::ConsoleAppender\n" +
		"log4cplus.appender.console.layout=log4cplus::PatternLayout\n" +
		"log4cplus.appender.console.layout.ConversionPattern=[%D{%H:%M:%S:%q}][%-5p][%c] %m%n\n";
*/		
/*
		StringConcatenation str = new StringConcatenation();
		str.append("log4cplus.rootLogger=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.logger.rootLogger.rad=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.logger.rootLogger.rad.sm=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.logger.rootLogger." + modName  + "=INFO, console" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.appender.console=log4cplus::ConsoleAppender" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.appender.console.layout=log4cplus::PatternLayout" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		str.append("log4cplus.appender.console.layout.ConversionPattern=[%D{%H:%M:%S:%q}][%-5p][%c] %m%n" + StringConcatenation.DEFAULT_LINE_DELIMITER);
		return str;
*/		
	}
}
