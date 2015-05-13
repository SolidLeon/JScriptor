package jscriptor.commands.helperclasses;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jscriptor.commands.CommandCALC;
import jscriptor.commands.CommandCOPY;
import jscriptor.commands.CommandCopyNew;
import jscriptor.commands.CommandDirCopy;
import jscriptor.commands.CommandECHO;
import jscriptor.commands.CommandECHO_PAUSED;
import jscriptor.commands.CommandEXEC;
import jscriptor.commands.CommandEXECUTE_SQL;
import jscriptor.commands.CommandMKDIR;
import jscriptor.commands.CommandRemoveFileAttributeReadOnly;
import jscriptor.commands.CommandReplaceInFile;
import jscriptor.commands.CommandWriteTextFile;
import jscriptor.commands.interfaces.ICommand;

import com.solidleon.solidlogger.logging.Logging;

public class CommandProcessor {

	private Statistic statistic = new Statistic();
	private Jumper jumper;
	
	private int commandDepth = 0;
	private List<String> scriptLines = new ArrayList<>();
	
	private Variables variables = new Variables();
	private Functions functions = new Functions();
	
	private int tabSize = 4;
	private String tabReplace;
	
	/// from SKIP to SKIP END
	private boolean skipping = false;

	public void setTabReplace(String tabReplace) {
		this.tabReplace = tabReplace;
	}

	public void setJumper(Jumper jumper) {
		this.jumper = jumper;
	}

	public String getTabReplace() {
		if (tabReplace == null) {
			tabReplace = "";
			for (int a = 0; a < tabSize; a++)
				tabReplace += " ";
		}
		return tabReplace;
	}
	
	public Jumper getJumper() {
		if (jumper == null) {
			jumper = new Jumper(scriptLines, commandDepth);
		}
		return jumper;
	}
	
	private boolean executeCommand(String command, List<String> parameters, boolean filecheck, Int lineIndex) throws Exception {
		if (command.isEmpty())
			return true;
		
		try {
			ICommand ci = null;
			
			Logging.info("----------------------------------------------------------------");
			Logging.info(lineIndex.i + " COMMAND : " + command);
			for (String para : parameters)
				Logging.info("    " + para);
			
			String cmd = command.trim().toUpperCase();
			
			if (this.skipping && !"SKIP END".equals(cmd))
				return true;
			
			if (!getJumper().executeJumps(cmd, parameters, filecheck, lineIndex)) {
				switch (cmd) {
				case "CALC": ci = new CommandCALC(variables); ci.execute(parameters); break;
				case "COPY": ci = new CommandCOPY(); if (!filecheck) ci.execute(parameters); break;
                case "COPY NEW": ci = new CommandCopyNew(false); if (!filecheck) ci.execute(parameters); break;
                case "COPY NEW IGNORE ERRORS": ci = new CommandCopyNew(true); if (filecheck == false) ci.execute(parameters); break;
//                case "CREATE DB SQL": ci = new Command_CREATE_SQL_DB(); if (filecheck == false) ci.execute(parameters); break;
//                case "CREATE ODBC SYSTEM DSN": ci = new Command_CREATE_ODBC_SYSTEM_DSN(); if (filecheck == false) ci.execute(parameters); break;
//                case "CREATE SQL DB": ci = new Command_CREATE_SQL_DB(); if (filecheck == false) ci.execute(parameters); break; //doublicate
                case "DEF": this.variables.executeCommand_DEF(parameters); break;
                case "DIRCOPY": ci = new CommandDirCopy(CommandDirCopy.CopyMode.FAIL_IF_SOMETHING_EXISTS); if (filecheck == false) ci.execute(parameters); break;
                case "DIRCOPY ONLY NEW": ci = new CommandDirCopy(CommandDirCopy.CopyMode.OVERWRITE_IF_NEWER); if (filecheck == false) ci.execute(parameters); break;
                case "DIRCOPY OVERWRITE": ci = new CommandDirCopy(CommandDirCopy.CopyMode.OVERWRITE_ALWAYS); if (filecheck == false) ci.execute(parameters); break;
                case "DOCU": /*nop();*/ break;
                case "ECHO": ci = new CommandECHO(); if (filecheck == false) ci.execute(parameters); break;
                case "ECHO PAUSED": ci = new CommandECHO_PAUSED(); if (filecheck == false) ci.execute(parameters); break;
                case "EXEC": ci = new CommandEXEC(); if (filecheck == false) ci.execute(parameters); break;
                case "EXECUTE SQL": ci = new CommandEXECUTE_SQL(); if (filecheck == false) ci.execute(parameters); break;
                case "EXIT": if (filecheck == false) throw new CommandExitException(); break;
                case "FUNC": this.functions.createFunction(parameters); break;
                case "LABEL": /* nop("label load is be done seperate");*/ break; //neues goto traget eintragen //TODO make doublicates save
                case "MKDIR": ci = new CommandMKDIR(); if (filecheck == false) ci.execute(parameters); break;
                case "NOP": /*nop();*/ break;
                case "PARAM": /*nop("rest von function call der nicht sauber removed wurde");*/ break;
                case "PARAMS": /*nop("rest von function call der nicht sauber removed wurde");*/ break;
                case "PAUSE": if (filecheck == false) System.in.read(); break;
                case "REM": /*nop();*/ break;
                case "REMOVE FILE ATTRIBUTE READ ONLY": ci = new CommandRemoveFileAttributeReadOnly(); if (filecheck == false) ci.execute(parameters); break;
                case "REPLACE IN FILE": ci = new CommandReplaceInFile(); if (filecheck == false) ci.execute(parameters); break;
                case "REPLACE IN FILES": ci = new CommandReplaceInFile(); if (filecheck == false) ci.execute(parameters); break; //doublicate
                case "SET": this.variables.executeCommand_SET(parameters); break;
                case "SETA": this.variables.executeCommand_DEF(parameters); break;
                case "SKIP": this.skipping = true; ; break; //skipps all lines until SKIP END
                case "SKIP END": this.skipping = false; ; break; //skipps all lines until SKIP END                   
                case "TODO": /*nop();*/ break; //maybe echo magenta ?
                case "UNSET": this.variables.executeCommand_UNSET(parameters); break;
                case "WRITE TEXT FILE": ci = new CommandWriteTextFile(); if (filecheck == false) ci.execute(parameters); break;
				default:
					if (!functions.callFunction(command, parameters, filecheck))
						throw new Exception("unknown command");
					break;
				}
			}
		} catch (CommandExitException ex) {
			Logging.info("EXIT : %s", ex.getMessage());
			return false;
		} catch (Exception ex) {
			if (filecheck)
				throw ex;
			else
				Logging.exception(ex);
		}
		parameters.clear();
		return true;
	}
	
