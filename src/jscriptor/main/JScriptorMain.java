package jscriptor.main;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jscriptor.commands.helperclasses.CommandProcessor;

import com.solidleon.solidlogger.logging.Logging;

public class JScriptorMain {

	public static final String VERSION = "1.0";
	
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				Logging.exception(e);
			}
		});
		
		List<String> rbts = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			if ("--help".equals(args[i])) {
				try {
					List<String> lines = Files.readAllLines(Paths.get("help.txt"));
					for (String s : lines)
						Logging.info(s);
				} catch (Exception ex) {
					Logging.exception(ex);
				}
			} else if ("-g".equals(args[i])) {
				Logging.openLogWindow(true);
			} else {
				rbts.add(args[i]);
			}
		}
		
		Logging.info("JScriptor v" + VERSION);
		Logging.info("Original C# application written by Richard Rieder");
		Logging.info("Java port written by Markus Mannel");
		Logging.info("Run %s script(s)", rbts.size());

		for (String rbt : rbts) {
			try {
				CommandProcessor.runBatch(rbt);
			} catch (Exception e) {
				Logging.exception(e);
			}
		}
		
		Logging.info("Terminated");
	}
	
	public static void libMain(String file) throws Exception {
		Logging.info("JScriptor v" + VERSION);
		Logging.info("Original C# application written by Richard Rieder");
		Logging.info("Java port written by Markus Mannel");

		CommandProcessor.runBatch(file);
		
		Logging.info("Terminated");
		
	}

}
