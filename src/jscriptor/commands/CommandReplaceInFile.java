package jscriptor.commands;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

import com.solidleon.solidlogger.logging.Logging;

public class CommandReplaceInFile implements ICommand {

	@Override
	public void execute(List<String> parameters) throws Exception {
		for (String para : parameters) {
			Logging.info(para);
			String s1 = parameters.get(0).trim();
			String s2 = parameters.get(1).trim();
			
			
			for (int i = 2; i < parameters.size(); i++) {
				try {
					if (parameters.get(i).trim().isEmpty())
						continue;
					
					String sFile = new String(Files.readAllBytes(Paths.get(parameters.get(i))), Charset.defaultCharset());
					
					sFile = sFile.replaceAll(s1, s2);
					
					Files.write(Paths.get(parameters.get(i)), sFile.getBytes(Charset.defaultCharset()), StandardOpenOption.TRUNCATE_EXISTING);
					
				} catch (Exception ex) {
					throw ex;
				}
			}
			
		}
	}
	
}
