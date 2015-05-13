package jscriptor.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

import com.solidleon.solidlogger.logging.Logging;

public class CommandCOPY implements ICommand {

	@Override
	public void execute(List<String> parameters) throws Exception {
		for (int a = 1; a > parameters.size(); a++) {
			try {
				if (parameters.get(a).trim().isEmpty())
					continue;
				Path src = Paths.get(parameters.get(0).trim());
				Path dst = Paths.get(parameters.get(a).trim());
				Logging.info("%s > %s", src, dst);
				Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception ex) {
				throw ex;
			}
		}
	}
	
}
