package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
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

@SuppressWarnings("all")
public class RadLogger implements IGenerator {
  @Inject
  @Extension
  private QClass _qClass;
  
  @Inject
  @Extension
  private FilesHelper _filesHelper;
  
  /**
   * Transform UML State Machine associated to a class (classifier behavior)
   * into a RAD ActionMgr class.
   */
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
    for (final org.eclipse.uml2.uml.Class e : _filter) {
      boolean _isToBeGenerated = this._qClass.isToBeGenerated(e);
      if (_isToBeGenerated) {
        this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath("logger")));
        fsa.generateFile(this._filesHelper.toHppFilePath("logger"), this.generateHeader(Config.getInstance().getCurrentModule(), "Logger"));
        this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath("logger")));
        fsa.generateFile(this._filesHelper.toCppFilePath("logger"), this.generateSource(Config.getInstance().getCurrentModule(), "Logger"));
      }
    }
  }
  
  /**
   * logger.hpp
   */
  public CharSequence generateHeader(final String moduleName, final String className) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @file");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @ingroup ");
    _builder.append(moduleName, " ");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* @copyright ESO - European Southern Observatory");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @author");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @brief ");
    _builder.append(className, " ");
    _builder.append(" header file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("#ifndef ");
    String _upperCase = moduleName.toUpperCase();
    _builder.append(_upperCase);
    _builder.append("_");
    String _upperCase_1 = className.toUpperCase();
    _builder.append(_upperCase_1);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define ");
    String _upperCase_2 = moduleName.toUpperCase();
    _builder.append(_upperCase_2);
    _builder.append("_");
    String _upperCase_3 = className.toUpperCase();
    _builder.append(_upperCase_3);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <rad/logger.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("namespace ");
    String _lowerCase = moduleName.toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("const std::string LOGGER_NAME = \"");
    _builder.append(moduleName);
    _builder.append("\";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("log4cplus::Logger& GetLogger();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}  // namespace ");
    String _lowerCase_1 = moduleName.toLowerCase();
    _builder.append(_lowerCase_1);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif  // ");
    String _upperCase_4 = moduleName.toUpperCase();
    _builder.append(_upperCase_4);
    _builder.append("_");
    String _upperCase_5 = className.toUpperCase();
    _builder.append(_upperCase_5);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * logger.cpp
   */
  public CharSequence generateSource(final String moduleName, final String className) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @file");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @ingroup ");
    _builder.append(moduleName, " ");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* @copyright ESO - European Southern Observatory");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @author");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @brief ");
    _builder.append(className, " ");
    _builder.append(" source file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/logger.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("namespace ");
    String _lowerCase = moduleName.toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("log4cplus::Logger& GetLogger() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("static log4cplus::Logger logger = log4cplus::Logger::getInstance(LOGGER_NAME);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return logger;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}  // namespace ");
    String _lowerCase_1 = moduleName.toLowerCase();
    _builder.append(_lowerCase_1);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
