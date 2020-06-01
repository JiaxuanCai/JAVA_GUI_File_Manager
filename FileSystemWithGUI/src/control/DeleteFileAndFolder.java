package control;

import java.io.File;

import javax.swing.JOptionPane;

/*	删除文件或目录
 *  delete直接接收待删除路径
 *  deleteFile和deleteFolder虽然定义成了静态，可以在类外单独调用，但是在这里我们把他们看成工具函数使用。
 */

public class DeleteFileAndFolder 
{	
	public static boolean delete(String fileName) 
	{
        File file = new File(fileName);
        if (!file.exists()) 
        {
        	JOptionPane.showMessageDialog(null,fileName+"不存在","删除失败",JOptionPane.WARNING_MESSAGE);  
            return false; //如果路径不存在，报错并返回假
        } 
        else 
        {
            if (file.isFile()) //判断路径类型并调用对应的工具函数
                return deleteFile(fileName);
            else
                return deleteFolder(fileName);
        }
    }
	
	public static boolean deleteFile(String fileName)  //删除文件
	{
        File file = new File(fileName);
        if (file.exists() && file.isFile())
        {
            if (file.delete())//简单地判断情形并调用file类的函数delete，根据情形输出提示并返回对应的布尔值
            {
            	JOptionPane.showMessageDialog(null,fileName,"删除文件成功",JOptionPane.WARNING_MESSAGE);  
                return true;
            } 
            else 
            {
            	JOptionPane.showMessageDialog(null,fileName,"删除文件失败",JOptionPane.WARNING_MESSAGE);  
                return false;
            }
        } 
        else
        {
        	JOptionPane.showMessageDialog(null,fileName +"不存在","删除文件失败",JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
	
	public static boolean deleteFolder(String dir) 
	{
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator; // 如果dir的结尾不是文件分隔符，则添加文件分隔符
        File dirFile = new File(dir);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) // 如果dir不存在，或者不是一个文件夹，则退出
        {
        	JOptionPane.showMessageDialog(null,dir+"不存在", "删除文件夹失败",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();// 删除文件夹中的所有文件和下属子文件夹
        for (int i = 0; i < files.length; i++) 
        {
            if (files[i].isFile()) // 删除子文件
            {
                flag = DeleteFileAndFolder.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            else if (files[i].isDirectory()) // 删除下属子文件夹
            {
                flag=DeleteFileAndFolder.deleteFolder(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) 
        {
        	JOptionPane.showMessageDialog(null,"删除文件夹失败","失败",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dirFile.delete()) {// 删除当前的空文件夹
        	JOptionPane.showMessageDialog(null,dir, "删除文件夹成功",JOptionPane.WARNING_MESSAGE);//最后输出提示
            return true;
        } 
        else 
        {
            return false;
        }
    }
}
