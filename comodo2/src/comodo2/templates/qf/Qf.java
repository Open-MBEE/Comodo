package comodo2.templates.qf;

import comodo2.engine.Config;
import comodo2.templates.qf.qm.Qm;
import comodo2.templates.qf.qm.QmImplFiles;
import comodo2.templates.qf.qm.QmHeaders;

import javax.inject.Inject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Qf implements IGenerator {

    @Inject
	private Qm mQm;
    
	@Inject
	private QmImplFiles mQmImplFiles;

	@Inject
	private QmHeaders mQmHeaders;

    @Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		if (Config.getInstance().getTargetPlatform().contentEquals(Config.TARGET_PLATFORM_QF_QM)) {
			mQm.doGenerate(input, fsa);
			mQmImplFiles.doGenerate(input, fsa);
			mQmHeaders.doGenerate(input, fsa);
		}
	}


}
