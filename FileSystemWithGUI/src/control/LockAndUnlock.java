package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*	���ļ����м��ܺͽ��ܵĲ���
 *  encryptFile: ��õ�ǰ�ļ���·�����ļ������ļ��е�·�����Լ���Կ
 *  ��Կ������: ����ʱ��Ϊ�����������û������趨������ʱ���ڻ�ԭ����ʱ�������û�������ȷ��Կʱ���ܱ�����
 */

public class LockAndUnlock
{
	public static void encryptFile(String fileDir , String folderDir , String key)
	{
        FileInputStream in = null; //������
        FileOutputStream out = null; //�����
        int keyword=Integer.parseInt(key); //��Կת��Ϊ����������֮��ļ��ܽ�������
        File file=new File(fileDir); 
        File desfile=new File(folderDir+"\\temp");
        try {
            String sourceFileUrl = fileDir;
            String targetFileUrl = folderDir+"\\temp";
            in = new FileInputStream(sourceFileUrl);
            out = new FileOutputStream(targetFileUrl);
            int data = 0;
            while ((data=in.read())!=-1){
                //����ȡ�����ֽ������һ�������������
                out.write(data^keyword); //Դ�ļ���������Կ�������������мӽ���
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //��finally�йرտ�������
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
