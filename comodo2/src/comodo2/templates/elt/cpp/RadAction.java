package comodo2.templates.elt.cpp;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.queries.QStateMachine;
import comodo2.utils.Actions;
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
public class RadAction implements IGenerator {
  @Inject
  @Extension
  private QClass _qClass;
  
  @Inject
  @Extension
  private QStateMachine _qStateMachine;
  
  @Inject
  @Extension
  private FilesHelper _filesHelper;
  
  @Inject
  @Extension
  private Actions _actions;
  
  /**
   * Transform UML State Machine associated to a class (classifier behavior)
   * into RAD actions classes.
   */
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
    for (final org.eclipse.uml2.uml.Class e : _filter) {
      if ((this._qClass.isToBeGenerated(e) && this._qClass.hasStateMachines(e))) {
        TreeSet<String> actionNames = new TreeSet<String>();
        Iterable<StateMachine> _stateMachines = this._qClass.getStateMachines(e);
        for (final StateMachine sm : _stateMachines) {
          TreeSet<String> _allActionNames = this._qStateMachine.getAllActionNames(sm);
          Iterables.<String>addAll(actionNames, _allActionNames);
        }
        TreeSet<String> guardNames = new TreeSet<String>();
        Iterable<StateMachine> _stateMachines_1 = this._qClass.getStateMachines(e);
        for (final StateMachine sm_1 : _stateMachines_1) {
          TreeSet<String> _allGuardNames = this._qStateMachine.getAllGuardNames(sm_1);
          Iterables.<String>addAll(guardNames, _allGuardNames);
        }
        TreeSet<String> _mergeClassNames = this._actions.getMergeClassNames(this._actions.getClassNames(actionNames), this._actions.getClassNames(guardNames));
        for (final String c : _mergeClassNames) {
          if ((((Config.getInstance().hasTargetPlatformCfgOption(Config.ELT_RAD_OPT_NOACTIONSTD)).booleanValue() == true) || (((Config.getInstance().hasTargetPlatformCfgOption(Config.ELT_RAD_OPT_NOACTIONSTD)).booleanValue() == false) && (!Objects.equal(c, "ActionsStd"))))) {
            boolean _skipFile = this._filesHelper.skipFile(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath(c)));
            boolean _equals = (_skipFile == false);
            if (_equals) {
              this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath(c)));
              fsa.generateFile(this._filesHelper.toHppFilePath(c), this.generateHeader(Config.getInstance().getCurrentModule(), c, actionNames, guardNames));
            }
            boolean _skipFile_1 = this._filesHelper.skipFile(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath(c)));
            boolean _equals_1 = (_skipFile_1 == false);
            if (_equals_1) {
              this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath(c)));
              fsa.generateFile(this._filesHelper.toCppFilePath(c), this.generateSource(Config.getInstance().getCurrentModule(), c, actionNames, guardNames));
            }
          }
        }
      }
    }
  }
  
  /**
   * Actions.hpp
   */
  public CharSequence generateHeader(final String moduleName, final String groupName, final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
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
    _builder.append(groupName, " ");
    _builder.append(" class header file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("#ifndef ");
    String _upperCase = moduleName.toUpperCase();
    _builder.append(_upperCase);
    _builder.append("_");
    String _upperCase_1 = groupName.toUpperCase();
    _builder.append(_upperCase_1);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define ");
    String _upperCase_2 = moduleName.toUpperCase();
    _builder.append(_upperCase_2);
    _builder.append("_");
    String _upperCase_3 = groupName.toUpperCase();
    _builder.append(_upperCase_3);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <rad/actionGroup.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/smAdapter.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <string>");
    _builder.newLine();
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
    _builder.append("* This class implements the action methods related");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* to ");
    _builder.append(groupName, " ");
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("class ");
    _builder.append(groupName);
    _builder.append(" : public rad::ActionGroup {");
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
    _builder.append("* @param[in] ios Reference to the event loop.");
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
    _builder.append(groupName, "    ");
    _builder.append("(boost::asio::io_service& ios, rad::SMAdapter& sm, DataContext& data);    ");
    _builder.newLineIfNotEmpty();
    {
      TreeSet<String> _methodNames = this._actions.getMethodNames(actionNames, groupName);
      for(final String method : _methodNames) {
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Method implementing the ");
        _builder.append(method, "     ");
        _builder.append(" action.");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("* @param[in] c Context containing the last event received by the State Machine.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("void ");
        _builder.append(method, "    ");
        _builder.append("(scxml4cpp::Context* c);");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      TreeSet<String> _methodNames_1 = this._actions.getMethodNames(guardNames, groupName);
      for(final String method_1 : _methodNames_1) {
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Method implementing the ");
        _builder.append(method_1, "     ");
        _builder.append(" guard.");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("* @param[in] c Context containing the last event received by the State Machine.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @return true if the guard is satisfied, false otherwise.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("bool ");
        _builder.append(method_1, "    ");
        _builder.append("(scxml4cpp::Context* c);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append(groupName, "    ");
    _builder.append("(const ");
    _builder.append(groupName, "    ");
    _builder.append("&) = delete;             //! Disable copy constructor");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append(groupName, "    ");
    _builder.append("& operator=(const ");
    _builder.append(groupName, "    ");
    _builder.append("&) = delete;  //! Disable assignment operator");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("   ");
    _builder.append("private:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("boost::asio::io_service& m_io_service;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("rad::SMAdapter& m_sm;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("DataContext& m_data; ");
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
    String _upperCase_5 = groupName.toUpperCase();
    _builder.append(_upperCase_5);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * Actions.cpp
   */
  public CharSequence generateSource(final String moduleName, final String groupName, final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
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
    _builder.append(groupName, " ");
    _builder.append(" class source file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/");
    String _fileName = this._filesHelper.toFileName(groupName);
    _builder.append(_fileName);
    _builder.append(".hpp>");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/dataContext.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/logger.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.newLine();
    _builder.append("namespace ");
    String _lowerCase = moduleName.toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(groupName);
    _builder.append("::");
    _builder.append(groupName);
    _builder.append("(boost::asio::io_service& ios, rad::SMAdapter& sm, DataContext& data)");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append(": rad::ActionGroup(\"");
    _builder.append(groupName, "    ");
    _builder.append("\"),");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("m_io_service(ios),");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("m_sm(sm),");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("m_data(data) {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("RAD_TRACE(GetLogger());");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    {
      TreeSet<String> _methodNames = this._actions.getMethodNames(actionNames, groupName);
      for(final String method : _methodNames) {
        _builder.newLine();
        _builder.append("void ");
        _builder.append(groupName);
        _builder.append("::");
        _builder.append(method);
        _builder.append("(scxml4cpp::Context* c) {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("RAD_TRACE(GetLogger());");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      TreeSet<String> _methodNames_1 = this._actions.getMethodNames(guardNames, groupName);
      for(final String method_1 : _methodNames_1) {
        _builder.newLine();
        _builder.append("bool ");
        _builder.append(groupName);
        _builder.append("::");
        _builder.append(method_1);
        _builder.append("(scxml4cpp::Context* c) {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("RAD_TRACE(GetLogger());");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("}  // namespace ");
    String _lowerCase_1 = moduleName.toLowerCase();
    _builder.append(_lowerCase_1);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
