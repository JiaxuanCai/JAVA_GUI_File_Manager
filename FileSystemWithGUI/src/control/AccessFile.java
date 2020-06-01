package control;

import java.io.*;
import java.util.*;

/*	��õ�ǰ·�����ļ������ļ������Լ���ǰ·���µģ������ǰ·�����ļ��У������������ļ���
 *  getSingleName: ��õ�ǰ·�����ļ����ļ��е�����
 *  getAllName: ��õ�ǰ·���������ǰ·�����ļ��У��µ����������ļ����ļ��е�����
 */

public class AccessFile 
{
	public static String[] getSingleName(String path)
	{
		File file = new File (path);//ʹ�ð�װ��File����ļ�·�����ļ���
		String[] fileName=file.list();
		return fileName;
	}
	
	public static void getAllName(String path, ArrayList<String> fileName)
	{
		File file = new File(path);
        File [] files = file.listFiles();
        String [] names = file.list();
        if(names != null)
        	fileName.addAll(Arrays.asList(names));//ȷ����Ϊ��
        for(File a:files)
        {
            if(a.isDirectory())
            {
                getAllName(a.getAbsolutePath(),fileName);//�õ��ļ��ľ���·��
            }
        }
	}
}
