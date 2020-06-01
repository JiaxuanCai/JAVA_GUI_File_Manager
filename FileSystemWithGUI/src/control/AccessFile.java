package control;

import java.io.*;
import java.util.*;

/*	获得当前路径的文件名或文件夹名以及当前路径下的（如果当前路径是文件夹）的所有下属文件名
 *  getSingleName: 获得当前路径的文件或文件夹的名称
 *  getAllName: 获得当前路径（如果当前路径是文件夹）下的所有下属文件或文件夹的名字
 */

public class AccessFile 
{
	public static String[] getSingleName(String path)
	{
		File file = new File (path);//使用包装器File获得文件路径的文件名
		String[] fileName=file.list();
		return fileName;
	}
	
	public static void getAllName(String path, ArrayList<String> fileName)
	{
		File file = new File(path);
        File [] files = file.listFiles();
        String [] names = file.list();
        if(names != null)
        	fileName.addAll(Arrays.asList(names));//确保不为空
        for(File a:files)
        {
            if(a.isDirectory())
            {
                getAllName(a.getAbsolutePath(),fileName);//得到文件的绝对路径
            }
        }
	}
}
