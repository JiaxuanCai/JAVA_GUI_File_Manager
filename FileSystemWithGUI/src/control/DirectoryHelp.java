package control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*	遍历大写字母A-Z获得盘符
 *  findDisk通过遍历获得路径所在的盘符
 *  
 */

public class DirectoryHelp 
{
	public static List<String> findDisk() 
	{  	  	  
        List<String> list = new ArrayList<String>(); //数据结构选择List 因为要反复删减操作
        for (char c = 'A'; c <= 'Z'; c++) {  //遍历A-Z看当前路径所在的盘符是哪一个
            String dirName = c + ":";  
            File win = new File(dirName);  
            if (win.exists()) {  
                String str = c + ":";  
                list.add(str);  //得到盘符之后组成完整路径
            }  
        }  
        return list;  
    }  
}
