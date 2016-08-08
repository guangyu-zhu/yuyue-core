package com.yuyue.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamUtils {

	private static Log	log	= LogFactory.getLog(StreamUtils.class);

	private StreamUtils() {
		// NON-INSTANCEABLE.
	}

	public static boolean createFolder(String folderPath) {
		boolean fileExisted = false;
		boolean createIsSuccess = false;

		File file = new File(folderPath);
		fileExisted = file.exists();
		if (!fileExisted) {
			createIsSuccess = file.mkdirs();// create folder along with all
											// neccessary parent folder
			file = null;
			return createIsSuccess;
		} else {
			file = null;
			return true;
		}
	}

	public static String getFileContent(String path) {
		StringBuilder webContent = new StringBuilder();
		try (InputStream is = new FileInputStream(new File(path)); InputStreamReader isr = new InputStreamReader(is, "utf-8"); BufferedReader in = new BufferedReader(isr);) {
			String thisLine = null;
			while ((thisLine = in.readLine()) != null) {
				webContent.append(thisLine);
				webContent.append("\n");
			}
			return webContent.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean createFile(InputStream is, String filePath) {
		try (FileOutputStream fos = new FileOutputStream(filePath);) {
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
			return true;
		} catch (Exception e) {

			// ignore here.
		}
		return false;
	}

	public static List<File> getFileList(String path) {
		File file = new File(path);
		File[] fileArray = file.listFiles();
		if (fileArray != null && fileArray.length > 0) {
			List<File> list = Arrays.asList(fileArray);
			Collections.sort(list, new Comparator<File>() {

				public int compare(File f1, File f2) {
					long lastModified1 = f1.lastModified();
					long lastModified2 = f2.lastModified();
					if (lastModified1 > lastModified2) {
						return -1;
					} else if (lastModified1 < lastModified2) {
						return 1;
					} else {
						return 0;
					}
				}

			});
			return list;
		} else {
			return null;
		}
	}

	public static boolean copyFile(String srcPath, String descPath) {
		try (FileInputStream fis = new FileInputStream(srcPath); FileOutputStream fos = new FileOutputStream(descPath);) {
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = fis.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return true;
	}

	public static void writeFileContent(String fileFullPath, String content) {
		try (FileWriter fw = new FileWriter(fileFullPath); PrintWriter pw = new PrintWriter(fw);) {

			pw.println(content);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static boolean checkFileContent(String fileFullPath, String code) {
		File f = new File(fileFullPath);
		try (FileInputStream fis = new FileInputStream(f); InputStreamReader reader = new InputStreamReader(fis); BufferedReader dr = new BufferedReader(reader);) {
			String line = null;
			while ((line = dr.readLine()) != null) {
				if (line.contains(code)) {
					return true;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	public static void appendFileContent(String fileFullPath, String content) {
		try (OutputStream fos = new FileOutputStream(fileFullPath, true); PrintWriter pw = new PrintWriter(fos);) {
			pw.append(content);
			pw.flush();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		deleteFile(file);
		return true;
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (!file.exists()) {
			return flag;
		} else {
			return deleteDirectory(file);
		}
	}
	
	public static boolean deleteDirectory(String path) {
		File file = new File(path);
		return deleteDirectory(file);
	}

	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static boolean renameTo(String source, String dest) {
		File file1 = new File(source);
		if (file1.exists()) {
			File file2 = new File(dest);
			return file1.renameTo(file2);
		} else {
			return false;
		}
	}

	public static void clearFolder(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] fs = file.listFiles();
			if (fs != null) {
				for (File f : fs) {
					f.delete();
				}
			}
		}
	}

	public static List<File> getLatestFiles(String path, String[] downloadCategorys, int size) {
		List<File> rsList = new ArrayList<File>();
		List<File> tmpList = new ArrayList<File>();
		if (downloadCategorys != null && downloadCategorys.length > 0) {
			for (int i = 0; i < downloadCategorys.length; i++) {
				String category = downloadCategorys[i];
				File file = new File(path + File.separator + category);
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					if (files != null && files.length > 0) {
						for (File ff : files) {
							File[] dlFiles = ff.listFiles();
							List<File> list = Arrays.asList(dlFiles);
							tmpList.addAll(list);
						}
					}
				}
			}
		}
		if (tmpList.size() > 0) {
			Collections.sort(tmpList, new Comparator<File>() {

				public int compare(File f1, File f2) {
					long time1 = f1.lastModified();
					long time2 = f2.lastModified();
					if (time1 > time2) {
						return -1;
					} else if (time1 < time2) {
						return 1;
					} else {
						return 0;
					}
				}

			});
			int index = 0;
			for (File f : tmpList) {
				if (index >= size) {
					break;
				}
				rsList.add(f);
				index++;
			}
		}
		return rsList;
	}

}
