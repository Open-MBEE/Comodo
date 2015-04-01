package eso.scxmltester;

import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.SCXMLHelper;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleScheduler;
import org.apache.commons.scxml.env.Tracer;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.invoke.SimpleSCXMLInvoker;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.io.SCXMLSerializer;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.TransitionTarget;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;

import eso.scxmltester.TextAreaAppender;
import eso.scxmltester.TextAreaHandler;

/**
 * 
 * @author 
 */
public class SCXMLTester extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final static String newline = "\n";
	//private Logger myLogger = Logger.getLogger(this.getClass().getName());  
	//private java.util.logging.Logger jdkLogger = java.util.logging.Logger.getLogger(this.getClass().getName());  //  @jve:decl-index=0:

	JButton loadBtn = new JButton("Load");
	JButton reloadBtn = new JButton("Reload");
	JButton showBtn = new JButton("Show");
	JButton statusBtn = new JButton("Status");
	JButton resetBtn = new JButton("Reset");
	JButton clearBtn = new JButton("Clear");
	//JButton helpBtn = new JButton("Help");
	JCheckBox debugBtn = new JCheckBox("Debug");	
	JLabel labelField = new JLabel("Command: ");
	JLabel labelCombo = new JLabel("Trigger: ");
	JTextField cmdTextField = new JTextField(20);
	JComboBox comboBox = new JComboBox();
	
	//JCheckBox myCheckBox = new JCheckBox("Check");
	JTextArea myText = new JTextArea(""); 
	JScrollPane scrollPane = new JScrollPane(myText);
	JPanel bottomPanel = new JPanel(); // The bottom panel which holds buttons.
	JPanel topPanel = new JPanel(); // The top panel which holds buttons.

	JPanel holdAll = new JPanel(); // The top level panel which holds all.

	
	String selectedModel;
	boolean isModelLoaded = false;
    String documentURI;
    Context rootCtx;
    Tracer trc;
    SCXML doc;
    SCXMLExecutor exec;
    EventDispatcher ed;

	Evaluator evaluator = new JexlEvaluator();
	//Evaluator evaluator = new ELEvaluator();

	/**
	 * The constructor.
	 */
	public SCXMLTester() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		bottomPanel.setLayout(flowLayout);
		bottomPanel.add(labelField);
		bottomPanel.add(cmdTextField);
		bottomPanel.add(labelCombo);
		bottomPanel.add(comboBox);

		topPanel.setLayout(flowLayout);
		topPanel.add(loadBtn);
		topPanel.add(reloadBtn);
		topPanel.add(resetBtn);
		topPanel.add(showBtn);
		topPanel.add(statusBtn);
		topPanel.add(clearBtn);
		//topPanel.add(helpBtn);
		topPanel.add(debugBtn);

		holdAll.setLayout(new BorderLayout());
		holdAll.add(bottomPanel, BorderLayout.SOUTH);
		holdAll.add(topPanel, BorderLayout.NORTH);

		//holdAll.add(myText, BorderLayout.CENTER);
		scrollPane.setAutoscrolls(true);
		holdAll.add(scrollPane, BorderLayout.CENTER);

		getContentPane().add(holdAll, BorderLayout.CENTER);
		
		loadBtn.addActionListener(this);
		reloadBtn.addActionListener(this);
		showBtn.addActionListener(this);
		statusBtn.addActionListener(this);
		resetBtn.addActionListener(this);
		clearBtn.addActionListener(this);
		cmdTextField.addActionListener(this);
		//comboBox.addActionListener(this);		
		//helpBtn.addActionListener(this);
		debugBtn.addActionListener(this);

		// help tooltip
		loadBtn.setToolTipText("Load an SCXML model and start execution.");
		reloadBtn.setToolTipText("Reload prev. SCXML model.");
		showBtn.setToolTipText("Show the loaded SCXML model.");
		statusBtn.setToolTipText("Show active states.");
		resetBtn.setToolTipText("Restart execution of the loaded SCXML model.");
		clearBtn.setToolTipText("Clear the output textbox.");
		debugBtn.setToolTipText("Enable/disable all loggings from SCXML engine.");
		comboBox.setToolTipText("Trigger an event.");
		cmdTextField.setToolTipText("Enter events and variable assignments");
		
		myText.setEditable(false);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		/*
		 * Logs should go to both console and TextArea.
		 */
		setupLog4JAppender(myText);
		//setupJdkLoggerHandler(myText);

		/*
		 * By default TextArea logs are filtered out.
		 * They can be enabled by checking the debug button.
		 */
		TextAreaAppender ap = (TextAreaAppender) Logger.getRootLogger().getAppender("TEXTAREA");
		ap.setThreshold(Level.OFF);
			
	}

	/**
	 * Each non abstract class that implements the ActionListener must have this
	 * method.
	 * 
	 * @param e
	 *            the action event.
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == loadBtn) {
			final JFileChooser fc = new JFileChooser();
			if (isModelLoaded) {
				File dir = new File(selectedModel);
				fc.setCurrentDirectory(dir);
			} else {
				File dir = new File(".");
				fc.setCurrentDirectory(dir);		
			}
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setDialogTitle("Select SCXML file");
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				selectedModel = fc.getSelectedFile().getPath();
	
				// load all events in the comboBox
				populateComboBox();
	
				loadModel();
				startExecution();
				printStatus(true);
				isModelLoaded = true;
			}
		} else if (e.getSource() == reloadBtn) {
			if (isModelLoaded == false) {
				printText("ERROR: no model loaded.");
				return;
			}

			// load all events in the comboBox
			populateComboBox();

			loadModel();
			startExecution();
			printStatus(true);
			isModelLoaded = true;
			
		} else if (e.getSource() == resetBtn) {
			if (isModelLoaded == false) {
				printText("ERROR: no model loaded.");
				return;
			}
			resetExecution();
			printStatus(true);
		} else if (e.getSource() == clearBtn) {
			myText.setText("");
			//printText("Clear text.");
			//myText.setText(null);
			//myText.replaceRange("",0,myText.getText().length());
		} else if (e.getSource() == statusBtn) {
			if (isModelLoaded == false) {
				printText("ERROR: no model loaded.");
				return;
			}
			printStatus(true);
//		} else if (e.getSource() == helpBtn) {
//			printHelp();
		} else if (e.getSource() == debugBtn) {
			TextAreaAppender ap = (TextAreaAppender) Logger.getRootLogger().getAppender("TEXTAREA");
			if (debugBtn.isSelected()) {
				ap.setThreshold(Level.DEBUG);
			} else {
				ap.setThreshold(Level.OFF);
			}
		} else if (e.getSource() == showBtn) {
			if (isModelLoaded == false) {
				printText("ERROR: no model loaded.");
				return;
			}
			printText("\nModel: " + selectedModel + "\n\n" + SCXMLSerializer.serialize(doc));
		} else if (e.getSource() == cmdTextField) {
			if (isModelLoaded == false) {
				printText("ERROR: no model loaded.");
				return;
			}
			String text = cmdTextField.getText();
		    printText("CMD> " + text);
		    executeCommand(text);
			printStatus(true);			    
		} else if (e.getSource() == comboBox) {
			String eventName = (String) comboBox.getSelectedItem();
			//fireEvent(eventName);
			printText("CMD>" + eventName);
			executeCommand(eventName);
			printStatus(true);			    			
		} else {
			printText("???");
		}
	}

	public void loadModel() {
	
        try {
            documentURI = getCanonicalURI(selectedModel);
            rootCtx = evaluator.newContext(null);
            trc = new Tracer();
            doc = SCXMLParser.parse(new URL(documentURI), trc); 
            if (doc == null) {
            	printText("\nERROR: The SCXML document " + selectedModel + " can not be parsed!");
                System.exit(-1);
            }
            printText(SCXMLSerializer.serialize(doc));
			printText("\nLoaded: " + selectedModel);
        } catch (IOException err) {
        	err.printStackTrace();
        } catch (ModelException err) {
        	err.printStackTrace();
        } catch (SAXException err) {
        	err.printStackTrace();
        }		
	}
	
	public void startExecution() {
        try {
        	exec = new SCXMLExecutor(evaluator, null, trc);
        	ed = new SimpleScheduler(exec);
        	exec.setEventdispatcher(ed);
        	exec.setStateMachine(doc);
        	exec.addListener(doc, trc);
        	exec.registerInvokerClass("scxml", SimpleSCXMLInvoker.class);
        	exec.setRootContext(rootCtx);
        	exec.go();
			printText("\nStarted execution: " + selectedModel);

        } catch (ModelException err) {
        	err.printStackTrace();
        }
	}
	
	public void resetExecution() {
		try {
			exec.reset();
		} catch (ModelException err) {
	        err.printStackTrace();
	    }	
		printText("\nReset execution: " + selectedModel);
	}
	
	public void executeCommand(final String theEvent) {
		try {
            String event = theEvent.trim();
            if (event.equalsIgnoreCase("help") || event.equals("?")) {
            	printHelp();
            } else if (event.equalsIgnoreCase("quit")) {
            	// break, exit
            } else if (event.equalsIgnoreCase("reset")) {
                resetExecution();
            } else if (event.indexOf('=') != -1) {
                int marker = event.indexOf('=');
                String name = event.substring(0, marker);
                String value = event.substring(marker + 1);
                rootCtx.setLocal(name, value);
                printText("\nSet variable " + name + " to " + value);
            } else if (SCXMLHelper.isStringEmpty(event) || event.equalsIgnoreCase("null")) {
                TriggerEvent[] evts = {new TriggerEvent(null, TriggerEvent.SIGNAL_EVENT, null)};
                exec.triggerEvents(evts);
                if (exec.getCurrentStatus().isFinal()) {
                	printText("\nA final configuration reached.");
                }
            } else {
                printText("\nProcess events: <" + event + ">");

                StringTokenizer st = new StringTokenizer(event);
                int tkns = st.countTokens();
                TriggerEvent[] evts = new TriggerEvent[tkns];
                for (int i = 0; i < tkns; i++) {
                    evts[i] = new TriggerEvent(st.nextToken(),
                            TriggerEvent.SIGNAL_EVENT, null);
                    
                    //printText("Event: <" + evts[i].getName() + ">");
                    
                }
                exec.triggerEvents(evts);
                if (exec.getCurrentStatus().isFinal()) {
                	printText("\nA final configuration reached.");
                }
            }
	    } catch (ModelException err) {
	        err.printStackTrace();
	    }
	}

	public void fireEvent(final String eventName) {
		try {
            TriggerEvent evt = new TriggerEvent(eventName, TriggerEvent.SIGNAL_EVENT, null);
            exec.triggerEvent(evt);
            if (exec.getCurrentStatus().isFinal()) {
            	printText("\nA final configuration reached.");
            }
		} catch (ModelException err) {
			err.printStackTrace();
		}
	}
	
    /**
     * @param uri an absolute or relative URL
     * @return java.lang.String canonical URL (absolute)
     * @throws java.io.IOException if a relative URL can not be resolved
     *         to a local file
     */
    private static String getCanonicalURI(final String uri)
    throws IOException {
        if (uri.toLowerCase().startsWith("http://")
            || uri.toLowerCase().startsWith("file://")) {
                return uri;
        }
        File in = new File(uri);
        return "file:///" + in.getCanonicalPath();
    }

    public String readTextFile(final String fullPathFilename) {
		StringBuffer buffer = new StringBuffer(1024);
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(fullPathFilename));
    		char[] chars = new char[1024];
    		while (reader.read(chars) > -1) {
    			buffer.append(String.valueOf(chars));
    		}
    		reader.close();
    	} catch (IOException e) {
    		printText(e.toString());
    	}
    	return buffer.toString();
    }
    
	public Set<String> extractEvents(final String fullPathFilename) {
		// we use TreeSet to get the set ordered
		Set<String> eventsSet = new TreeSet<String>();
		
		String text = readTextFile(fullPathFilename);
		//printText(text);
		
		//String text = "bla vla event=\"pippo\" bla bla";
		Pattern p = Pattern.compile("event=\"[a-zA-Z0-9]+\"", Pattern.CASE_INSENSITIVE + Pattern.MULTILINE);
		//Pattern p = Pattern.compile("event=\"[\p{Graph}]+\"", Pattern.CASE_INSENSITIVE + Pattern.MULTILINE);

		Matcher m = p.matcher(text);
		while (m.find()) {
			String token = text.substring(m.start(),m.end());
			String[] listStr = token.split("\"");
			String eventName = listStr[1];
			//printText("Event = " + eventName);
			eventsSet.add(eventName);
		}
		
		
		//printText(eventsSet.toString());
		
		return eventsSet;
	}
    
	public void populateComboBox() {
		//comboBox.setEnabled(false);
		comboBox.removeActionListener(this);		

		Set<String> eventsSet = extractEvents(selectedModel);
		//String[] eventsString = (String[]) eventsSet.toArray();
		comboBox.removeAllItems();
		Iterator<String> i = eventsSet.iterator();
		while (i.hasNext()) {
			comboBox.addItem(i.next());
		}
		//comboBox.setEnabled(true);
		comboBox.addActionListener(this);		

	}
	
	public void printStatus(final boolean leafOnly) {
		Set<TransitionTarget> activeStates;
		if (leafOnly) {
			activeStates = exec.getCurrentStatus().getStates();
		} else {
			activeStates = exec.getCurrentStatus().getAllStates();
		}
		String status = "\nActive states: ";
		for (Iterator<TransitionTarget> i = activeStates.iterator(); i.hasNext();) {
			TransitionTarget tt = i.next();
			status = status + " " + tt.getId();
        }
        printText(status);
	}

	public void printHelp() {
        printText("\nHelp on SCXMLTester:");
        printText("  LOAD: loads an SCXML model and starts execution.");
        printText("  SHOW: shows loaded SCXML model.");
        printText("  STATUS: shows active states.");
        printText("  RESET: restart execution of the loaded SCXML model.");
        printText("  CLEAR: clear the textbox.");
        printText("  CMD: allows to enter events and variable assignments");
		printText("       events: enter a space-separated list of events followed by enter");
        printText("       assignments: to populate a variable in the current context, type \"name=value\"");
        printText("  HELP: shows this text.");
        printText("  DEBUG: enable all loggings from SCXML engine.");

        //printText("To quit, enter \"quit\"");
        //printText("To reset state machine, enter " + "\"reset\"");               
	}

	public void printText(final String text) {
		//myText.append(text+newline);
		myText.setText(myText.getText() + text + newline);
	}

	protected static void setupLog4JAppender(JTextArea jTextArea) {
		// This code attaches the appender to the text area
		TextAreaAppender.setTextArea(jTextArea);
		
		// Normally configuration would be done via a log4j.properties
		// file found on the class path, but here we will explicitly set
		// values to keep it simple.
		//
		// Great introduction to Log4J at http://logging.apache.org/log4j/docs/manual.html
		//
		// Could also have used straight code like: app.logger.setLevel(Level.INFO);
		Properties logProperties = new Properties();
		logProperties.put("log4j.rootLogger", "ALL, CONSOLE, TEXTAREA");

		logProperties.put("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender"); // A standard console appender
		logProperties.put("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout"); //See: http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
		logProperties.put("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss} [%12.12t] %5.5p %40.40c: %m%n");
		
		logProperties.put("log4j.appender.TEXTAREA", "eso.scxmltester.TextAreaAppender");  // Our custom appender
		logProperties.put("log4j.appender.TEXTAREA.layout", "org.apache.log4j.PatternLayout"); //See: http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
		logProperties.put("log4j.appender.TEXTAREA.layout.ConversionPattern", "%d{HH:mm:ss} %5.5p %40.40c: %m%n");
		
		PropertyConfigurator.configure(logProperties);
	}

	protected static void setupJdkLoggerHandler(JTextArea logJTextArea) {
		// This code attaches the handler to the text area
		TextAreaHandler.setTextArea(logJTextArea);
		
		// Normally configuration would be done via a properties file
		// that would be read in with LogManager.getLogManager().readConfiguration()
		// But I create an inputstream here to keep it local.
		// See JAVA_HOME/jre/lib/logging.properties for more description of these settings.
		//
		StringBuffer buf = new StringBuffer();
		buf.append("handlers = TextAreaHandler, java.util.logging.ConsoleHandler"); // A default handler and our custom handler
		buf.append("\n");
		buf.append(".level = INFO"); // Set the default logging level see: C:\software\sun\jdk141_05\docs\api\index.html
		buf.append("\n");
		buf.append("TextAreaHandler.level = INFO"); // Custom Handler logging level
		buf.append("\n");
		buf.append("java.util.logging.ConsoleHandler.level = INFO"); // Custom Handler logging level
		buf.append("\n");
//		buf.append("java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter"); //
//		buf.append("\n");
//		buf.append("com.zcage.log.TextAreaHandler.formatter = java.util.logging.SimpleFormatter"); //
//		buf.append("\n");
//		buf.append("java.awt.KeyboardFocusManager.level = INFO");  // Set the logging level for this logger  
		
		try {
			java.util.logging.LogManager.getLogManager().readConfiguration(
					new ByteArrayInputStream(buf.toString().getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * The program * @param args the program start up parameters, not used.
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				SCXMLTester myApplication = new SCXMLTester();

				// Specify where will it appear on the screen:
				myApplication.setLocation(10, 10);
				myApplication.setSize(650, 400);
				myApplication.setTitle("SCXMLTester");

				// Show it!
				myApplication.setVisible(true);
			}
		});
	}

}
