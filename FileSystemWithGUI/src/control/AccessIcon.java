package control;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.io.*;
import java.util.*;

/*	获得当前路径的文件名或文件夹对应的小图标，以及当前路径（如果是文件夹）所有下属文件对应的小图标用于文件树的渲染
 *  getSingleIcon: 获得当前路径的文件或文件夹的小图标
 *  getAllIcon: 获得当前路径（如果当前路径是文件夹）下的所有下属文件或文件夹的小图标
 */

public class AccessIcon {
	public static Icon getSingleIcon(String path)
	{
		FileSystemView fview=FileSystemView.getFileSystemView(); //使用包装器FileSystemView调用系统方法获得系统的小图标，不用自己绘制
		File f= new File(path);
		Icon i=fview.getSystemIcon(f); //活用File类和FIleSystemView类获得图标
		return i;
	}
	
	public static Icon[] getAllIcon(String path)
	{
		Icon[] icons=new Icon[1000000]; //数组开大一点防止路径下文件很多，结束类方法调用后自动清理
		int i=0;
		if(path=="HOME")
		{
			List<String> Disks=DirectoryHelp.findDisk();
			for(int j=0;i<Disks.size();j++)
			{
				FileSystemView fsv = FileSystemView.getFileSystemView(); //同上面获得单个图标的方法
				File file = new File(Disks.get(j) + "\\"); //根目录
				icons[i++]=fsv.getSystemIcon(file); //遍历所有文件（夹）
			}
		}
		else
		{
			File file = new File(path); //使用包装器类File活用获得下属所有文件路径，便于后期获得图标
			File[] files = file.listFiles(); 
			for(File a:files) 
			{ 
				if(a!=null && a.exists()) 
				{ 
					FileSystemView fsv= FileSystemView.getFileSystemView(); //同上面获得单个图标的方法
					icons[i++]=fsv.getSystemIcon(a);
				} 
			} 
		} 
		return icons; 
	}
}
