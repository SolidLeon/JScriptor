package jscriptor.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

import com.solidleon.solidlogger.logging.Logging;

public class CommandCopyNew implements ICommand {

	private boolean ignoreErrors = false;
	
	public CommandCopyNew(boolean ignoreErrors) {
		this.ignoreErrors = ignoreErrors;
	}
	
	@Override
	public void execute(List<String> parameters) throws Exception {
		try {
			List<String> extensions = Arrays.asList(parameters.get(0).trim().split(","));
			File dirFrom = new File(parameters.get(1).trim());
			if (!dirFrom.exists())
				throw new RuntimeException("directory from does not exist '" + parameters.get(1).trim() + "' -> '" + dirFrom.getAbsolutePath() + "'");
			
			for (int b = 2; b < parameters.size(); b++) {
				if (parameters.get(b).trim().isEmpty())
					continue;
				File dirTo = new File(parameters.get(b).trim());
				if (!dirTo.exists())
					throw new RuntimeException("directory to does not exist");
				
				for (File f : dirFrom.listFiles()) {
					try {
						String[] parts = f.getName().split("\\.");
						if ((extensions.contains(parts[parts.length - 1]))) {
							String s1 = parameters.get(1).trim();
							String s2 = parameters.get(b).trim();
							String s3 = f.getAbsolutePath();
							String toFile = s3.replace(s1, s2);
							File f2 = new File(toFile);
							Logging.info("%d>%d", f.lastModified(), f2.lastModified());
							Logging.info("%s > %s", f.getAbsolutePath(), f2.getAbsolutePath());
							copyFileIfNewer(f, f2);
						}
					} catch (Exception ex) {
						throw ex;
					}
				}
			}
		} catch (Exception ex) {
			if (!ignoreErrors)
				throw ex;
		}
	}

	public static void copyFileIfNewer(File f, File f2) {
		try {
			if (f.lastModified() > f2.lastModified()) {
				Logging.info("COPY : " + f.getAbsolutePath() + " > " + f2.getAbsolutePath());
				Files.copy(f.toPath(), f2.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} else {
				Logging.info("no copy");
			}
		} catch (Exception ex) {
			Logging.info("Copy failed : %s", ex.getMessage());
		}
	}

}
