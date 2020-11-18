package comodo2.workflows;

import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@Accessors
public class GeneratorConfig {
	private String outputPath;

	@Pure
	public String getOutputPath() {
		return this.outputPath;
	}

	public void setOutputPath(final String outputPath) {
		this.outputPath = outputPath;
	}
}
