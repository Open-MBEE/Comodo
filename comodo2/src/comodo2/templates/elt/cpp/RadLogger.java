package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

public class RadLogger implements IGenerator {
	
	//private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	private QClass mQClass;

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
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath("logger")));
				fsa.generateFile(mFilesHelper.toHppFilePath("logger"), this.generateHeader(Config.getInstance().getCurrentModule(), "Logger"));
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath("logger")));
				fsa.generateFile(mFilesHelper.toCppFilePath("logger"), this.generateSource(Config.getInstance().getCurrentModule(), "Logger"));
			}
		}
	}

	/**
	 * logger.hpp
	 */
	public CharSequence generateHeader(final String moduleName, final String className) {
		return
		"/**\n" +
		" * @file\n" +
		" * @ingroup " + moduleName + "\n" +
		" * @copyright ESO - European Southern Observatory\n" +
		" * @author\n" +
		" *\n" +
		" * @brief " + className + " header file.\n" +
		" */\n" +
		"#ifndef " + moduleName.toUpperCase() + "_LOGGER_HPP_\n" +
		"#define " + moduleName.toUpperCase() + "_LOGGER_HPP_\n" +
		"\n" +
		"#include <rad/logger.hpp>\n" +
		"\n" +
		"namespace " + moduleName.toLowerCase() + " {\n" +
		"\n" +
		"const std::string LOGGER_NAME = \"" + moduleName + "\";\n" +
		"\n" +
		"log4cplus::Logger& GetLogger();\n" +
		"\n" +
		"}  // namespace " + moduleName.toLowerCase() +	"\n" +
		"\n" +		
		"#endif  // " + moduleName.toUpperCase() + "_LOGGER_HPP_\n";
	}

	/**
	 * logger.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String className) {
		return
		"/**\n" +
		" * @file\n" +
		" * @ingroup " + moduleName + "\n" +
		" * @copyright ESO - European Southern Observatory\n" +
		" * @author\n" +
		" *\n" +
		" * @brief " + className + " source file.\n" +
		" */\n" +
		"\n" + 
		"#include <" + moduleName + "/logger.hpp>\n" +
		"\n" + 
		"namespace " + moduleName.toLowerCase() + " {\n" +
		"\n" + 
		"log4cplus::Logger& GetLogger() {\n" +
		"    static log4cplus::Logger logger = log4cplus::Logger::getInstance(LOGGER_NAME);\n" +
		"    return logger;\n" +
		"}\n" +
		"\n" + 
		"}  // namespace " + moduleName.toLowerCase() + "\n";
	}
}
