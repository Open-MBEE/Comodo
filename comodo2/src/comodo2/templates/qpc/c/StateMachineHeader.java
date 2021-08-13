package comodo2.templates.qpc.c;

import javax.inject.Inject;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import comodo2.queries.QClass;
import comodo2.queries.QRegion;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.templates.qpc.Utils;
import comodo2.templates.qpc.model.CurrentGeneration;
import comodo2.templates.qpc.model.OrthogonalStateWrapper;
import comodo2.templates.qpc.model.RegionWrapper;
import comodo2.templates.qpc.traceability.FileDescriptionHeader;
import comodo2.utils.FilesHelper;

public class StateMachineHeader implements IGenerator {
	
	@Inject
	private QClass mQClass;

	@Inject
	private QState mQState;

	@Inject
	private QRegion mQRegion;

	@Inject
	private Utils mUtils;

	@Inject
	private QStateMachine mQStateMachine;
	
	@Inject
	private FilesHelper mFilesHelper;
	
	@Inject
	private FileDescriptionHeader mFileDescHeader;

	public CurrentGeneration current;

	/**
	 * Generates the header file for the State Machine source file.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		
		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof org.eclipse.uml2.uml.Class) {
				org.eclipse.uml2.uml.Class c = (org.eclipse.uml2.uml.Class)e; 
				if ((mQClass.isToBeGenerated(c) && mQClass.hasStateMachines(c))) {
					for (final StateMachine sm : mQClass.getStateMachines(c)) {
						// Sets current generation context
						current = new CurrentGeneration(c.getName(), sm.getName());

						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(current.getSmQualifiedName())));
						fsa.generateFile(mFilesHelper.toHFilePath(current.getSmQualifiedName()), this.generate(sm, current));						
					}
				}				
			}
		}
	}


	public CharSequence generate(StateMachine sm, CurrentGeneration current) {
        STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineHeader.stg");
		ST st = g.getInstanceOf("StateMachineHeader");
		st.add("fileDescriptionHeader", mFileDescHeader.generateFileDescriptionHeader(current.getClassName(), sm.getName(), true));
		st.add("className", current.getClassName());
		st.add("smQualifiedName", current.getSmQualifiedName());
		st.add("smQualifiedNameUpperCase", current.getSmQualifiedName().toUpperCase());

		// retrieve states qualified name and format them
		BasicEList<String> statesList = new BasicEList<String>();
		BasicEList<String> activeObjectList = new BasicEList<String>();
		for (State s : mQStateMachine.getAllStates(sm)){
			statesList.add(mUtils.formatStateName(mQState.getFullyQualifiedName(s), current.getSmQualifiedName()));
			activeObjectList.add(getActiveObjectName(s));
		}

		// Orthogonal regions definitions
		BasicEList<RegionWrapper> orthogonalRegions = new BasicEList<RegionWrapper>();
		for (State s : mQStateMachine.getAllOrthogonalStates(sm)){
			OrthogonalStateWrapper orthogonalStateWrapper = new OrthogonalStateWrapper(s, current);
			orthogonalRegions.addAll(orthogonalStateWrapper.getWrappedRegions());
		}
		if (!orthogonalRegions.isEmpty()){
			st.add("orthogonalRegions", orthogonalRegions);
		} // end of orthogonal regions definitions


		st.add("statesList", statesList);
		st.add("activeObjectList", activeObjectList);
		st.add("historyPseudostatesList", mQStateMachine.getAllHistoryPseudostates(sm));
		st.add("timeEventList", mQStateMachine.getAllStatesWithTimeEvents(sm));

        return st.render();
	}

	/**
	 * This varies based on whether the state is part of an orthogonal region or not.
	 * If part of an orthogonal region, the ActiveObject will be the one of the region.
	 * If not, the ActiveObject will be the one of the state machine.
	 * @param s State
	 * @return name of the ActiveObject that this state definition takes as an input.
	 */
	public String getActiveObjectName(final State s){
		Region r = mQState.getParentOrthogonalRegion(s);
		if (r!=null){
			return mUtils.formatRegionName(mQRegion.getFullyQualifiedName(r));
		} else {
			return current.getSmQualifiedName();
		}
	}
}
