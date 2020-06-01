package control;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.io.*;
import java.util.*;

/*	��õ�ǰ·�����ļ������ļ��ж�Ӧ��Сͼ�꣬�Լ���ǰ·����������ļ��У����������ļ���Ӧ��Сͼ�������ļ�������Ⱦ
 *  getSingleIcon: ��õ�ǰ·�����ļ����ļ��е�Сͼ��
 *  getAllIcon: ��õ�ǰ·���������ǰ·�����ļ��У��µ����������ļ����ļ��е�Сͼ��
 */

public class AccessIcon {
	public static Icon getSingleIcon(String path)
	{
		FileSystemView fview=FileSystemView.getFileSystemView(); //ʹ�ð�װ��FileSystemView����ϵͳ�������ϵͳ��Сͼ�꣬�����Լ�����
		File f= new File(path);
		Icon i=fview.getSystemIcon(f); //����File���FIleSystemView����ͼ��
		return i;
	}
	
	public static Icon[] getAllIcon(String path)
	{
		Icon[] icons=new Icon[1000000]; //���鿪��һ���ֹ·�����ļ��ܶ࣬�����෽�����ú��Զ�����
		int i=0;
		if(path=="HOME")
		{
			List<String> Disks=DirectoryHelp.findDisk();
			for(int j=0;i<Disks.size();j++)
			{
				FileSystemView fsv = FileSystemView.getFileSystemView(); //ͬ�����õ���ͼ��ķ���
				File file = new File(Disks.get(j) + "\\"); //��Ŀ¼
				icons[i++]=fsv.getSystemIcon(file); //���������ļ����У�
			}
		}
		else
		{
			File file = new File(path); //ʹ�ð�װ����File���û�����������ļ�·�������ں��ڻ��ͼ��
			File[] files = file.listFiles(); 
			for(File a:files) 
			{ 
				if(a!=null && a.exists()) 
				{ 
					FileSystemView fsv= FileSystemView.getFileSystemView(); //ͬ�����õ���ͼ��ķ���
					icons[i++]=fsv.getSystemIcon(a);
				} 
			} 
		} 
		return icons; 
	}
}
