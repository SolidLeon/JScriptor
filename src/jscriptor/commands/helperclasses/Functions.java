package jscriptor.commands.helperclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Functions {

	private Map<String, String[]> functions = new HashMap<>();
	
	public void createFunction(List<String> parameters) {
		String functionName = parameters.get(0).trim();
		
		String[] dummy;
		
		if ((dummy = functions.get(functionName)) == null) {
			List<String> funParams = new ArrayList<>();
			for (int a = 1; a < parameters.size(); a++)
				funParams.add(parameters.get(a));
			functions.put(functionName, funParams.toArray(new String[0]));
		} else {
			//Logging.info("Already exists");
		}
	}
	
	public boolean callFunction(String command, List<String> parameters, boolean filecheck) throws Exception {
		String[] lines = null;
		if ((lines = functions.get(command.trim())) == null)
			return false;
		List<String> linesWithParams = new ArrayList<>();
		
		linesWithParams.add(lines[0].toLowerCase().replaceAll("param", "DEF"));
		linesWithParams.add(lines[1]);
		
		int depth = DepthCalculator.countDepth(lines[1]);
		for (String para : parameters) {
			String spar = para.trim();
			linesWithParams.add(padLeft(spar, spar.length() + depth, ' '));
		}
		linesWithParams.addAll(Arrays.asList(lines));
		CommandProcessor.executeLinesWithNewProcess(linesWithParams.toArray(new String[0]), filecheck);
		return true;
	}

	private String padLeft(String spar, int totalLength, char c) {
		StringBuilder result = new StringBuilder(spar);
		int toInsert = spar.length() - totalLength;
		if (toInsert > 0) {
			for (int i = 0; i < toInsert; i++)
				result.insert(0, c);
		}
		return result.toString();
	}
	
}
