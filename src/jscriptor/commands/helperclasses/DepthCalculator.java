package jscriptor.commands.helperclasses;

public class DepthCalculator {

	public static int countDepth(String line) {
		return line.length() - trimStart(line).length();
	}
	
	public static String trimStart(String s) {
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) != ' ' && s.charAt(i) != '\t')
				return s.substring(i);
		return s;
	}
	
}
