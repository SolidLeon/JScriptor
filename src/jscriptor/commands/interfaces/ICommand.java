package jscriptor.commands.interfaces;

import java.util.List;

public interface ICommand {

	void execute(List<String> parameters) throws Exception;
	
}
