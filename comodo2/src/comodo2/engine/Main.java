package comodo2.engine;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.mwe2.language.Mwe2StandaloneSetup;
import org.eclipse.emf.mwe2.launch.runtime.Mwe2Runner;

import com.google.inject.Injector;

public class Main {
	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);
	//private static final Logger mLogger = Logger.getRootLogger();

	public static void main(String[] args) {
		try {
			//org.apache.log4j.BasicConfigurator.configure();
			new Main().run(args);
		} catch(Throwable throwable) {
			mLogger.error(throwable.getMessage(), throwable);
			System.exit(1);
		}		
	}

	/**
	 * Print help
	 */
	public void printHelp(Options opt) {
		HelpFormatter f = new HelpFormatter();
		f.printHelp("\njava -jar comodo2.jar {options}\n\n", opt);
	}

	public Options getOptions() {
		final Options opt = new Options();

		opt.addOption("h", "help", false, "Print help for this application.");
		opt.addOption("d", "debug", false, "Enable printing of debug information.");	
		opt.addOption("i", "input-model", true, "Filepath of the model to transform. Model should be in EMF XMI (.uml) format.");
		opt.addOption("o", "output-path", true, "Output directory path.");
		opt.addOption("t", "target-platform", true, "Target platform [SCXML|ELT-RAD|ELT-MAL].");		
		opt.addOption("c", "target-platform-config", true, "Configuration parameters specific to the target platform [NOACTIONSSTD].");
		opt.addOption("g", "generation-mode", true, "Generation mode [DEFAULT|UPDATE|ALL].");
		opt.addOption("n", "no-backup", false, "Disable automatic backup of overwritten files.");
		opt.addOption("a", "avoid-fully-qualified", false, "Avoid using fully qualified names.");

		// -m option is added manually since requires multiple number of values
		// -m for some platform may not be required
		OptionBuilder.withArgName("m");
		OptionBuilder.withLongOpt("modules");
		OptionBuilder.hasArgs();
		OptionBuilder.withDescription("Specify the module(s) to generate.");
		opt.addOption(OptionBuilder.create("m"));
		return opt;
	}

	public void run(String[] args) {	
		mLogger.setLevel(Level.INFO);

		Options options = getOptions();
		final CommandLineParser parser = new PosixParser();
		CommandLine line = null;
		try {
			Config.getInstance().setStartTime();
			mLogger.info("Starting execution at " + Config.getInstance().getStartTimeStr());

			line = parser.parse(options, args);

			/*
			 * Help option, show helper.
			 */
			if (line.hasOption('h') || line.getOptions().length == 0) {
				printHelp(options);
				return;
			}


			/*
			 * Enable logging of debug messages.
			 */
			if (line.hasOption('d')) {
				mLogger.setLevel(Level.DEBUG);
				mLogger.debug("Logging level set to: DEBUG");
			}

			/*
			 * Disable automatic backup of overwritten files.
			 */
			if (line.hasOption('n')) {
				Config.getInstance().DisableFileBackup();
				mLogger.debug("Files backup: DISABLED");
			} else {
				mLogger.debug("Files backup: ENABLED");
			}

			/*
			 * Avoid using fully qualified names.
			 */
			if (line.hasOption('a')) {
				Config.getInstance().setGenerateFullyQualifiedStateNames(false);
			} 
			mLogger.debug("Use fully qualified names: " + Config.getInstance().generateFullyQualifiedStateNames());

			/*
			 * Get input model and use class loader to resolve 
			 * file locations.
			 */
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			String modelName = "";
			if (line.hasOption('i') == false) {
				throw new ParseException("Missing input model.");				
			} else { 
				modelName = line.getOptionValue("i");
			}
			mLogger.debug("Model name: <" + modelName + ">");

			Path modelFilePath = Paths.get(modelName);
			if (Files.exists(Paths.get(modelName)) == false) {
				throw new ParseException("Model <" + modelName + "> does not exist.");
			}
			Config.getInstance().setModelFilepath(modelFilePath);
			mLogger.debug("Resolved model name: <" + modelFilePath.toAbsolutePath() + ">");			
			Path modelDirPath = modelFilePath.getParent();
			mLogger.debug("Resolved model directory: <" + modelDirPath.toAbsolutePath() + ">");			

			/*
			 * Get modules to generate.
			 */
			if (line.hasOption('m')) {
				Config.getInstance().setModules(line.getOptionValue("m"));
			}
			mLogger.debug("Modules: <" + Config.getInstance().getModulesStr() + ">");			

			/*
			 * Get target platform
			 */
			if (line.hasOption('t')) {
				Config.getInstance().setTargetPlatform(line.getOptionValue("t"));
			}
			mLogger.debug("Target Platform: <" + Config.getInstance().getTargetPlatform() + ">");			

			/*
			 * Get configuration for target platform
			 */
			if (line.hasOption('c')) {
				Config.getInstance().setTargetPlatformCfg(line.getOptionValue("c"));				
			} 
			mLogger.debug("Target Platform Configuration: <" + Config.getInstance().getTargetPlatformCfg() + ">");

			/*
			 * Get generation mode
			 */
			if (line.hasOption('g')) {
				Config.getInstance().setGenerationMode(line.getOptionValue("g"));
			}
			mLogger.debug("Generation Mode: <" + Config.getInstance().getGenerationMode() + ">");			

			/*
			 * Get output path
			 */
			String outputPath = "";
			if (line.hasOption('o') == false) {
				throw new ParseException("Missing output directory.");				
			} else { 
				outputPath = line.getOptionValue("o");
				Config.getInstance().setOutputDirectory(outputPath);
			}
			mLogger.debug("Output directory: <" + outputPath + ">");

			/*
			 * Creating parameters mapping for the workflow.
			 */
			Map<String, String> params = new HashMap<String, String>();
			params.put("outputPath", outputPath);
			params.put("modelPath", modelDirPath.toString());

			/*
			 * Run the workflow
			 */
			String workflowPath = Config.getInstance().getWorkflowFilename();
			//System.out.println("ClassLoader: " + classLoader.getClass().toString());
			URL urlWorkflow = classLoader.getResource(workflowPath);
			if (urlWorkflow == null) {
				mLogger.error("MWE " + workflowPath + " not found!");
				// TODO throw exception 
				return;
			}
			mLogger.debug("Workflow: " + workflowPath);

			mLogger.info("Starting transformation " + Config.getInstance().getTargetPlatform() 
					+ " on model " + modelFilePath + " for modules " + Config.getInstance().getModulesStr());
			long startTime = System.nanoTime();			

			// check  OperationCanceledException is accessible
			OperationCanceledException.class.getName();

			Injector injector = new Mwe2StandaloneSetup().createInjectorAndDoEMFRegistration();
			Mwe2Runner mweRunner = injector.getInstance(Mwe2Runner.class);
			
			mweRunner.run(URI.createURI(urlWorkflow.toString()), params);
			mLogger.info("Execution completed (" + (System.nanoTime() - startTime)/1e9 + "s).");			
		} catch(NoClassDefFoundError e) {
			if ("org/eclipse/core/runtime/OperationCanceledException".equals(e.getMessage())) {
				mLogger.error("Could not load class: org.eclipse.core.runtime.OperationCanceledException");
				mLogger.error("Add org.eclipse.equinox.common to the class path.");
			} else {
				throw e;
			}
		} catch (final ParseException exp) {
			//System.err.println("Parsing arguments failed: " + exp.getMessage());
			mLogger.error("Parsing arguments failed: " + exp.getMessage());
			printHelp(options);
			return;
		}
	}
}
