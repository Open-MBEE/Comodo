package comodo2.templates.qpc.traceability;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class FileDescriptionHeader {
    
    public String generateFileDescriptionHeader(String className, String smName, Boolean printReq){
		STGroup g = new STGroupFile("resources/qpc_tpl/FileDescriptionHeader.stg");
		ST st = g.getInstanceOf("FileDescriptionHeader");

		st.add("className", className);
		st.add("smName", smName);

		if (printReq){
			st.add("requirementsList", printRequirementsList());
		}

		return st.render();
	}

	public String printRequirementsList(){
		STGroup g = new STGroupFile("resources/qpc_tpl/FileDescriptionHeader.stg");
		ST st = g.getInstanceOf("RequirementsList");

		String[] reqList = {"<requirement 162>", "<requirement 177>"};
		st.add("requirements", reqList);

		return st.render();
	}
}
