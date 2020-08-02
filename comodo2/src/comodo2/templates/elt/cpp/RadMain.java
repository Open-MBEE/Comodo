package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.queries.QInterface;
import comodo2.utils.FilesHelper;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class RadMain implements IGenerator {
  @Inject
  @Extension
  private QClass _qClass;
  
  @Inject
  @Extension
  private QInterface _qInterface;
  
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
        this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toCppFilePath("main")));
        fsa.generateFile(this._filesHelper.toCppFilePath("main"), this.generateSource(Config.getInstance().getCurrentModule(), "main", e));
      }
    }
  }
  
  /**
   * main.cpp
   */
  public CharSequence generateSource(final String moduleName, final String fileName, final org.eclipse.uml2.uml.Class c) {
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
    _builder.append(fileName, " ");
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
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/dataContext.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/dbInterface.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(moduleName);
    _builder.append("/actionMgr.hpp>");
    _builder.newLineIfNotEmpty();
    {
      EList<Interface> _allRealizedInterfaces = c.allRealizedInterfaces();
      for(final Interface i : _allRealizedInterfaces) {
        {
          boolean _hasRequests = this._qInterface.hasRequests(i);
          if (_hasRequests) {
            _builder.append("#include <");
            _builder.append(moduleName);
            _builder.append("/");
            String _fileName = this._filesHelper.toFileName(i.getName());
            _builder.append(_fileName);
            _builder.append("Impl.hpp>");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("#include <events.rad.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <rad/mal/replier.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/mal/utils.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/exceptions.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/dbAdapterRedis.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/smAdapter.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <scxml4cpp/Context.h>");
    _builder.newLine();
    _builder.append("#include <scxml4cpp/EventQueue.h>");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <boost/asio.hpp>");
    _builder.newLine();
    _builder.append("#include <boost/exception/diagnostic_information.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <google/protobuf/stubs/common.h>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <memory>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Application main.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param[in] argc Number of command line options.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param[in] argv Command line options.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("int main(int argc, char *argv[]) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("rad::LogInitialize();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("LOG4CPLUS_INFO(");
    _builder.append(moduleName, "\t");
    _builder.append("::GetLogger(), \"Application ");
    _builder.append(moduleName, "\t");
    _builder.append(" started.\");");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("try {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/*");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*  Load CII/MAL middleware here because it resets");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*  the log4cplus configuration!");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("rad::cii::LoadMiddlewares({\"zpb\"});");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/* Read only configuration */");
    _builder.newLine();
    _builder.append("        ");
    _builder.append(moduleName, "        ");
    _builder.append("::Config config;");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("if (config.ParseOptions(argc, argv) == false) {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("// request for help");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return EXIT_SUCCESS;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("config.LoadConfig();");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("rad::LogConfigure(rad::Helper::FindFile(config.GetLogProperties()));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/*");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* LAN 2020-07-09 EICSSW-717");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* Create CII/MAL replier as soon as possible to avoid problems when");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* an exceptions is thrown from and Action/Guard.");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("rad::cii::Replier mal_replier(elt::mal::Uri(config.GetMsgReplierEndpoint()));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/* Runtime DB */");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("rad::DbAdapterRedis redis_db;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/* Runtime data context */");
    _builder.newLine();
    _builder.append("        ");
    _builder.append(moduleName, "        ");
    _builder.append("::DataContext data_ctx(config, redis_db);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/*");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* Create event loop");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// event loop");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("boost::asio::io_service io_service;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/*");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* State Machine related objects");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// SM event queue and context");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("scxml4cpp::EventQueue external_events;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("scxml4cpp::Context state_machine_ctx;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// State Machine facade");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("rad::SMAdapter state_machine(io_service,");
    _builder.newLine();
    _builder.append("                                     ");
    _builder.append("&state_machine_ctx,");
    _builder.newLine();
    _builder.append("                                     ");
    _builder.append("external_events);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// actions and activities");
    _builder.newLine();
    _builder.append("        ");
    _builder.append(moduleName, "        ");
    _builder.append("::ActionMgr action_mgr;");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("action_mgr.CreateActions(io_service, state_machine, data_ctx);");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("action_mgr.CreateActivities(state_machine, data_ctx);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// Load SM model");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("state_machine.Load(config.GetSmScxmlFilename(), &action_mgr.GetActions(),");
    _builder.newLine();
    _builder.append("                ");
    _builder.append("&action_mgr.GetActivities());");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// Register handlers to reject events");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("//state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Init>();");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("//state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Enable>();");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("//state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Disable>();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// Register publisher to export state information");
    _builder.newLine();
    {
      boolean _generateFullyQualifiedStateNames = Config.getInstance().generateFullyQualifiedStateNames();
      if (_generateFullyQualifiedStateNames) {
        _builder.append("        ");
        _builder.append("state_machine.SetStatusRepresentation(true);");
        _builder.newLine();
      } else {
        _builder.append("        ");
        _builder.append("state_machine.SetStatusRepresentation(false);");
        _builder.newLine();
      }
    }
    _builder.append("        ");
    _builder.append("using std::placeholders::_1;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("state_machine.SetStatusPublisher(std::bind(");
    _builder.newLine();
    _builder.append("                ");
    _builder.append("&");
    _builder.append(moduleName, "                ");
    _builder.append("::DbInterface::SetControlState,");
    _builder.newLineIfNotEmpty();
    _builder.append("                ");
    _builder.append("&data_ctx.GetDbInterface(), _1));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/*");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* Register CII/MAL interfaces.");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*/");
    _builder.newLine();
    {
      EList<Interface> _allRealizedInterfaces_1 = c.allRealizedInterfaces();
      for(final Interface i_1 : _allRealizedInterfaces_1) {
        {
          boolean _hasRequests_1 = this._qInterface.hasRequests(i_1);
          if (_hasRequests_1) {
            _builder.append("\t\t");
            _builder.append("mal_replier.RegisterService<");
            String _name = this._qInterface.getContainerPackage(i_1).getName();
            _builder.append(_name, "\t\t");
            _builder.append("::Async");
            String _name_1 = i_1.getName();
            _builder.append(_name_1, "\t\t");
            _builder.append(">(\"");
            String _name_2 = i_1.getName();
            _builder.append(_name_2, "\t\t");
            _builder.append("\",");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("    ");
            _builder.append("std::make_shared<");
            _builder.append(moduleName, "\t\t    ");
            _builder.append("::");
            String _name_3 = i_1.getName();
            _builder.append(_name_3, "\t\t    ");
            _builder.append("Impl>(state_machine));");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/*");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* Start event loop");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("state_machine.Start();");
    _builder.newLine();
    _builder.append("        ");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("io_service.run();");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("state_machine.Stop();");
    _builder.newLine();
    _builder.append("        ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("} catch (rad::Exception& e) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("LOG4CPLUS_ERROR(");
    _builder.append(moduleName, "        ");
    _builder.append("::GetLogger(), e.what());");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("return EXIT_FAILURE;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("} catch (...) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("LOG4CPLUS_ERROR(");
    _builder.append(moduleName, "        ");
    _builder.append("::GetLogger(), boost::current_exception_diagnostic_information());");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("return EXIT_FAILURE;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// to avoid valgrind warnings on potential memory loss");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("google::protobuf::ShutdownProtobufLibrary();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("LOG4CPLUS_INFO(");
    _builder.append(moduleName, "    ");
    _builder.append("::GetLogger(), \"Application ");
    _builder.append(moduleName, "    ");
    _builder.append(" terminated.\");");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("return EXIT_SUCCESS;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
