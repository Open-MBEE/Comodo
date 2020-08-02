package comodo2.templates.elt;

import com.google.common.base.Objects;
import comodo2.engine.Config;
import comodo2.templates.elt.cpp.MalAsync;
import comodo2.templates.elt.cpp.RadAction;
import comodo2.templates.elt.cpp.RadActionMgr;
import comodo2.templates.elt.cpp.RadActionsStd;
import comodo2.templates.elt.cpp.RadActivity;
import comodo2.templates.elt.cpp.RadConfig;
import comodo2.templates.elt.cpp.RadDataContext;
import comodo2.templates.elt.cpp.RadDbInterface;
import comodo2.templates.elt.cpp.RadLogger;
import comodo2.templates.elt.cpp.RadMain;
import comodo2.templates.elt.txt.RadLog;
import comodo2.templates.elt.waf.MalWscript;
import comodo2.templates.elt.waf.RadWscript;
import comodo2.templates.elt.xml.Mal;
import comodo2.templates.elt.yaml.RadCfg;
import comodo2.templates.elt.yaml.RadEv;
import comodo2.templates.scxml.Scxml;
import javax.inject.Inject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Elt implements IGenerator {
	@Inject
	private Scxml mScxmlTemplate;

	@Inject
	private RadCfg mRadCfgTemplate;

	@Inject
	private RadEv mRadEvTemplate;

	@Inject
	private RadLog mRadLogTemplate;

	@Inject
	private RadActivity mRadActivityTemplate;

	@Inject
	private RadAction mRadActionTemplate;

	@Inject
	private RadActionsStd mRadActionsStdTemplate;

	@Inject
	private RadActionMgr mRadActionMgrTemplate;

	@Inject
	private RadConfig mRadConfigTemplate;

	@Inject
	private RadDataContext mRadDataContextTemplate;

	@Inject
	private RadDbInterface mRadDbInterfaceTemplate;

	@Inject
	private RadLogger mRadLoggerTemplate;

	@Inject
	private RadMain mRadMainTemplate;

	@Inject
	private RadWscript mRadWscriptTemplate;

	@Inject
	private Mal mMalTemplate;

	@Inject
	private MalAsync mMalAsyncTemplate;

	@Inject
	private MalWscript mMalWscriptTemplate;

	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		if ((Objects.equal(Config.getInstance().getTargetPlatform(), Config.TARGET_PLATFORM_ELT_RAD) || 
				Objects.equal(Config.getInstance().getTargetPlatform(), Config.TARGET_PLATFORM_ELT_MAL))) {
			mMalTemplate.doGenerate(input, fsa);
		}
		if (Objects.equal(Config.getInstance().getTargetPlatform(), Config.TARGET_PLATFORM_ELT_RAD)) {
			mScxmlTemplate.doGenerate(input, fsa);
			mRadEvTemplate.doGenerate(input, fsa);
			mMalAsyncTemplate.doGenerate(input, fsa);
			if ((Config.getInstance().isGenerationModeAll() || Config.getInstance().isGenerationModeUpdate())) {
				mRadActivityTemplate.doGenerate(input, fsa);
				mRadActionTemplate.doGenerate(input, fsa);
				mRadActionMgrTemplate.doGenerate(input, fsa);
				if (Config.getInstance().isGenerationModeAll()) {
					mMalWscriptTemplate.doGenerate(input, fsa);
					mRadWscriptTemplate.doGenerate(input, fsa);
					mRadCfgTemplate.doGenerate(input, fsa);
					mRadLogTemplate.doGenerate(input, fsa);
					mRadLoggerTemplate.doGenerate(input, fsa);
					mRadConfigTemplate.doGenerate(input, fsa);
					mRadDataContextTemplate.doGenerate(input, fsa);
					mRadDbInterfaceTemplate.doGenerate(input, fsa);
					if (Config.getInstance().hasTargetPlatformCfgOption(Config.ELT_RAD_OPT_NOACTIONSTD) == false) {
						mRadActionsStdTemplate.doGenerate(input, fsa);
					}
					mRadMainTemplate.doGenerate(input, fsa);
				}
			}
		}
	}
}
