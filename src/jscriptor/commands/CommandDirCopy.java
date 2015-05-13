package jscriptor.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jscriptor.commands.interfaces.ICommand;

import com.solidleon.solidlogger.logging.Logging;

public class CommandDirCopy implements ICommand {

	private CopyMode copyMode;
	
	
	
	public CommandDirCopy(CopyMode copyMode) {
		super();
		this.copyMode = copyMode;
	}

	@Override
	public void execute(List<String> parameters) throws Exception {
		for (int a = 1; a < parameters.size(); a++) {
			if (parameters.get(a).trim().isEmpty())
				continue;
			else
				copyDirectory(parameters.get(0), parameters.get(a), true);
		}
	}

	private void copyDirectory(String sourceDirName, String destDirName, boolean copySubDirs) throws Exception {
		try {
			File dir = new File(sourceDirName.trim());
			File[] dirs = dir.listFiles();
			
			checkSourceDirectory(dir);
			createTargetDirectoryIfMissing(destDirName);
			
			getDirectoryFilesAndCopyThem(destDirName, dir);
			
			if (copySubDirs) {
				for (File subdir : dirs) {
					File temppath = new File(destDirName.trim(), subdir.getName());
					copyDirectory(subdir.getAbsolutePath(), temppath.getAbsolutePath(), copySubDirs);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	private void getDirectoryFilesAndCopyThem(String destDirName, File dir) throws Exception {
		destDirName = destDirName.trim();
		for (File f : dir.listFiles()) {
			Logging.info("%s > %s", f.getAbsolutePath(), destDirName);
			File temppath = new File(destDirName, f.getName());
			if (copyMode == CopyMode.FAIL_IF_SOMETHING_EXISTS)
				Files.copy(f.toPath(), temppath.toPath());
			else if (copyMode == CopyMode.OVERWRITE_ALWAYS)
				Files.copy(f.toPath(), temppath.toPath(), StandardCopyOption.REPLACE_EXISTING);
			else
				CommandCopyNew.copyFileIfNewer(f, temppath);
		}
	}

	private void createTargetDirectoryIfMissing(String destDirName) {
		// TODO Auto-generated method stub
		
	}

	private void checkSourceDirectory(File dir) {
		// TODO Auto-generated method stub
		
	}
	
	public static enum CopyMode {
		FAIL_IF_SOMETHING_EXISTS,
		OVERWRITE_ALWAYS, OVERWRITE_IF_NEWER
	}

}
