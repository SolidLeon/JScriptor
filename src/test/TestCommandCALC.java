package test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jscriptor.commands.CommandCALC;
import jscriptor.commands.helperclasses.Variables;

import org.junit.Test;

public class TestCommandCALC {

	@Test
	public void testExecute() {
		Variables variables = new Variables();
		CommandCALC cmd = new CommandCALC(variables);
		
		List<String> parameters = new ArrayList<>();
		parameters.add("+");
		parameters.add("abc");
		parameters.add("1");
		parameters.add("2");
		parameters.add("3");
		
		try {
			cmd.execute(parameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> expected = new ArrayList<>();
		expected.add(new BigInteger("6").toString());
		assertEquals(expected, variables.variables.get(variables.variableKeys.indexOf("abc")));
	}
	

}
