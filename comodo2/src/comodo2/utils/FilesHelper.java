package comodo2.utils;

import comodo2.engine.Config;
import comodo2.engine.Main;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.log4j.Logger;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class FilesHelper {
  private static final Logger mLogger = Logger.getLogger(Main.class);
  
  public String getRelativeSourcePath() {
    String _currentModule = Config.getInstance().getCurrentModule();
    String _plus = ("/" + _currentModule);
    return (_plus + "/src/");
  }
  
  public String getRelativeConfigPath() {
    String _currentModule = Config.getInstance().getCurrentModule();
    String _plus = ("/" + _currentModule);
    String _plus_1 = (_plus + "/resource/config/");
    String _currentModule_1 = Config.getInstance().getCurrentModule();
    String _plus_2 = (_plus_1 + _currentModule_1);
    return (_plus_2 + "/");
  }
  
  public String toAbsolutePath(final String relativePath) {
    String _outputDirectory = Config.getInstance().getOutputDirectory();
    return (_outputDirectory + relativePath);
  }
  
  public String title(final String str) {
    String _upperCase = str.substring(0, 1).toUpperCase();
    String _substring = str.substring(1);
    return (_upperCase + _substring);
  }
  
  /**
   * Convert a Class name from "MyClass" to "myClass".
   */
  public String toFileName(final String className) {
    String _lowerCase = className.substring(0, 1).toLowerCase();
    String _substring = className.substring(1);
    return (_lowerCase + _substring);
  }
  
  public String toCppFilePath(final String className) {
    String _relativeSourcePath = this.getRelativeSourcePath();
    String _fileName = this.toFileName(className);
    String _plus = (_relativeSourcePath + _fileName);
    return (_plus + ".cpp");
  }
  
  public String toHppFilePath(final String className) {
    String _relativeSourcePath = this.getRelativeSourcePath();
    String _plus = (_relativeSourcePath + "/include/");
    String _currentModule = Config.getInstance().getCurrentModule();
    String _plus_1 = (_plus + _currentModule);
    String _plus_2 = (_plus_1 + "/");
    String _fileName = this.toFileName(className);
    String _plus_3 = (_plus_2 + _fileName);
    return (_plus_3 + ".hpp");
  }
  
  public String toXmlFilePath(final String className) {
    String _relativeSourcePath = this.getRelativeSourcePath();
    String _fileName = this.toFileName(className);
    String _plus = (_relativeSourcePath + _fileName);
    return (_plus + ".xml");
  }
  
  public String toRadEvFilePath(final String className) {
    String _relativeSourcePath = this.getRelativeSourcePath();
    String _fileName = this.toFileName(className);
    String _plus = (_relativeSourcePath + _fileName);
    return (_plus + ".rad.ev");
  }
  
  public String toScxmlFilePath(final String className) {
    String _relativeConfigPath = this.getRelativeConfigPath();
    String _fileName = this.toFileName(className);
    String _plus = (_relativeConfigPath + _fileName);
    return (_plus + ".xml");
  }
  
  /**
   * Checks if a given filename already exists,
   * in that case it creates a backup with filename:
   * filename.2020-06-20T11:01:09.172.backup
   */
  public void makeBackup(final String absPath) {
    try {
      Boolean _isFileBackupEnabled = Config.getInstance().isFileBackupEnabled();
      if ((_isFileBackupEnabled).booleanValue()) {
        File src = new File(absPath);
        boolean _isFile = src.isFile();
        if (_isFile) {
          String _startTime = Config.getInstance().getStartTime();
          String _plus = ("." + _startTime);
          String postfix = (_plus + ".backup");
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
    boolean _isGenerationModeAll = Config.getInstance().isGenerationModeAll();
    if (_isGenerationModeAll) {
      return false;
    }
    boolean _isGenerationModeUpdate = Config.getInstance().isGenerationModeUpdate();
    if (_isGenerationModeUpdate) {
      File src = new File(absPath);
      boolean _isFile = src.isFile();
      boolean _equals = (_isFile == false);
      if (_equals) {
        return false;
      }
    }
    FilesHelper.mLogger.debug((("Skipped generation of: " + absPath) + ", file already exists."));
    return true;
  }
}
