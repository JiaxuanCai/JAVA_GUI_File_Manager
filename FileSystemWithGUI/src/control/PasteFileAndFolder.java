package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*	粘贴已经复制的内容，根据已复制内容的类型调用pasteFile或是pasteFolder
 *  pasteFile: 粘贴文件
 *  pasteFolder : 粘贴文件夹
 */

public class PasteFileAndFolder {
	
	public static void pasteFile(String fromDir , String toDir) throws IOException
	{
		FileInputStream in = new FileInputStream(fromDir);//用目标路径和源路径构建输出输入流，供后续使用
		FileOutputStream out = new FileOutputStream(toDir);
		byte[] bs = new byte[1*1024*1024];	
		int count = 0;
		while((count = in.read(bs))!=-1){ 
			out.write(bs,0,count);
		}
		in.close();
		out.flush();
		out.close();
	}
	
	public static void pasteFolder(String fromDir,String toDir) throws IOException
	{
			File dirSouce = new File(fromDir);
			//判断源目录是不是一个目录
			if (!dirSouce.isDirectory()) {
				//如果不是目录那就不复制
				return;
			}
			//创建目标目录的File对象
			File destDir = new File(toDir);	
			//如果目的目录不存在
			if(!destDir.exists()){
				//创建目的目录
				destDir.mkdir();
			}
			//获取源目录下的File对象列表
			File[]files = dirSouce.listFiles();
			for (File file : files) {
				//拼接新的fromDir(fromFile)和toDir(toFile)的路径
				String strFrom = fromDir + File.separator + file.getName();
				System.out.println(strFrom);
				String strTo = toDir + File.separator + file.getName();
				System.out.println(strTo);
				//判断File对象是目录还是文件
				//判断是否是目录
				if (file.isDirectory()) {
					//递归调用复制目录的方法
					pasteFolder(strFrom,strTo);
				}
				//判断是否是文件
				if (file.isFile()) {
					System.out.println("正在复制文件："+file.getName());
					//递归调用复制文件的方法
					pasteFile(strFrom,strTo);
				}
			}
		}

	
}
