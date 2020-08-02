package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.queries.QClass;
import comodo2.queries.QInterface;
import comodo2.queries.QSignal;
import comodo2.templates.elt.cpp.Types;
import comodo2.utils.FilesHelper;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class MalAsync implements IGenerator {
  @Inject
  @Extension
  private QClass _qClass;
  
  @Inject
  @Extension
  private QSignal _qSignal;
  
  @Inject
  @Extension
  private QInterface _qInterface;
  
  @Inject
  @Extension
  private Types _types;
  
  @Inject
  @Extension
  private FilesHelper _filesHelper;
  
  /**
   * Transform UML Interfaces realized by a <<cmodComponent>> Class
   * into a RAD implementation of CII/MAL Asynchronous interface.
   */
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
    for (final org.eclipse.uml2.uml.Class e : _filter) {
      boolean _isToBeGenerated = this._qClass.isToBeGenerated(e);
      if (_isToBeGenerated) {
        EList<Interface> _allRealizedInterfaces = e.allRealizedInterfaces();
        for (final Interface i : _allRealizedInterfaces) {
          boolean _hasRequests = this._qInterface.hasRequests(i);
          if (_hasRequests) {
            String _name = i.getName();
            this._filesHelper.makeBackup(this._filesHelper.toAbsolutePath(this._filesHelper.toHppFilePath((_name + "Impl"))));
            String _name_1 = i.getName();
            fsa.generateFile(this._filesHelper.toHppFilePath((_name_1 + "Impl")), this.generate(e, i));
          }
        }
      }
    }
  }
  
  public CharSequence generate(final org.eclipse.uml2.uml.Class c, final Interface i) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _printHeader = this.printHeader(this._qClass.getContainerPackageName(c), i.getName(), this._qInterface.getContainerPackage(i).getName());
    _builder.append(_printHeader);
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    CharSequence _exploreSignals = this.exploreSignals(this.getAllSignals(i), this._qInterface.getContainerPackage(i).getName());
    _builder.append(_exploreSignals, "    ");
    _builder.newLineIfNotEmpty();
    CharSequence _printFooter = this.printFooter(this._qClass.getContainerPackageName(c), i.getName());
    _builder.append(_printFooter);
    _builder.append("\t    ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * This function finds all signals of the class's realized Interfaces.
   */
  public HashSet<Signal> getAllSignals(final Interface i) {
    HashSet<Signal> allSignals = new HashSet<Signal>();
    final Function1<Reception, String> _function = (Reception e) -> {
      return e.getName();
    };
    List<Reception> _sortBy = IterableExtensions.<Reception, String>sortBy(i.getOwnedReceptions(), _function);
    for (final Reception r : _sortBy) {
      allSignals.add(r.getSignal());
    }
    return allSignals;
  }
  
  /**
   * For each signal, generates the realization of MAL/CII interface methods.
   */
  public CharSequence exploreSignals(final HashSet<Signal> allSignals, final String ifModName) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Signal s : allSignals) {
        {
          boolean _hasReply = this._qSignal.hasReply(s);
          if (_hasReply) {
            {
              boolean _hasParam = this._qSignal.hasParam(s);
              if (_hasParam) {
                {
                  boolean _isFirstParamTypePrimitive = this._qSignal.isFirstParamTypePrimitive(s);
                  if (_isFirstParamTypePrimitive) {
                    _builder.append("virtual elt::mal::future<");
                    String _qualifiedReplyTypeName = this.getQualifiedReplyTypeName(s, ifModName);
                    _builder.append(_qualifiedReplyTypeName);
                    _builder.append("> ");
                    String _nameWithoutPrefix = this._qSignal.nameWithoutPrefix(s);
                    _builder.append(_nameWithoutPrefix);
                    _builder.append("(");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("const ");
                    String _typeName = this._types.typeName(this._qSignal.getFirstParamType(s));
                    _builder.append(_typeName, "    ");
                    _builder.append("& mal_param) override {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("RAD_TRACE(GetLogger());");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("auto ev = std::make_shared<");
                    String _nameWithNamespace = this._qSignal.nameWithNamespace(s);
                    _builder.append(_nameWithNamespace, "\t");
                    _builder.append(">(mal_param);");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("virtual elt::mal::future<");
                    String _qualifiedReplyTypeName_1 = this.getQualifiedReplyTypeName(s, ifModName);
                    _builder.append(_qualifiedReplyTypeName_1);
                    _builder.append("> ");
                    String _nameWithoutPrefix_1 = this._qSignal.nameWithoutPrefix(s);
                    _builder.append(_nameWithoutPrefix_1);
                    _builder.append("(");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("const std::shared_ptr<");
                    _builder.append(ifModName, "    ");
                    _builder.append("::");
                    String _typeName_1 = this._types.typeName(this._qSignal.getFirstParamType(s));
                    _builder.append(_typeName_1, "    ");
                    _builder.append(">& mal_param) override {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("RAD_TRACE(GetLogger());");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("auto ev = std::make_shared<");
                    String _nameWithNamespace_1 = this._qSignal.nameWithNamespace(s);
                    _builder.append(_nameWithNamespace_1, "\t");
                    _builder.append(">(mal_param->clone());\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("virtual elt::mal::future<");
                String _qualifiedReplyTypeName_2 = this.getQualifiedReplyTypeName(s, ifModName);
                _builder.append(_qualifiedReplyTypeName_2);
                _builder.append("> ");
                String _nameWithoutPrefix_2 = this._qSignal.nameWithoutPrefix(s);
                _builder.append(_nameWithoutPrefix_2);
                _builder.append("() override {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("RAD_TRACE(GetLogger());");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("auto ev = std::make_shared<");
                String _nameWithNamespace_2 = this._qSignal.nameWithNamespace(s);
                _builder.append(_nameWithNamespace_2, "\t");
                _builder.append(">();");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("m_sm.PostEvent(ev);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return ev->GetPayload().GetReplyFuture();");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
          }
        }
      }
    }
    return _builder;
  }
  
  public String getQualifiedReplyTypeName(final Signal s, final String prefix) {
    boolean _isReplyTypePrimitive = this._qSignal.isReplyTypePrimitive(s);
    if (_isReplyTypePrimitive) {
      return this._types.typeName(this._qSignal.getReplyType(s));
    } else {
      String _typeName = this._types.typeName(this._qSignal.getReplyType(s));
      String _plus = ((("std::shared_ptr<" + prefix) + "::") + _typeName);
      return (_plus + ">");
    }
  }
  
  public CharSequence printHeader(final String appModuleName, final String ifName, final String ifModuleName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @file");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @ingroup ");
    _builder.append(appModuleName, " ");
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
    _builder.append("* @brief  ");
    _builder.append(ifName, " ");
    _builder.append(" Interface implementation header file.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("#ifndef ");
    String _upperCase = appModuleName.toUpperCase();
    _builder.append(_upperCase);
    _builder.append("_");
    String _upperCase_1 = ifName.toUpperCase();
    _builder.append(_upperCase_1);
    _builder.append("_IMPL_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define ");
    String _upperCase_2 = appModuleName.toUpperCase();
    _builder.append(_upperCase_2);
    _builder.append("_");
    String _upperCase_3 = ifName.toUpperCase();
    _builder.append(_upperCase_3);
    _builder.append("_IMPL_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include \"");
    String _fileName = this._filesHelper.toFileName(ifName);
    _builder.append(_fileName);
    _builder.append(".rad.hpp\"\t");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <");
    _builder.append(appModuleName);
    _builder.append("/logger.hpp>");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <rad/exceptions.hpp>");
    _builder.newLine();
    _builder.append("#include <rad/smAdapter.hpp>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("namespace ");
    _builder.append(appModuleName);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("class ");
    _builder.append(ifName);
    _builder.append("Impl : public ");
    _builder.append(ifModuleName);
    _builder.append("::Async");
    _builder.append(ifName);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("public:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("explicit ");
    _builder.append(ifName, "    ");
    _builder.append("Impl(rad::SMAdapter& sm) : m_sm(sm) { RAD_TRACE(GetLogger()); }");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("virtual ~");
    _builder.append(ifName, "    ");
    _builder.append("Impl() { RAD_TRACE(GetLogger()); }");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printFooter(final String appModuleName, final String ifName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("private:");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("rad::SMAdapter& m_sm;");
    _builder.newLine();
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}  // namespace ");
    _builder.append(appModuleName);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif  // ");
    String _upperCase = appModuleName.toUpperCase();
    _builder.append(_upperCase);
    _builder.append("_");
    String _upperCase_1 = ifName.toUpperCase();
    _builder.append(_upperCase_1);
    _builder.append("_IMPL_HPP_");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
