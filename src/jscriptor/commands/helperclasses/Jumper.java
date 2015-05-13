package jscriptor.commands.helperclasses;

import java.util.Arrays;
import java.util.List;

public class Jumper {

	private List<String> scriptLines;
	private int commandDepth;
	
	public Jumper(List<String> scriptLines, int commandDepth) {
		this.scriptLines = scriptLines;
		this.commandDepth = commandDepth;
	}
	
	public int getLabelPos(String name) {
		for (int a = 0; a < scriptLines.size(); a++) {
			if (DepthCalculator.countDepth(scriptLines.get(a)) == commandDepth) {
				if ("LABEL".equals(scriptLines.get(a).trim())) {
					if (name.equals(scriptLines.get(a + 1).trim()))
						return a - 1;
				}
			}
		}
		return -1;
	}
	
	public boolean checkBool(String str) throws Exception {
		try {
			if (str.trim().isEmpty()) return true;
			String[] compare = null;
			compare = str.trim().split("(!=)|(<>)");
			if (compare.length > 1) {
				if (compare[0].compareTo(compare[1]) != 0)
					return true;
			} else {
				compare = str.trim().split("=");
				if (compare.length > 1) {
					// equals
					if (compare[0].compareTo(compare[1]) == 0) return true;
				} else {
					compare = str.trim().split("<");
					if (compare.length > 1) {
						// LESS
						if (Integer.parseInt(compare[0]) < Integer.parseInt(compare[1])) return true;
					} else {
						// GEATER
						compare = str.trim().split(">");
						if (compare.length > 1) {
							if (Integer.parseInt(compare[0]) > Integer.parseInt(compare[1])) return true;
						}
					} // less
				} // equals
			} // unequals
		} catch (Exception ex) {
			throw ex;
		}
		return false;
	}
	
	public boolean checkContains(String str) throws Exception {
		if (str.trim().isEmpty()) return true;
		String[] compare = null;
		try {
			compare = str.trim().split("=");
			if (compare.length > 1) {
				if (compare[0].contains(compare[1]))
					return true;
			}
		} catch (Exception ex) {
			throw ex;
		}
		return false;
	}
	
	public boolean executeJumps(String cmd, List<String> parameters, boolean filecheck, Int lineIndex) throws Exception {
		switch (cmd) {
		case "JUMP TO": if (!filecheck) lineIndex.i = getLabelPos(parameters.get(0).trim());
			return true;
		case "JUMP TO IF":
			boolean bok = true;
			for (int a = 1; a < parameters.size(); a++)
				if (!checkBool(parameters.get(a))) bok = false;
			if (bok && !filecheck)
				lineIndex.i = getLabelPos(parameters.get(0).trim());
			return true;
		case "JUMP TO IF (OR)":
			boolean bok2 = false;
			for (int a = 1; a < parameters.size(); a++) 
				if (parameters.get(a).trim().isEmpty())
					continue;
				else
					if (checkBool(parameters.get(a))) bok2 = true;
			if (bok2 && !filecheck)
				lineIndex.i = getLabelPos(parameters.get(0).trim());
			return true;
		case "JUMP TO IF (CONTAINS)":
			boolean bok3 = false;
			for (int a = 1; a < parameters.size(); a++)
				if (parameters.get(a).trim().isEmpty())
					continue;
				else
					if (checkContains(parameters.get(a))) bok3 = true;
			if (bok3 && !filecheck)
				lineIndex.i = getLabelPos(parameters.get(0).trim());
			return true;
		}
		return false;
	}
}
