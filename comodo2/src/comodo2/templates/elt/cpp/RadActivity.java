package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.queries.QStateMachine;
import comodo2.utils.FilesHelper;
import java.util.TreeSet;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class RadActivity implements IGenerator {
  @Inject
  @Extension
  private QClass _qClass;
  
  @Inject
  @Extension
  private QStateMachine _qStateMachine;
  
  @Inject
  @Extension
  private FilesHelper _filesHelper;
  
  /**
   * Transform UML State Machine associated to a class (classifier behavior)
   * into an RAD do-activities C++ class.
   */
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
    for (final org.eclipse.uml2.uml.Class e : _filter) {
      if ((this._qClass.isToBeGenerated(e) && this._qClass.hasStateMachines(e))) {
        Iterable<StateMachine> _stateMachines = this._qClass.getStateMachines(e);
        for (final StateMachine sm : _stateMachines) {
          TreeSet<String> _allActivityNames = this._qStateMachine.getAllActivityNames(sm);
          for (final String activityName : _allActivityNames) {
            {
              boolean _skipFile = this._filesHelper.skipFile(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath(activityName)));
              boolean _equals = (_skipFile == false);
              if (_equals) {
                this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath(activityName)));
                fsa.generateFile(this._filesHelper.toHppFilePath(activityName), this.generateHeader(Config.getInstance().getCurrentModule(), activityName));
              }
              boolean _skipFile_1 = this._filesHelper.skipFile(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath(activityName)));
              boolean _equals_1 = (_skipFile_1 == false);
              if (_equals_1) {
                this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath(activityName)));
                fsa.generateFile(this._filesHelper.toCppFilePath(activityName), this.generateSource(Config.getInstance().getCurrentModule(), activityName));
              }
            }
          }
        }
      }
    }
  }
  
  /**
   * DoActivity.hpp
   */
  public CharSequence generateHeader(final String moduleName, final String activityName) {
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
    _builder.append(activityName, " ");
    _builder.append(" class header file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("#ifndef ");
    String _upperCase = moduleName.toUpperCase();
    _builder.append(_upperCase);
    _builder.append("_");
    String _upperCase_1 = activityName.toUpperCase();
    _builder.append(_upperCase_1);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define ");
    String _upperCase_2 = moduleName.toUpperCase();
    _builder.append(_upperCase_2);
    _builder.append("_");
    String _upperCase_3 = activityName.toUpperCase();
    _builder.append(_upperCase_3);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/logger.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <rad/activity.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/smAdapter.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <string>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("namespace ");
    String _lowerCase = moduleName.toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("class DataContext;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This class contains the implementation of the do-activity used");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* to simulate the axes movements.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("class ");
    _builder.append(activityName);
    _builder.append(" : public rad::ThreadActivity {");
    _builder.newLineIfNotEmpty();
    _builder.append("   ");
    _builder.append("public:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Constructor.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] id Name of the activity.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] sm State Machine facade.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] data Data shared within the application among actions and activities.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(activityName, "    ");
    _builder.append("(const std::string& id, rad::SMAdapter& sm, DataContext& data);");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("virtual ~");
    _builder.append(activityName, "    ");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Thread implementation method.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void Run() override;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append(activityName, "    ");
    _builder.append("(const ");
    _builder.append(activityName, "    ");
    _builder.append("&) = delete;             //! Disable copy constructor");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append(activityName, "    ");
    _builder.append("& operator=(const ");
    _builder.append(activityName, "    ");
    _builder.append("&) = delete;  //! Disable assignment operator");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("   ");
    _builder.append("private:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("log4cplus::Logger m_logger = log4cplus::Logger::getInstance(LOGGER_NAME + \".");
    _builder.append(activityName, "    ");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("rad::SMAdapter& m_sm;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("DataContext& m_data;");
    _builder.newLine();
    _builder.append("};");
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
    String _upperCase_5 = activityName.toUpperCase();
    _builder.append(_upperCase_5);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * DoActivity.cpp
   */
  public CharSequence generateSource(final String moduleName, final String activityName) {
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
    _builder.append(activityName, " ");
    _builder.append(" class source file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/");
    String _fileName = this._filesHelper.toFileName(activityName);
    _builder.append(_fileName);
    _builder.append(".hpp>");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/dataContext.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/dbInterface.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <events.rad.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <rad/assert.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/exceptions.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/mal/publisher.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <chrono>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("namespace ");
    String _lowerCase = moduleName.toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(activityName);
    _builder.append("::");
    _builder.append(activityName);
    _builder.append("(const std::string& id, rad::SMAdapter& sm, DataContext& data)");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append(": rad::ThreadActivity(id), m_sm(sm), m_data(data) {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("RAD_TRACE(GetLogger());");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append(activityName);
    _builder.append("::~");
    _builder.append(activityName);
    _builder.append("() { RAD_TRACE(GetLogger()); }");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("void ");
    _builder.append(activityName);
    _builder.append("::Run() {");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("/*");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Inside the thread we use dedicated logger to be able");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* to enable/disable logs independently from the main thread.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("RAD_TRACE(m_logger);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("using namespace std::chrono_literals;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("while (IsStopRequested() == false) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("LOG4CPLUS_DEBUG(m_logger, \"");
    _builder.append(activityName, "        ");
    _builder.append(" is working... \");");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("std::this_thread::sleep_for(1s);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
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
