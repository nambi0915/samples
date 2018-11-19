package abbyy.ocrsdk;

import java.io.File;

import java.util.Arrays;
import java.util.Vector;

import otherdetails.HospitalDetailsParser;

public class AbbyyOcr extends HospitalDetailsParser {
	// Abbyy authentication
	private static Client restClient;
	private final static String appId = "orzo-abbyy";
	private final static String password = "ezvhxJ4+eC5ppWIsBUKVL/B0";
	/**
	 * Check that user specified application id and password.
	 * 
	 * @return false if no application id or password
	 */
	public static boolean checkAppId() {
		if (appId.isEmpty() || password.isEmpty()) {
			System.out.println(
					"Error: No application id and password are specified.");
			System.out.println("Please specify them in the program..");
			return false;
		}
		return true;
	}
	/**
	 * Initializing Abbyy Client
	 * 
	 */
	public AbbyyOcr() {
		restClient = new Client();
		// replace with 'https://cloud.ocrsdk.com' to enable secure connection
		restClient.serverUrl = "https://cloud.ocrsdk.com";
		restClient.applicationId = appId;
		restClient.password = password;
	}
	public static void displayHelp() {
		System.out.println("This program is able to recognize:\n" + "\n"
				+ "1. Extract Invoice information\n"
				+ "2. Arguments to pass:\n	\"recognize\" \"Path to image/pdf\" \"BatchId\" \"FileID\"\n"
				+ "3. To run jar:\n   java -jar recognize \"Path to image/pdf\" BatchId FileID\n");
	}

	private static void performRecognition(Vector<String> argList)
			throws Exception {
		String language = "English";
		String sourceFilePath = argList.lastElement();
		Integer lastIndexOfDot = sourceFilePath.lastIndexOf(".");
		outputTextFilePath = sourceFilePath.substring(0, lastIndexOfDot)
				+ ".txt";
		outputXmlFilePath = sourceFilePath.substring(0, lastIndexOfDot)
				+ ".xml";
		System.out.println("Output File 1: " + outputTextFilePath);
		System.out.println("Output File 2 " + outputXmlFilePath);
		// argList.remove(argList.size() - 1);
		// argList now contains list of source images to process

		ProcessingSettings.OutputFormat outputFormat = outputFormatByFileExt(
				outputTextFilePath);
		// String outputFormat = "docx";
		ProcessingSettings settings = new ProcessingSettings();
		settings.setLanguage(language);
		settings.setOutputFormat(outputFormat);
		settings.setTextType("ocrB,normal,typewriter");
		Task task = null;
		if (argList.size() == 1) {
			System.out.println("Uploading file recognizing ..");
			task = restClient.processImage(argList.elementAt(0), settings);
			sourceFilePath = argList.elementAt(0);
			System.out.println("Source path : " + argList.elementAt(0));
		} else if (argList.size() > 1) {
			// Upload images via submitImage and start recognition with
			// processDocument
			for (int i = 0; i < argList.size(); i++) {
				System.out.println(String.format("Uploading image %d/%d..",
						i + 1, argList.size()));
				String taskId = null;
				if (task != null) {
					taskId = task.Id;
				}
				Task result = restClient.submitImage(argList.elementAt(i),
						taskId);
				if (task == null) {
					task = result;
				}
			}
			task = restClient.processDocument(task.Id, settings);
		} else {
			System.out.println("No files to process.");
			return;
		}
		waitAndDownloadResult(task, outputTextFilePath, outputXmlFilePath);
	}

	/**
	 * Wait until task processing finishes and download result.
	 */
	private static void waitAndDownloadResult(Task task, String textOutputPath,
			String xmlOutputPath) throws Exception {
		task = waitForCompletion(task);
		// Task[] taskIds = restClient.listFinishedTasks();
		// System.out.println("TOTAL LENGTH"+taskIds.length);
		// for(int i = 2;i < 4;i++) {
		// task = taskIds[i];
		System.out.println("Task file count " + task);
		if (task.Status == Task.TaskStatus.Completed) {
			System.out.println("Downloading..");
			// System.out.println(restClient.listFinishedTasks());
			restClient.downloadResult(task, task.DownloadUrl, textOutputPath);
			restClient.downloadResult(task, task.DownloadUrl1, xmlOutputPath);
			System.out.println("Ready");
			// System.err.println("\nTime in seconds "+new Date());
		} else if (task.Status == Task.TaskStatus.NotEnoughCredits) {
			System.out.println("Not enough credits to process document. "
					+ "Please add more pages to your application's account.");
		} else {
			System.out.println("Task failed");
		}
		// }
	}

	private static Task waitForCompletion(Task task) throws Exception {
		// Note: it's recommended that your application waits
		// at least 2 seconds before making the first getTaskStatus request
		// and also between such requests for the same task.
		// Making requests more often will not improve your application
		// performance.
		// Note: if your application queues several files and waits for them
		// it's recommended that you use listFinishedTasks instead (which is
		// described
		// at http://ocrsdk.com/documentation/apireference/listFinishedTasks/).
		while (task.isTaskActive()) {

			Thread.sleep(5000);
			System.out.println("Waiting..");
			task = restClient.getTaskStatus(task.Id);
		}
		return task;
	}

	private static ProcessingSettings.OutputFormat outputFormatByFileExt(
			String filePath) {
		int extIndex = filePath.lastIndexOf('.');
		if (extIndex < 0) {
			System.out.println(
					"No file extension specified. Plain text will be used as output format.");
			return ProcessingSettings.OutputFormat.txt;
		}
		String ext = filePath.substring(extIndex).toLowerCase();
		if (ext.equals(".txt")) {
			return ProcessingSettings.OutputFormat.txt;
		} else if (ext.equals(".xml")) {
			return ProcessingSettings.OutputFormat.xml;
		} else if (ext.equals(".pdf")) {
			return ProcessingSettings.OutputFormat.pdfSearchable;
		} else if (ext.equals(".docx")) {
			return ProcessingSettings.OutputFormat.docx;
		} else if (ext.equals(".xlsx")) {
			return ProcessingSettings.OutputFormat.xlsx;
		} else if (ext.equals(".rtf")) {
			return ProcessingSettings.OutputFormat.rtf;
		} else {
			System.out.println(
					"Unknown output extension. Plain text will be used.");
			return ProcessingSettings.OutputFormat.txt;
		}
	}
	/**
	 * Module that performs OCR for given file
	 * 
	 * @param modeAndSourceFileList
	 *            List containing mode of extraction and source file path
	 * @throws Exception
	 */
	public void doOcr(Vector<String> modeAndSourceFileList) throws Exception {
		System.out.println("Process documents using ABBYY Cloud OCR SDK.\n");
		if (!checkAppId()) {
			return;
		}
		if (modeAndSourceFileList.size() >= 1) {
			// Removing mode
			String mode = modeAndSourceFileList.remove(0);
			// System.out.println("X "+argList +"\n MODE "+mode);
			if (mode.equalsIgnoreCase("recognize")) {
				try {
					performRecognition(modeAndSourceFileList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Unknown mode: " + mode);
				return;
			}
		} else {
			System.out.println("Ooops! Check arguments");
		}
		// performRecognition(argList);
	}
}
