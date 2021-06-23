package comodo2.templates.qf;

import comodo2.engine.Config;
import comodo2.templates.qf.qm.Qm;
import comodo2.templates.qf.qm.QmImplFiles;

import javax.inject.Inject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Qf implements IGenerator {

    @Inject
	private Qm mQmTemplate;
    
	@Inject
	private QmImplFiles mQmImplTemplate;

    @Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		if (Config.getInstance().getTargetPlatform().contentEquals(Config.TARGET_PLATFORM_QF_QM)) {
			mQmTemplate.doGenerate(input, fsa);
			mQmImplTemplate.doGenerate(input, fsa);
		}
	}


}
