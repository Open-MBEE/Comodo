package eso.uml2scxml;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


import org.eclipse.emf.mwe.core.WorkflowRunner;



public class UML2SCXML extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final String MWE_WORKFLOW_FILENAME = "UML2SCXML.mwe";
	
	String workflowFilename = "";
	String inputModel = "";
	String outputPath = "";
	
		
	JButton loadBtn = new JButton("UML2SCXML");
	JTextArea myText = new JTextArea(""); 
	JScrollPane scrollPane = new JScrollPane(myText);
	JPanel bottomPanel = new JPanel(); // The bottom panel which holds buttons.
	JPanel holdAll = new JPanel(); // The top level panel which holds all.

	/**
	 * The constructor.
	 */
	public UML2SCXML() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);	
		bottomPanel.setLayout(flowLayout);
		bottomPanel.add(loadBtn);
		holdAll.setLayout(new BorderLayout());
		holdAll.add(bottomPanel, BorderLayout.SOUTH);
		//holdAll.add(myText, BorderLayout.CENTER);
		scrollPane.setAutoscrolls(true);
		holdAll.add(scrollPane, BorderLayout.CENTER);

		getContentPane().add(holdAll, BorderLayout.CENTER);
		
		loadBtn.addActionListener(this);

		// help tooltip
		loadBtn.setToolTipText("Load a UML model and convert it into SCXML.");
		
		myText.setEditable(false);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);			
		
		setWorkflowFilename(findMweWorkflow());
	}

	public String getWorkflowFilename() {
		return workflowFilename;
	}

	public void setWorkflowFilename(final String filename) {
		workflowFilename = filename;
	}
	
	public void printText(final String text) {
		myText.setText(myText.getText() + text + "\n");
	}

	public String findMweWorkflow() {
		
		String workflowFullFilename = "./"+MWE_WORKFLOW_FILENAME;
		File workflowFile = new File(workflowFullFilename);
		if (workflowFile.exists() == false) {
			final JFileChooser fc = new JFileChooser();	
			File dir = new File(".");
			fc.setCurrentDirectory(dir);				
			fc.setDialogTitle("Select UML2SCXML.mwe workflow file");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setApproveButtonText("Select");
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				workflowFullFilename = fc.getSelectedFile().getPath();	
			} else {
				return "";							
			}
		}			
		
		return workflowFullFilename;
	}
	
	public static int convertUml2Scxml(final String workflowPath, final String inputModel, final String outputPath) {

		try {
			/*
			 * For some unknown reason output directory should not be in URL format 
			 * while the model path should be URL!
			 */
			URL inputModelURL = new URL("file:///" + inputModel);
			//URL outputPathURL = new URL("file:///" + outputPath);
			URL workflowPathURL = new URL("file:///" + workflowPath);

			//printText("\ninputModelURL: "+inputModelURL.toString()+"\n");
			//printText("outputPathURL:   "+outputPathURL.toString()+"\n");
			//printText("workflowPathURL: "+workflowPathURL.toString()+"\n");
			
			// Map the properties of the workflow
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("modelFileURI", inputModelURL.toString());
			properties.put("ouputFolderURI", outputPath.toString());

			Map<String, String> slotContents = new HashMap<String, String>();

			// Start the transformation by invoking the workflow runner
			WorkflowRunner wrunner = new WorkflowRunner();
			wrunner.run(workflowPathURL.toString(), null, properties, slotContents);			
		} catch (Exception e) {
			e.printStackTrace();	
			return -1;
		}
		return 0;
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
			
			if (getWorkflowFilename().isEmpty()) {
				String myWorkflowFilename = findMweWorkflow();
				if (myWorkflowFilename.isEmpty()) {
					printText("ERROR: UML2SCXML.mwe workflow file not found.");
					return;
				}
				setWorkflowFilename(myWorkflowFilename);
			}
			
			final JFileChooser fc = new JFileChooser();			
			if (inputModel.isEmpty() == false) {
				File dir = new File(inputModel);
				fc.setCurrentDirectory(dir);
			} else {
				File dir = new File(".");
				fc.setCurrentDirectory(dir);				
			}
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setDialogTitle("Select UML Model");
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				inputModel = fc.getSelectedFile().getPath();	
				printText("UML Model:  " + inputModel);
				
				File curDirectory = new File(inputModel);
				fc.setDialogTitle("Select output directory");
				fc.setCurrentDirectory(curDirectory);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setApproveButtonText("Select");
				returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					outputPath = fc.getSelectedFile().getPath();					
					printText("SCXML File: " + outputPath);
					
					/*
					String workflowFilename = "./"+MWE_WORKFLOW_FILENAME;
					File workflowFile = new File(workflowFilename);
					if (workflowFile.exists() == false) {
						fc.setDialogTitle("Select UML2SCXML.mwe workflow file");
						fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fc.setApproveButtonText("Select");
						returnVal = fc.showOpenDialog(this);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							workflowFilename = fc.getSelectedFile().getPath();	
						} else {
							printText("UML2SCXML Workflow file not found.");	
							return;							
						}
					}					
					*/
					
					printText("UML2SCXML MWE File: " + getWorkflowFilename());
					printText("Start transformation: " + new Date().toString());
					if (convertUml2Scxml(getWorkflowFilename(), inputModel, outputPath) != 0) {
						printText("ERROR: converting " + inputModel + " UML model using " + workflowFilename);
					} else {
						printText("End transformation:   " + new Date().toString());	
					}
				} else {
					printText("Output directory not selected.");	
					return;
				}
			} else {
				printText("UML model not selected.");				
				return;
			}
		} else {
			printText("Unknown event");
		}		
	}

		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				UML2SCXML myApplication = new UML2SCXML();
								
				// Specify where will it appear on the screen:
				myApplication.setLocation(10, 10);
				myApplication.setSize(650, 400);
				myApplication.setTitle("UML2SCXML");
								
				// Show it!
				myApplication.setVisible(true);
							
			}
		});
		
	}


}
