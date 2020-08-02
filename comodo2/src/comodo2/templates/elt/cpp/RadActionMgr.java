package comodo2.templates.elt.cpp;

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
public class RadActionMgr implements IGenerator {
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
   * into a RAD ActionMgr class.
   */
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
    for (final org.eclipse.uml2.uml.Class e : _filter) {
      if ((this._qClass.isToBeGenerated(e) && this._qClass.hasStateMachines(e))) {
        this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath("actionMgr")));
        fsa.generateFile(this._filesHelper.toHppFilePath("actionMgr"), this.generateHeader(Config.getInstance().getCurrentModule(), "ActionMgr"));
        TreeSet<String> activityNames = new TreeSet<String>();
        Iterable<StateMachine> _stateMachines = this._qClass.getStateMachines(e);
        for (final StateMachine sm : _stateMachines) {
          TreeSet<String> _allActivityNames = this._qStateMachine.getAllActivityNames(sm);
          Iterables.<String>addAll(activityNames, _allActivityNames);
        }
        TreeSet<String> actionNames = new TreeSet<String>();
        Iterable<StateMachine> _stateMachines_1 = this._qClass.getStateMachines(e);
        for (final StateMachine sm_1 : _stateMachines_1) {
          TreeSet<String> _allActionNames = this._qStateMachine.getAllActionNames(sm_1);
          Iterables.<String>addAll(actionNames, _allActionNames);
        }
        TreeSet<String> guardNames = new TreeSet<String>();
        Iterable<StateMachine> _stateMachines_2 = this._qClass.getStateMachines(e);
        for (final StateMachine sm_2 : _stateMachines_2) {
          TreeSet<String> _allGuardNames = this._qStateMachine.getAllGuardNames(sm_2);
          Iterables.<String>addAll(guardNames, _allGuardNames);
        }
        this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath("actionMgr")));
        fsa.generateFile(this._filesHelper.toCppFilePath("actionMgr"), this.generateSource(Config.getInstance().getCurrentModule(), "ActionMgr", activityNames, actionNames, guardNames));
      }
    }
  }
  
  /**
   * ActionMgr.hpp
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
    _builder.append(" class header file.");
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
    _builder.append("#include <rad/actionMgr.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/smAdapter.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <boost/asio.hpp>");
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
    _builder.append("* This class is responsible for the life-cycle management of");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* actions and activities.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("class ActionMgr : public rad::ActionMgr {");
    _builder.newLine();
    _builder.append("   ");
    _builder.append("public:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Default constructor.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("ActionMgr();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Default destructor.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("virtual ~ActionMgr();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Method to instantiate the action objects.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] ios Event loop.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] sm SM adapter used to inject internal events.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] the_data Data shared within the application among actions and activities.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void CreateActions(boost::asio::io_service& ios, rad::SMAdapter& sm, DataContext& the_data);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Method to instantiate activity objects.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] the_data Data shared within the application.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* @param[in] sm Reference to the State Machine adapter needed to trigger internal events.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void CreateActivities(rad::SMAdapter& sm, DataContext& the_data);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("ActionMgr(const ActionMgr&) = delete;             //! Disable copy constructor");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("ActionMgr& operator=(const ActionMgr&) = delete;  //! Disable assignment operator");
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
    String _upperCase_5 = className.toUpperCase();
    _builder.append(_upperCase_5);
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * ActionMgr.cpp
   */
  public CharSequence generateSource(final String moduleName, final String className, final TreeSet<String> activityNames, final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
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
    _builder.append(" class source file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/");
    String _fileName = this._filesHelper.toFileName(className);
    _builder.append(_fileName);
    _builder.append(".hpp>");
    _builder.newLineIfNotEmpty();
    {
      TreeSet<String> _mergeClassNames = this._actions.getMergeClassNames(this._actions.getClassNames(actionNames), this._actions.getClassNames(guardNames));
      for(final String name : _mergeClassNames) {
        _builder.append("#include <");
        _builder.append(moduleName);
        _builder.append("/");
        String _fileName_1 = this._filesHelper.toFileName(name);
        _builder.append(_fileName_1);
        _builder.append(".hpp>\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final String name_1 : activityNames) {
        _builder.append("#include <");
        _builder.append(moduleName);
        _builder.append("/");
        String _fileName_2 = this._filesHelper.toFileName(name_1);
        _builder.append(_fileName_2);
        _builder.append(".hpp>\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/dataContext.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/logger.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <rad/actionCallback.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/actionGroup.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/assert.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <functional>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("namespace ");
    String _lowerCase = moduleName.toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("ActionMgr::ActionMgr() { RAD_TRACE(GetLogger()); }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("ActionMgr::~ActionMgr() { RAD_TRACE(GetLogger()); }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void ActionMgr::CreateActions(boost::asio::io_service& ios, rad::SMAdapter& sm,");
    _builder.newLine();
    _builder.append("                              ");
    _builder.append("DataContext& the_data) {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("RAD_TRACE(GetLogger());");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("scxml4cpp::Action* the_action = nullptr;    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("using std::placeholders::_1;");
    _builder.newLine();
    {
      TreeSet<String> _mergeClassNames_1 = this._actions.getMergeClassNames(this._actions.getClassNames(actionNames), this._actions.getClassNames(guardNames));
      for(final String group : _mergeClassNames_1) {
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append(group, "\t");
        _builder.append("* my_");
        String _lowerCase_1 = group.toLowerCase();
        _builder.append(_lowerCase_1, "\t");
        _builder.append(" = new ");
        _builder.append(group, "\t");
        _builder.append("(ios, sm, the_data);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("if (my_");
        String _lowerCase_2 = group.toLowerCase();
        _builder.append(_lowerCase_2, "\t");
        _builder.append(" == nullptr) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("LOG4CPLUS_ERROR(GetLogger(), \"Cannot create ");
        _builder.append(group, "\t\t");
        _builder.append(" object.\");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("// @TODO throw exception");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("AddActionGroup(my_");
        String _lowerCase_3 = group.toLowerCase();
        _builder.append(_lowerCase_3, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        {
          TreeSet<String> _methodNames = this._actions.getMethodNames(actionNames, group);
          for(final String method : _methodNames) {
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("the_action = new rad::ActionCallback(\"");
            _builder.append(group, "\t");
            _builder.append(".");
            _builder.append(method, "\t");
            _builder.append("\",");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("                                     ");
            _builder.append("std::bind(&");
            _builder.append(group, "\t                                     ");
            _builder.append("::");
            _builder.append(method, "\t                                     ");
            _builder.append(", my_");
            String _lowerCase_4 = group.toLowerCase();
            _builder.append(_lowerCase_4, "\t                                     ");
            _builder.append(", _1));");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("AddAction(the_action);");
            _builder.newLine();
          }
        }
        {
          TreeSet<String> _methodNames_1 = this._actions.getMethodNames(guardNames, group);
          for(final String method_1 : _methodNames_1) {
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("the_action = new rad::GuardCallback(\"");
            _builder.append(group, "\t");
            _builder.append(".");
            _builder.append(method_1, "\t");
            _builder.append("\",");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("                                    ");
            _builder.append("std::bind(&");
            _builder.append(group, "\t                                    ");
            _builder.append("::");
            _builder.append(method_1, "\t                                    ");
            _builder.append(", my_");
            String _lowerCase_5 = group.toLowerCase();
            _builder.append(_lowerCase_5, "\t                                    ");
            _builder.append(", _1));");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("AddAction(the_action);");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("    ");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void ActionMgr::CreateActivities(rad::SMAdapter& sm, DataContext& the_data) {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("RAD_TRACE(GetLogger());");
    _builder.newLine();
    {
      int _size = activityNames.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("\t");
        _builder.append("scxml4cpp::Activity* the_activity = nullptr;");
        _builder.newLine();
        {
          for(final String name_2 : activityNames) {
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("the_activity = new ");
            _builder.append(name_2, "\t");
            _builder.append("(\"");
            _builder.append(name_2, "\t");
            _builder.append("\", sm, the_data);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("AddActivity(the_activity);");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}  // namespace ");
    String _lowerCase_6 = moduleName.toLowerCase();
    _builder.append(_lowerCase_6);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
