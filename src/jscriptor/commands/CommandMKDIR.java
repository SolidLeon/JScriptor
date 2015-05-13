package jscriptor.commands;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

public class CommandMKDIR implements ICommand {

	@Override
	public void execute(List<String> parameters) throws Exception {
		for (String para : parameters) {
			if (para.trim().isEmpty()) 
				continue;
			else
				Files.createDirectory(Paths.get(para));
		}
	}

}
