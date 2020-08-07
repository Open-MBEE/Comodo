package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.queries.QClass;
import comodo2.queries.QInterface;
import comodo2.queries.QSignal;
import comodo2.utils.FilesHelper;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

public class MalAsync implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	private QClass mQClass;

	@Inject
	private QSignal mQSignal;

	@Inject
	private QInterface mQInterface;

	@Inject
	private Types mTypes;

	@Inject
	private FilesHelper mFilesHelper;

	/**
	 * Transform UML Interfaces realized by a <<cmodComponent>> Class
	 * into a RAD implementation of CII/MAL Asynchronous interface.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if (mQClass.isToBeGenerated(e)) {
				for (final Interface i : e.allRealizedInterfaces()) {
					if (mQInterface.hasRequests(i)) {
						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath((i.getName() + "Impl"))));
						fsa.generateFile(mFilesHelper.toHppFilePath((i.getName() + "Impl")), this.generate(e, i));
					}
				}
			}
		}
	}

	public CharSequence generate(final org.eclipse.uml2.uml.Class c, final Interface i) {
		String ifModuleName = mQInterface.getContainerPackage(i).getName();
		String ifName = i.getName();
		String moduleName = mQClass.getContainerPackageName(c);
		String className = ifName + "Impl";
		String parentClass = ifModuleName + "::Async" + ifName;			
		String radEvInclude = mFilesHelper.toFileName(ifName) + ".rad.hpp";
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppMalAsync.stg");
			ST st = g.getInstanceOf("MalAsyncHeader");			
			st.add("moduleName", moduleName);
			st.add("moduleNameUpperCase", moduleName.toUpperCase());
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classNameUpperCase", className.toUpperCase());	
			st.add("radEvInclude" , radEvInclude);
			st.add("parentClass" , parentClass);			
			st.add("methodsImpl" , exploreSignals(getAllSignals(i), mQInterface.getContainerPackage(i).getName()));			
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating header file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";
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
		String str = "";
		for (final Signal s : allSignals) {
			if (mQSignal.hasReply(s)) {
				str += "\n";
				if (mQSignal.hasParam(s)) {
					str += "virtual elt::mal::future<" + getQualifiedReplyTypeName(s, ifModName) + "> " + mQSignal.nameWithoutPrefix(s) + "(\n";
					if (mQSignal.isFirstParamTypePrimitive(s)) {
						str += "    const " + mTypes.typeName(mQSignal.getFirstParamType(s)) + "& mal_param) override {\n";
						str += "    RAD_TRACE(GetLogger());\n";
						str += "    auto ev = std::make_shared<" + mQSignal.nameWithNamespace(s) + ">(mal_param);\n";
					} else {
						str += "    const std::shared_ptr<" + ifModName + "::" + mTypes.typeName(mQSignal.getFirstParamType(s)) + ">& mal_param) override {\n";
						str += "    RAD_TRACE(GetLogger());\n";
						str += "    auto ev = std::make_shared<" + mQSignal.nameWithNamespace(s) + ">(mal_param->clone());\n";
					}
				} else {
					str += "virtual elt::mal::future<" + getQualifiedReplyTypeName(s, ifModName) + "> " + mQSignal.nameWithoutPrefix(s) + "() override {\n";
					str += "    RAD_TRACE(GetLogger());\n";
					str += "    auto ev = std::make_shared<" + mQSignal.nameWithNamespace(s) + ">();\n";
				}
				str += "    m_sm.PostEvent(ev);\n";
				str += "    return ev->GetPayload().GetReplyFuture();\n";
				str += "}\n";
			}
		}
		return str;
	}

	public String getQualifiedReplyTypeName(final Signal s, final String prefix) {
		if (mQSignal.isReplyTypePrimitive(s)) {
			return mTypes.typeName(mQSignal.getReplyType(s));
		} else {
			return "std::shared_ptr<" + prefix + "::" + mTypes.typeName(mQSignal.getReplyType(s)) + ">";
		}
	}
}
