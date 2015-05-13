package jscriptor.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jscriptor.commands.helperclasses.Variables;
import jscriptor.commands.interfaces.ICommand;

public class CommandCALC implements ICommand {

	Variables variables = null;
	
	public CommandCALC(Variables variables) {
		this.variables = variables;
	}
	
	@Override
	public void execute(List<String> parameters) throws Exception {
		String what = parameters.get(0).trim();
		String resultVar = parameters.get(1).trim();
		
		// startwert
		BigInteger result = null;
		try {
			result = new BigInteger(parameters.get(2));
		} catch (Exception ex) {
			result = new BigInteger("0");
		}
		
		//restliche werte auf start wert/last result anwenden
		for (int a = 3; a < parameters.size(); a++) {
			try {
				if (parameters.get(a).trim().isEmpty()) 
					continue;
				
				BigInteger bi = null;
				try {
					bi = new BigInteger(parameters.get(a));
				} catch (Exception ex) {
					bi = new BigInteger("0");
				}
				
				switch (what) {
				case "+":
					result = result.add(bi);
					break;
				case "-":
					result = result.subtract(bi);
					break;
				case "*":
					result = result.multiply(bi);
					break;
				case "/":
					result = result.divide(bi);
					break;
				}
			} catch (Exception ex) {
				throw ex;
			}
			
			List<String> res = new ArrayList<String>();
			res.add(result.toString());
			variables.addVariable(resultVar, res);
		}
	}

}
