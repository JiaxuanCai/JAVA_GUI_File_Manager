package control;

import java.io.File;

import javax.swing.JOptionPane;

/*	拷贝文件夹或文件，对应弹出功能框中“拷贝”的选项
 *  init: 初始化类的成员变量，使得处于初始状态。目的是每次完成粘贴之后重置成员变量，即每次复制的内容经过一次粘贴之后失效，模仿Windows的文件管理系统。
 *  generateDir: 根据选中的路径生成拷贝时使用的来源路径，方便粘贴的时候使用。
 */

public class CopyFileAndFolder {
	public static int dirtype; //指示复制的内容的类型（文件或文件夹）
	public static CopyFileAndFolder _inst; //前期方法定义为非静态时期debug使用的自身的实例作为成员变量
	public static String dir;//复制的来源路径
	public static String fName;//文件的真实名称（去除路径）
	
	public static void init()//初始化
	{
		dirtype=0;
		dir="";
	}
	
	public static void generateDir(String dir1)
	{
		File file=new File(dir1);
		dir=dir1;
		if(file.isFile()) {
			dirtype=1;
			File tempFile =new File( dir.trim());//获得文件真实名称
			fName = tempFile.getName();
		}
		if(file.isDirectory()) {
			dirtype=2;
			File tempFile =new File( dir.trim());//获得文件真实名称
			fName = tempFile.getName();
		}
			
		
//		JOptionPane.showMessageDialog(null,"测试","类型为"+dirtype+" 名为"+fName,JOptionPane.WARNING_MESSAGE);
	}
}
