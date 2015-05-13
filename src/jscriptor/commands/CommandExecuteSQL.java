package jscriptor.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

public class CommandExecuteSQL implements ICommand {

	public void executeSQL(String connectionString, String sql) throws SQLException {
		try (Connection conn = DriverManager.getConnection(connectionString)) {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.execute();
		}
	}
	
	@Override
	public void execute(List<String> parameters) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		for (int a = 1; a < parameters.size(); a++) 
			sb.append("\r\n").append(parameters.get(a));
		
		executeSQL(parameters.get(0), sb.toString());
	}

}
