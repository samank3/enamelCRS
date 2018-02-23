package enamel;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import org.junit.Before;
import org.junit.Test;

public class AuthoringTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testImportScenario() {
		File testFile = new File("FactoryScenarios/Scenario_1.txt");
		Authoring.file_chooser = new JFileChooser();
		Authoring.file_chooser.setSelectedFile(testFile);
		String fileName = Authoring.file_chooser.getSelectedFile().getName();
		assertEquals("Scenario_1.txt", fileName);
	}

	@Test
	public void testExportScenario() {
		File testFile = new File("FactoryScenarios/Scenario_test.txt");
		Authoring.file_chooser = new JFileChooser();
		Authoring.file_chooser.setSelectedFile(testFile);
		File currentFile = Authoring.file_chooser.getSelectedFile();

		String testString = "Cell 1 \n Button 4 \n This is a test case \n";

		try {
			if (!currentFile.exists()) {
				currentFile.createNewFile();
			}

			FileWriter fw = new FileWriter(currentFile);
			fw.write(testString);
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(true, currentFile.exists());
	}

	@Test
	public void testCreateScenarios() {
		Authoring.scenarioReader = new JTextArea();
		Authoring.testCreateScenarios();
		assertEquals("Cell 1\nButton 4\n", Authoring.scenarioReader.getText());
	}

	@Test
	public void testAddNewEvent() {
		Authoring.btnAddUserInput = new JButton();
		Authoring.scenarioReader = new JTextArea();
		Authoring.addUserInputString();
		assertEquals("/~user-input\n", Authoring.scenarioReader.getText());
	}

	@Test
	public void testValidScenario() throws FileNotFoundException {
		File correctFormat = new File("FactoryScenarios/Scenario_1.txt");
		File wrongFormat = new File("FactoryScenarios/Scenario_wrong.txt");
		int count = 0;
		boolean result = false;
		Scanner scanner = new Scanner(wrongFormat);

		while (scanner.hasNextLine()) {
			// Check if first two lines follow the specific plan.
			String line = scanner.nextLine();
			if (count == 0) {
				if (line.startsWith("Cell")) {
					result = true;

				}
			}
			if (count == 1) {
				if (line.startsWith("Button")) {
					result = true;
				}
			}

		}
		scanner.close();
		assertEquals(result, false);

		scanner = new Scanner(correctFormat);
		while (scanner.hasNextLine()) {
			// Check if first two lines follow the specific plan.
			String line = scanner.nextLine();
			if (count == 0) {
				if (line.startsWith("Cell")) {
					result = true;

				}
			}
			if (count == 1) {
				if (line.startsWith("Button")) {
					result = true;
				}
			}

		}

		scanner.close();
		assertEquals(result, true);

	}

	@Test
	public void testAudioCreation() throws LineUnavailableException, InterruptedException {
		// Last Test
		AudioRecorder recorder = new AudioRecorder("tester1212", 1);
		recorder.start();
		File testFile = new File("FactoryScenarios/AudioFiles/tester1212.wav");
		assertEquals(true, testFile.exists());
	}

	// Tests the following scenario:
	/*
	 * A script was created using Authoring.java and exported as "MyExample.txt".
	 * This method tests to see if the Authoring app has created the correct script
	 * for the following commands: Cell:6, Button: 4, Display string "hello", Repeat
	 * button "2", Skip button "2" to "3", Pause for 5 seconds, Clear all cells
	 * "MyExample.txt" is then compared with the expected output
	 */
	@Test
	void testSavedFile() throws FileNotFoundException {

		Scanner in = new Scanner(new FileReader("FactoryScenarios/MyExample.txt"));
		StringBuilder sb = new StringBuilder();
		while (in.hasNext()) {
			sb.append(in.next());
		}
		in.close();
		String outString = sb.toString();

		String expected = "Cell6Button4/~disp-string:hello/~repeat-button:2/~skip-button:02/~skip:3/~pause:5/~disp-clearAll";

		assertEquals(expected, outString);

	}

}
