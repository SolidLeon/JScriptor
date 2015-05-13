package jscriptor.commands;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

public class CommandWriteTextFile implements ICommand {

	@Override
	public void execute(List<String> parameters) throws Exception {
		String s1 = parameters.get(0);
		Path p1 = Paths.get(s1);
		if (Files.exists(p1)) throw new RuntimeException("file does already exist");
		try {
			Files.write(p1, parameters, Charset.defaultCharset(), StandardOpenOption.APPEND);
		} catch (Exception ex) {
			throw ex;
		}
	}
}
