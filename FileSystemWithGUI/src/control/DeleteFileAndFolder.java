package control;

import java.io.File;

import javax.swing.JOptionPane;

/*	ɾ���ļ���Ŀ¼
 *  deleteֱ�ӽ��մ�ɾ��·��
 *  deleteFile��deleteFolder��Ȼ������˾�̬�����������ⵥ�����ã��������������ǰ����ǿ��ɹ��ߺ���ʹ�á�
 */

public class DeleteFileAndFolder 
{	
	public static boolean delete(String fileName) 
	{
        File file = new File(fileName);
        if (!file.exists()) 
        {
        	JOptionPane.showMessageDialog(null,fileName+"������","ɾ��ʧ��",JOptionPane.WARNING_MESSAGE);  
            return false; //���·�������ڣ��������ؼ�
        } 
        else 
        {
            if (file.isFile()) //�ж�·�����Ͳ����ö�Ӧ�Ĺ��ߺ���
                return deleteFile(fileName);
            else
                return deleteFolder(fileName);
        }
    }
	
	public static boolean deleteFile(String fileName)  //ɾ���ļ�
	{
        File file = new File(fileName);
        if (file.exists() && file.isFile())
        {
            if (file.delete())//�򵥵��ж����β�����file��ĺ���delete���������������ʾ�����ض�Ӧ�Ĳ���ֵ
            {
            	JOptionPane.showMessageDialog(null,fileName,"ɾ���ļ��ɹ�",JOptionPane.WARNING_MESSAGE);  
                return true;
            } 
            else 
            {
            	JOptionPane.showMessageDialog(null,fileName,"ɾ���ļ�ʧ��",JOptionPane.WARNING_MESSAGE);  
                return false;
            }
        } 
        else
        {
        	JOptionPane.showMessageDialog(null,fileName +"������","ɾ���ļ�ʧ��",JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
	
	public static boolean deleteFolder(String dir) 
	{
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator; // ���dir�Ľ�β�����ļ��ָ�����������ļ��ָ���
        File dirFile = new File(dir);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) // ���dir�����ڣ����߲���һ���ļ��У����˳�
        {
        	JOptionPane.showMessageDialog(null,dir+"������", "ɾ���ļ���ʧ��",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();// ɾ���ļ����е������ļ����������ļ���
        for (int i = 0; i < files.length; i++) 
        {
            if (files[i].isFile()) // ɾ�����ļ�
            {
                flag = DeleteFileAndFolder.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            else if (files[i].isDirectory()) // ɾ���������ļ���
            {
                flag=DeleteFileAndFolder.deleteFolder(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) 
        {
        	JOptionPane.showMessageDialog(null,"ɾ���ļ���ʧ��","ʧ��",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dirFile.delete()) {// ɾ����ǰ�Ŀ��ļ���
        	JOptionPane.showMessageDialog(null,dir, "ɾ���ļ��гɹ�",JOptionPane.WARNING_MESSAGE);//��������ʾ
            return true;
        } 
        else 
        {
            return false;
        }
    }
}
