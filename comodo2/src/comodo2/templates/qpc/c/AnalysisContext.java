package comodo2.templates.qpc.c;

import javax.inject.Inject;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.utils.FilesHelper;

public class AnalysisContext implements IGenerator {
	
	@Inject
	private QClass mQClass;
	
	@Inject
	private FilesHelper mFilesHelper;

	/**
	 * Generates the header file for the State Machine source file.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		
		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof org.eclipse.uml2.uml.Interaction) {
				org.eclipse.uml2.uml.Interaction interaction = (org.eclipse.uml2.uml.Interaction)e; 
				if ((mQClass.isToBeGenerated(interaction))) {
					String seqDiagName = interaction.getName();
					String cmdoModule = Config.getInstance().getCurrentModule();

					mFilesHelper.makeBackup(mFilesHelper.toQmImplFilePath(mFilesHelper.toQmFilePath(seqDiagName + "_seq_diagram.yaml")));
					fsa.generateFile(mFilesHelper.toQmImplFilePath(seqDiagName + "_seq_diagram.yaml"), this.generateSequenceDiagramYAML(interaction, cmdoModule));
				}
			}
		}
	}

	public CharSequence generateSequenceDiagramYAML(Interaction interaction, String cmdoModule) {
		String seqDiagName = interaction.getName();

        STGroup stg = new STGroupFile("resources/qpc_tpl/AnalysisContext.stg");
		ST st_diag = stg.getInstanceOf("SequenceDiagramYAML");
		st_diag.add("cmdoModule", cmdoModule);
		st_diag.add("seqDiagName", seqDiagName);

		String sequence = "";
		String lifelines = "";
		int msg_id = 1;

		for (Lifeline l : interaction.getLifelines()){
			lifelines += " - <" + l.getRepresents().getType().getName() + ">\n";
		}

		for (Message msg : interaction.getMessages()){
			sequence += generateMessageYAML(stg, msg, msg_id);
			msg_id++;
		}

		st_diag.add("sequence", sequence);
		st_diag.add("lifelines", lifelines);

        return st_diag.render();
	}

	public String generateMessageYAML(STGroup stg, Message msg, Integer msg_id){
		ST st_el = stg.getInstanceOf("SequenceElementYAML");

		MessageOccurrenceSpecificationImpl send_spec = (MessageOccurrenceSpecificationImpl) msg.getSendEvent();
		MessageOccurrenceSpecificationImpl recv_spec = (MessageOccurrenceSpecificationImpl) msg.getReceiveEvent();
		
		st_el.add("msg_id", msg_id);
		st_el.add("signal", msg.getSignature().getName());
		st_el.add("source", send_spec.getCovered().getRepresents().getType().getName());
		st_el.add("target", recv_spec.getCovered().getRepresents().getType().getName());
		
		// System.out.println("RECEIVE BY: " + recv_spec.getCovered().getRepresents().getType().getName());
		// System.out.println("SENT BY: " + send_spec.getCovered().getRepresents().getType().getName());
		// MessageEnd end = m.getReceiveEvent();
		// System.out.println(m.allOwnedElements().toString());
		// System.out.println(m.getSendEvent().getOwnedElements().toString());

		return st_el.render();
	}
}
