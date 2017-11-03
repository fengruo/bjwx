package com.common.util;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.util.Enumeration;

import org.apache.tools.zip.*;

/**
 * 
 * 功能： 1 、实现把指定文件夹下的所有文件压缩为指定文件夹下指定 zip 文件 2 、实现把指定文件夹下的 zip 文件解压到指定目录下
 * 
 * 
 * 
 * @author ffshi
 * 
 * 
 */

public class ZipUtils {

	public static void main(String[] args) {

		// 把 E 盘正则表达式文件夹下的所有文件压缩到 E 盘 stu 目录下，压缩后的文件名保存为 正则表达式 .zip

		// zip ("E:// 正则表达式 ", "E://stu // 正则表达式 .zip ");

		// 把 E 盘 stu 目录下的正则表达式 .zip 压缩文件内的所有文件解压到 E 盘 stu 目录下面

		//unZip("F://javasoftware//tomcat7//webapps//testPool//uploadfile//2013060912173839.zip",
			//	"F://javasoftware//tomcat7//webapps//testPool//uploadfile//12");
		//zip("E://temp","E://temp.zip");

	}
	public void makeZipFile(String zipfile,String forFolder){
		unZip(zipfile,forFolder);
	}

	public void changeFolderZip(String sourceDir, String zipFile){
		zip(sourceDir,zipFile);
	}
	public static void zip(String sourceDir, String zipFile) {

		OutputStream os;

		try {

			os = new FileOutputStream(zipFile);

			BufferedOutputStream bos = new BufferedOutputStream(os);

			ZipOutputStream zos = new ZipOutputStream(bos);

			File file = new File(sourceDir);

			String basePath = null;

			if (file.isDirectory()) {

				basePath = file.getPath();

			} else {

				basePath = file.getParent();

			}

			zipFile(file, basePath, zos);

			zos.closeEntry();

			zos.close();

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	private static void zipFile(File source, String basePath,

	ZipOutputStream zos) {

		File[] files = new File[0];

		if (source.isDirectory()) {

			files = source.listFiles();

		} else {

			files = new File[1];

			files[0] = source;

		}

		String pathName;

		byte[] buf = new byte[1024];

		int length = 0;

		try {

			for (File file : files) {

				if (file.isDirectory()) {

					pathName = file.getPath().substring(basePath.length() + 1)

					+ "/";

					zos.putNextEntry(new ZipEntry(pathName));

					zipFile(file, basePath, zos);

				} else {

					pathName = file.getPath().substring(basePath.length() + 1);

					InputStream is = new FileInputStream(file);

					BufferedInputStream bis = new BufferedInputStream(is);

					zos.putNextEntry(new ZipEntry(pathName));

					while ((length = bis.read(buf)) > 0) {

						zos.write(buf, 0, length);

					}

					is.close();

				}

			}

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public static void unZip(String zipfile, String destDir) {

		destDir = destDir.endsWith("//") ? destDir : destDir + "//";

		byte b[] = new byte[1024];

		int length;

		ZipFile zipFile;

		try {

			zipFile = new ZipFile(new File(zipfile), "gbk");
			Enumeration enumeration = zipFile.getEntries();

			ZipEntry zipEntry = null;

			while (enumeration.hasMoreElements()) {

				zipEntry = (ZipEntry) enumeration.nextElement();

				String name = zipEntry.getName();
				File loadFile = new File(destDir + File.separator + name);

				if (zipEntry.isDirectory()) {

					// 这段都可以不要，因为每次都貌似从最底层开始遍历的

					loadFile.mkdirs();

				} else {

					if (!loadFile.getParentFile().exists())

						loadFile.getParentFile().mkdirs();

					OutputStream outputStream = new FileOutputStream(loadFile);

					InputStream inputStream = zipFile.getInputStream(zipEntry);

					while ((length = inputStream.read(b)) > 0)

						outputStream.write(b, 0, length);					
					
					outputStream.flush();
					outputStream.close();
					inputStream.close();
				}

			}

			System.out.println(" 文件解压成功 ");

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

}
