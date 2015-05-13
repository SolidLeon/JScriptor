package jscriptor.commands;

import java.io.File;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

public class CommandEXEC implements ICommand {

	@Override
	public void execute(List<String> parameters) throws Exception {
		String pars = "";
		if (parameters.size() > 2) {
			for (int a = 2; a < parameters.size(); a++) {
				if (parameters.get(a).trim().isEmpty())
					continue;
				pars += " ";
				pars += parameters.get(a).trim();
			}
		}
		
		ProcessBuilder clientProcess = new ProcessBuilder(parameters.get(0).trim(), pars);
		clientProcess.directory(new File(parameters.get(1).trim()));
		Process p = clientProcess.start();
		int code = p.waitFor();
	}

	
}
