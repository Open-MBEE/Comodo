package comodo2.templates;

import com.google.common.base.Objects;
import comodo2.engine.Config;
import comodo2.templates.elt.Elt;
import comodo2.templates.scxml.Scxml;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Root implements IGenerator {
	@Inject
	private Scxml mScxmlTemplate;

	@Inject
	private Elt mEltTemplate;

	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		if (Config.getInstance().getModules().length > 0) {
			for (final String m : Config.getInstance().getModules()) {
				Config.getInstance().setCurrentModule(m);
				generate(input, fsa);
			}
		} else {
			generate(input, fsa);
		}
	}

	public void generate(final Resource input, final IFileSystemAccess fsa) {
		if (Config.getInstance().isModelConfigured(input.getURI().toFileString())) {
			long startTime = System.nanoTime();	
			if (Objects.equal(Config.getInstance().getTargetPlatform(), Config.TARGET_PLATFORM_SCXML)) {
				mScxmlTemplate.doGenerate(input, fsa);
			} else {
				if ((Objects.equal(Config.getInstance().getTargetPlatform(), Config.TARGET_PLATFORM_ELT_RAD) || 
						Objects.equal(Config.getInstance().getTargetPlatform(), Config.TARGET_PLATFORM_ELT_MAL))) {
					this.mEltTemplate.doGenerate(input, fsa);
				} else {
					mLogger.error("Unsupported target: <" + Config.getInstance().getTargetPlatform() + "> for module <" + Config.getInstance().getCurrentModule() + ">");
				}
			}
			mLogger.debug("Processed module <" + Config.getInstance().getCurrentModule() + "> from resource URI <" + input.getURI().toString() + "> (" + 
					(System.nanoTime() - startTime)/1e9 + "s)");
		}
	}
}
