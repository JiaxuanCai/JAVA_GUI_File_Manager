package control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*	������д��ĸA-Z����̷�
 *  findDiskͨ���������·�����ڵ��̷�
 *  
 */

public class DirectoryHelp 
{
	public static List<String> findDisk() 
	{  	  	  
        List<String> list = new ArrayList<String>(); //���ݽṹѡ��List ��ΪҪ����ɾ������
        for (char c = 'A'; c <= 'Z'; c++) {  //����A-Z����ǰ·�����ڵ��̷�����һ��
            String dirName = c + ":";  
            File win = new File(dirName);  
            if (win.exists()) {  
                String str = c + ":";  
                list.add(str);  //�õ��̷�֮���������·��
            }  
        }  
        return list;  
    }  
}
