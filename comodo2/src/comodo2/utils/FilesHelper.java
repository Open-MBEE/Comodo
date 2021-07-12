package comodo2.utils;

import comodo2.engine.Config;
import comodo2.engine.Main;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.log4j.Logger;
import org.eclipse.xtext.xbase.lib.Exceptions;

public class FilesHelper {
	private static final Logger mLogger = Logger.getLogger(Main.class);

	public String getRelativeSourcePath() {
		return "/" + Config.getInstance().getCurrentModule() + "/src/";
	}

	public String getRelativeConfigPath() {
		return ("/" + Config.getInstance().getCurrentModule() + "/resource/config/" + Config.getInstance().getCurrentModule() + "/");
	}

	public String toAbsolutePath(final String relativePath) {
		return Config.getInstance().getOutputDirectory() + relativePath;
	}

	public String title(final String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Convert a Class name from "MyClass" to "myClass".
	 */
	public String toFileName(final String className) {
		return className.substring(0, 1).toLowerCase() + className.substring(1);
	}

	public String toCppFilePath(final String className) {
		return getRelativeSourcePath() + toFileName(className) + ".cpp";
	}

	public String toCFilePath(final String className) {
		return "/" + Config.getInstance().getCurrentModule() + "/" + toFileName(className) + ".c";
	}

	public String toHFilePath(final String className) {
		return "/" + Config.getInstance().getCurrentModule() + "/" + toFileName(className) + ".h";
	}

	public String toHppFilePath(final String className) {
		return getRelativeSourcePath() + "/include/" + Config.getInstance().getCurrentModule() + "/" + toFileName(className) + ".hpp";
	}

	public String toXmlFilePath(final String className) {
		return getRelativeSourcePath() + toFileName(className) + ".xml";
	}

	public String toRadEvFilePath(final String className) {
		return getRelativeSourcePath() + toFileName(className) + ".rad.ev";
	}

	public String toScxmlFilePath(final String className) {
		return getRelativeConfigPath() + toFileName(className) + ".xml";
	}

	public String toQmFilePath(final String className) {
		// QM files are XML files that open in the Quantum Modeler
		return "/" + Config.getInstance().getCurrentModule() + "/" + toFileName(className) + ".qm";
	}

	public String toQmImplFilePath(final String fileName) {
		// QM files are XML files that open in the Quantum Modeler
		return "/" + Config.getInstance().getCurrentModule() + "/" + fileName;
	}

	/**
	 * Checks if a given filename already exists,
	 * in that case it creates a backup with filename:
	 * filename.2020-06-20T11:01:09.172.backup
	 */
	public void makeBackup(final String absPath) {
		try {
			if (Config.getInstance().isFileBackupEnabled()) {
				File src = new File(absPath);
				if (src.isFile()) {
					String postfix = ("." + Config.getInstance().getStartTimeStr() + ".backup");
					File dst = new File((absPath + postfix));
					Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
					FilesHelper.mLogger.debug((("Created backup file: " + absPath) + postfix));
				}
			}
		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	/**
	 * This method check if the given file can be created
	 * or should be skipped.
	 * 
	 * In ALL mode, all files can be generated regardless if they exists already.
	 * In UPDATE mode, only files that do not exists can be generated.
	 */
	public boolean skipFile(final String absPath) {
		if (Config.getInstance().isGenerationModeAll()) {
			return false;
		}
		if ( Config.getInstance().isGenerationModeUpdate()) {
			File src = new File(absPath);
			if (src.isFile() == false) {
				return false;
			}
		}
		FilesHelper.mLogger.debug((("Skipped generation of: " + absPath) + ", file already exists."));
		return true;
	}
}
