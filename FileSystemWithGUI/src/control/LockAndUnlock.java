package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*	对文件进行加密和解密的操作
 *  encryptFile: 获得当前文件的路径和文件所在文件夹的路径，以及秘钥
 *  秘钥的作用: 加密时作为运算数，由用户自行设定；解密时用于还原计算时，仅当用户输入正确秘钥时才能被解密
 */

public class LockAndUnlock
{
	public static void encryptFile(String fileDir , String folderDir , String key)
	{
        FileInputStream in = null; //输入流
        FileOutputStream out = null; //输出流
        int keyword=Integer.parseInt(key); //秘钥转换为整数，用于之后的加密解密运算
        File file=new File(fileDir); 
        File desfile=new File(folderDir+"\\temp");
        try {
            String sourceFileUrl = fileDir;
            String targetFileUrl = folderDir+"\\temp";
            in = new FileInputStream(sourceFileUrl);
            out = new FileOutputStream(targetFileUrl);
            int data = 0;
            while ((data=in.read())!=-1){
                //将读取到的字节异或上一个数，加密输出
                out.write(data^keyword); //源文件数据与秘钥进行异或操作进行加解密
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //在finally中关闭开启的流
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        file.delete();
        desfile.renameTo(new File(fileDir));
	}

}
