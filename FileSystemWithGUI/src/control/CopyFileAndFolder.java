package control;

import java.io.File;

import javax.swing.JOptionPane;

/*	�����ļ��л��ļ�����Ӧ�������ܿ��С���������ѡ��
 *  init: ��ʼ����ĳ�Ա������ʹ�ô��ڳ�ʼ״̬��Ŀ����ÿ�����ճ��֮�����ó�Ա��������ÿ�θ��Ƶ����ݾ���һ��ճ��֮��ʧЧ��ģ��Windows���ļ�����ϵͳ��
 *  generateDir: ����ѡ�е�·�����ɿ���ʱʹ�õ���Դ·��������ճ����ʱ��ʹ�á�
 */

public class CopyFileAndFolder {
	public static int dirtype; //ָʾ���Ƶ����ݵ����ͣ��ļ����ļ��У�
	public static CopyFileAndFolder _inst; //ǰ�ڷ�������Ϊ�Ǿ�̬ʱ��debugʹ�õ������ʵ����Ϊ��Ա����
	public static String dir;//���Ƶ���Դ·��
	public static String fName;//�ļ�����ʵ���ƣ�ȥ��·����
	
	public static void init()//��ʼ��
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
			File tempFile =new File( dir.trim());//����ļ���ʵ����
			fName = tempFile.getName();
		}
		if(file.isDirectory()) {
			dirtype=2;
			File tempFile =new File( dir.trim());//����ļ���ʵ����
			fName = tempFile.getName();
		}
			
		
//		JOptionPane.showMessageDialog(null,"����","����Ϊ"+dirtype+" ��Ϊ"+fName,JOptionPane.WARNING_MESSAGE);
	}
}
