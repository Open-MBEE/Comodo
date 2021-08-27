package comodo2.engine;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 

/**
 * Singleton based on Bill Push.
 *
 * The private inner static class that contains the instance of the singleton class. 
 * When the singleton class is loaded, ConfigHelper class is not loaded into memory 
 * and only when someone calls the getInstance method, this class gets loaded and 
 * creates the Singleton class instance.
 */
public class Config {

	/**
	 * Code Generation modes
	 */
	public final static  String GEN_MODE_DEFAULT = "DEFAULT"; // generate only (fully generated) files not to be modified by the developer (scxml, event, ICD, ...)
	public final static  String GEN_MODE_UPDATE = "UPDATE"; // generate (fully generated) files not to be modified by the developer and actionMgr + new actions/activities
	public final static  String GEN_MODE_ALL = "ALL"; // generate all files also what is responsibility of the developer (i.e. actions/activities etc.)

	/**
	 * Supported Platforms
	 */
	public final static  String TARGET_PLATFORM_SCXML   = "SCXML";      // SCXML only
	public final static  String TARGET_PLATFORM_ELT_RAD = "ELT-RAD";    // RAD application (SCXML, MAL, rad.ev)
	public final static  String TARGET_PLATFORM_ELT_MAL = "ELT-MAL";    // MAL ICD only
	public final static  String TARGET_PLATFORM_QPC_QM = "QPC-QM";    	// Quantum Framework: Quantum Modeler file format

	/**
	 * Platforms specific configuration options
	 */
	public final static  String ELT_RAD_OPT_NOACTIONSTD = "NOACTIONSSTD";      // Don't add hard coded actionsStd implementation
	
	/**
	 * Configuration Parameters
	 */
	private String mCurrentModule = "";
	private String mModules = "";
	private String mTargetPlatform = "";	
	private String mTargetPlatformCfg = "";	
	private Path mModelFilepath;	
	private String mGenerationMode = GEN_MODE_DEFAULT;
	private String mOutputDirectory = "";	
	private Boolean mFileBackupEnabled = true;	
	/*
	 * By default we use fully qualified state names to avoid
	 * problems with duplicated state names.
	 */
	private boolean mGenerateFullyQualifiedStateNames = true;
	
	/**
	 * Other Parameters
	 */
	public LocalDateTime mStartTime;
	

	
	private Config() {}

	private static class ConfigHelper {
		private static final Config instance = new Config();
	}
				
	public boolean generateFullyQualifiedStateNames() {
		return mGenerateFullyQualifiedStateNames;
	}

	public boolean isModuleConfigured(String name) {
		if (mCurrentModule == "") {
			return true;
		}
		return mCurrentModule.contentEquals(name);
	}

	/**
	 * Checks whether the given model has to be transformed.
	 * @param modelName org.eclipse.emf.common.util.Resource.getURI().toFileString()
	 * @return true if the given model name correspond to the one
	 * provided as command line option.
	 */
	public boolean isModelConfigured(String modelName) {
		return (mModelFilepath.toAbsolutePath().toString().contentEquals(modelName));
	}
	
	public boolean isGenerationModeAll() {
		return mGenerationMode.contentEquals(GEN_MODE_ALL);
	}

	public boolean isGenerationModeUpdate() {
		return mGenerationMode.contentEquals(GEN_MODE_UPDATE);
	}

	public Boolean isFileBackupEnabled() {
		return mFileBackupEnabled;
	}

	public void DisableFileBackup() {
		mFileBackupEnabled = false;
	}

	public Path getModelFilepath() {
		return mModelFilepath;
	}

	public String getOutputDirectory() {
		return mOutputDirectory;
	}

	public String getCurrentModule() {
		return mCurrentModule;
	}

	public String[] getModules() {
		return mModules.split(" ");
	}

	public String getModulesStr() {
		return mModules;
	}

	public String getTargetPlatform() {
		return mTargetPlatform;
	}

	public String getTargetPlatformCfg() {
		return mTargetPlatformCfg;
	}

	public Boolean hasTargetPlatformCfgOption(String option) {
		return mTargetPlatformCfg.contains(option);
	}
	
	public String getGenerationMode() {
		return mGenerationMode;
	}

	public String getStartTimeStr() {
		 DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss.SSS");  
		 return mStartTime.format(format);   		  
		//return mStartTime.toString();
	}

	public void setGenerationMode(String mode) {
		if (mode.contentEquals(GEN_MODE_ALL)) {
			mGenerationMode = GEN_MODE_ALL;
		} else if (mode.contentEquals(GEN_MODE_UPDATE)) {
			mGenerationMode = GEN_MODE_UPDATE;
		} else {
			mGenerationMode = GEN_MODE_DEFAULT;
		}
	}

	public void setModelFilepath(Path modelFilepath) {
		mModelFilepath = modelFilepath;
	}

	public void setGenerateFullyQualifiedStateNames(boolean fullyQualified) {
		mGenerateFullyQualifiedStateNames = fullyQualified;
	}

	public void setCurrentModule(String module) {
		mCurrentModule = module;
	}

	public void setModules(String modules) {
		mModules = modules;
	}

	public void setTargetPlatform(String target) {
		mTargetPlatform = target;
	}

	public void setTargetPlatformCfg(String targetCfg) {
		mTargetPlatformCfg = targetCfg;
	}

	public void setStartTime() {
		mStartTime = LocalDateTime.now();
	}

	public void setOutputDirectory(String dir) {
		mOutputDirectory = dir;
	}

	public static Config getInstance(){
		return ConfigHelper.instance;
	}
	

}
