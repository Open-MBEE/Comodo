package comodo2.templates.qpc;

import comodo2.engine.Config;
import comodo2.templates.qpc.impl.QpcHeaders;
import comodo2.templates.qpc.impl.QpcImplFiles;
import comodo2.templates.qpc.qm.Qm;

import javax.inject.Inject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Qpc implements IGenerator {

    @Inject
	private Qm mQm;
    
	@Inject
	private QpcImplFiles mQpcImplFiles;

	@Inject
	private QpcHeaders mQpcHeaders;

    @Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		if (Config.getInstance().getTargetPlatform().contentEquals(Config.TARGET_PLATFORM_QPC_QM)) {
			mQm.doGenerate(input, fsa);
			mQpcImplFiles.doGenerate(input, fsa);
			mQpcHeaders.doGenerate(input, fsa);
		}
	}


}
