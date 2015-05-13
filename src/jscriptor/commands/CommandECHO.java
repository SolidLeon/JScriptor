package jscriptor.commands;

import java.util.List;

import jscriptor.commands.interfaces.ICommand;

import com.solidleon.solidlogger.logging.Logging;

public class CommandECHO implements ICommand {

	@Override
	public void execute(List<String> parameters) throws Exception {
		for (String para : parameters) {
			Logging.info(para);
		}
	}

}