	private String perpareLine(String command, String line, Int cursor, Int endPos) {
		line = line.replace("\t", getTabReplace());
		for (int i = 0; i < variables.variableKeys.size(); i++) {
			try {
				List<String> varLines = (List<String>) variables.variables.get(i);
				if (varLines.size() == 1) {
					if (!"UNSET".equals(command.trim()))
						line = line.replace(variables.variableKeys.get(i), varLines.get(0));
				} else {
					if (!"DEF".equals(command.trim())) {
						if (variables.variableKeys.get(i).equals(line.trim())) {
							scriptLines.remove(cursor.i);
							endPos.i -= 1;
							
							int add = 0;
							for (String sline : varLines) {
								scriptLines.add(cursor.i + add, sline);
								++add;
								endPos.i += 1;
							}
						}
					}
				}
			} catch (Exception ex) {
				throw ex;
			}
		}
		return line;
	}
	
	public void executeLines(List<String> linesIn, boolean filecheck) throws Exception {
		this.scriptLines = linesIn;
		
		int depth = 0;									//depth of current line
		int lastDepth = 0;								//depth of last line
		List<String> parameters = new ArrayList<>();	//all lines since last command
		String command = "";							//last command (parameters follow afterwards)
		
		Int endPos = new Int();
		endPos.i = scriptLines.size();
		
		for (Int cursor = new Int(); cursor.i < endPos.i; cursor.i += 1) {
			String line = perpareLine(command, scriptLines.get(cursor.i), cursor, endPos);
			depth = DepthCalculator.countDepth(line);
			if (depth > commandDepth) {
				parameters.add(line);
			} else {
				if (depth == commandDepth) {
					if (line.trim().isEmpty()) // can not be a new command (maybe empty line) 
						parameters.add(line); // assume as parameter
					else {
						int tempcursor = cursor.i;
						if (!executeCommand(command, parameters, filecheck, cursor))
							break; //EXIT / ERROR
						else 
							if (tempcursor != cursor.i) { //cursor changed, go on with new cursor
								command = "";
								continue;
							}
						command = line; // save new one
					}
				}
			}
			lastDepth = depth;
		}
	}
	
	private void calculateCommandDepth(List<String> lines) {
		for (int a = 0; a < 100; a++) {
			for (String line : lines) {
				try {
					if (line.trim().isEmpty())
						continue;
					else 
						if (line.length() > a) {
							String l = line.toUpperCase();
							if (l.charAt(a) >= 'A' && l.charAt(a) <= 'Z') {
								commandDepth = a; 
								return;
							}
						} else 
							continue;
				} catch (Exception ex) {
					//NOP
				}
			}
		}
	}

	public static void executeLinesWithNewProcess(String[] linesIn, boolean filecheck) throws Exception {
		List<String> lines = new ArrayList<>();
		lines.addAll(Arrays.asList(linesIn));
		CommandProcessor cp = new CommandProcessor();
		cp.calculateCommandDepth(lines);
		cp.executeLines(lines, filecheck); //check for script errors and preload labels with true , execute script with false
	}
	
	public static void runBatch(String filename) throws Exception {
		Path path = Paths.get(filename);
		Logging.info("Run '%s' ...", path);
		Logging.info("Read '%s' with charset '%s'", path, Charset.defaultCharset());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		executeLinesWithNewProcess(lines.toArray(new String[0]), true); // error check (optional)
		executeLinesWithNewProcess(lines.toArray(new String[0]), false); // real run
	}
	
	
}
