package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*	ճ���Ѿ����Ƶ����ݣ������Ѹ������ݵ����͵���pasteFile����pasteFolder
 *  pasteFile: ճ���ļ�
 *  pasteFolder : ճ���ļ���
 */

public class PasteFileAndFolder {
	
	public static void pasteFile(String fromDir , String toDir) throws IOException
	{
		FileInputStream in = new FileInputStream(fromDir);//��Ŀ��·����Դ·�����������������������ʹ��
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
			//�ж�ԴĿ¼�ǲ���һ��Ŀ¼
			if (!dirSouce.isDirectory()) {
				//�������Ŀ¼�ǾͲ�����
				return;
			}
			//����Ŀ��Ŀ¼��File����
			File destDir = new File(toDir);	
			//���Ŀ��Ŀ¼������
			if(!destDir.exists()){
				//����Ŀ��Ŀ¼
				destDir.mkdir();
			}
			//��ȡԴĿ¼�µ�File�����б�
			File[]files = dirSouce.listFiles();
			for (File file : files) {
				//ƴ���µ�fromDir(fromFile)��toDir(toFile)��·��
				String strFrom = fromDir + File.separator + file.getName();
				System.out.println(strFrom);
				String strTo = toDir + File.separator + file.getName();
				System.out.println(strTo);
				//�ж�File������Ŀ¼�����ļ�
				//�ж��Ƿ���Ŀ¼
				if (file.isDirectory()) {
					//�ݹ���ø���Ŀ¼�ķ���
					pasteFolder(strFrom,strTo);
				}
				//�ж��Ƿ����ļ�
				if (file.isFile()) {
					System.out.println("���ڸ����ļ���"+file.getName());
					//�ݹ���ø����ļ��ķ���
					pasteFile(strFrom,strTo);
				}
			}
		}

	
}
