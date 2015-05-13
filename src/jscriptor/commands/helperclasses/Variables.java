package jscriptor.commands.helperclasses;

import java.util.ArrayList;
import java.util.List;

public class Variables {

	public List<String> variableKeys = new ArrayList<>();
	public List<Object> variables = new ArrayList<>();
	
	public void addVariable(String key, Object val) {
		removeVariable(key);
		variableKeys.add(key);
		variables.add(val);
	}
	
	public void removeVariable(String key) {
		int idx = variableKeys.indexOf(key.trim());
		
		if (idx >= 0) {
			variables.remove(idx);
			variableKeys.remove(idx);
		}
	}
	
	public void clearVariables() {
		variableKeys.clear();
		variables.clear();
	}
	
	public void executeCommand_SET(List<String> parameters) {
		for (String para : parameters) {
			if (para.trim().isEmpty())
				continue;
			String[] pars = para.split("=");
			List<String> paras = new ArrayList<>();
			paras.add(pars[0]);
			paras.add(pars[1]);
			executeCommand_DEF(paras);
		}
	}
	
	public void executeCommand_UNSET(List<String> parameters) {
		for (String s : parameters) removeVariable(s);
	}
	
	public void executeCommand_DEF(List<String> parameters) {
		// cut tail empty lines (multilines are handled different so it makes sense)
		int emptyTailLines = 1;
		for (int a = parameters.size() - 1; a > 0; --a) 
			if (parameters.get(a).trim().isEmpty()) emptyTailLines++;
		
		List<String> trimmedParas = new ArrayList<>();
		for (String par : parameters)
			trimmedParas.add(par.trim());
		
		addVariable(trimmedParas.get(0), trimmedParas.subList(1, parameters.size() - emptyTailLines));
	}
}
