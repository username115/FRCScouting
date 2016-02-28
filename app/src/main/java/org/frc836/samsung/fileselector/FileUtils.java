/*
 * This class taken from the Samsung tutorial on File Dialogs.
 * http://developer.samsung.com/android/technical-docs/Implementing-a-file-selector-dialog
 * 
 * This is a modified version of the original tutorial file.
 */
package org.frc836.samsung.fileselector;

import java.io.File;

/**
 * A set of tools for file operations
 */
public class FileUtils {

	/** Filter which accepts every file */
	public static final String FILTER_ALLOW_ALL = "*.*";

	/** Filter which accepts only directories */
	public static final String FILTER_DIR_ONLY = "";

	/**
	 * This method checks that the file is accepted by the filter
	 * 
	 * @param file
	 *            - file that will be checked if there is a specific type
	 * @param filter
	 *            - criterion - the file type(for example ".jpg")
	 * @return true - if file meets the criterion - false otherwise.
	 */
	public static boolean accept(final File file, final String filter) {
		if (filter.compareTo(FILTER_ALLOW_ALL) == 0) {
			return true;
		}
		if (file.isDirectory()) {
			return true;
		}
		if (filter.compareTo(FILTER_DIR_ONLY) == 0) {
			return false;
		}
		int lastIndexOfPoint = file.getName().lastIndexOf('.');
		if (lastIndexOfPoint == -1) {
			return false;
		}
		String fileType = file.getName().substring(lastIndexOfPoint)
				.toLowerCase();
		return fileType.compareTo(filter) == 0;
	}
}
