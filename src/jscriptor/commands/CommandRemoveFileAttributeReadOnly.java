package jscriptor.commands;

import java.io.File;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

import com.solidleon.solidlogger.logging.Logging;

public class CommandRemoveFileAttributeReadOnly implements ICommand {

	public void removeFlagFromFile(File f) {
		Logging.info("Removing readonly flag (if set) for: %s", f.getAbsolutePath());
		if (f.canWrite()) {
			f.setWritable(true);
		}
	}
	
	public void removeFlag(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory())
				removeFlag(f);
			else 
				removeFlagFromFile(f);
		}
	}
	
	@Override
	public void execute(List<String> parameters) throws Exception {
		for (String para : parameters) {
			if (para.trim().isEmpty())
				continue;
			File file = new File(para.trim());
			if (file.isDirectory()) {
				removeFlag(file);
			} else {
				removeFlagFromFile(file);
			}
		}
	}

}
