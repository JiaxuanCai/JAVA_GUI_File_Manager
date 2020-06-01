package control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/*	ѹ���ͽ�ѹ�ļ�
 *  zip:ѹ���ļ�
 *  unzip:��ѹ�ļ�
 */

public class ZipAndUnzip {
	private static int BUFFERSIZE = 2 << 10; //������
	private static String basePath;
	public static void zipDirectory(String path) throws IOException {
        File file = new File(path);
        String parent = file.getParent();
        File zipFile = new File(parent, file.getName() + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zip(zos, file, file.getName());
        JOptionPane.showMessageDialog(null,"ѹ���ɹ�","ѹ���ɹ�",JOptionPane.WARNING_MESSAGE);  
        zos.flush();
        zos.close();
    }
	
	private static void zip(ZipOutputStream zos, File file, String path) throws IOException {
        // �����ж����ļ��������ļ��У��ļ�ֱ��д��Ŀ¼����㣬�ļ��������
        if (file.isDirectory()) {
            ZipEntry entry = new ZipEntry(path + File.separator);// �ļ��е�Ŀ¼�������������Ʒָ�����β
            zos.putNextEntry(entry);
            File[] files = file.listFiles();
            for (File x : files) {
                zip(zos, x, path + File.separator + x.getName());
            }
        } else {
            FileInputStream fis = new FileInputStream(file);// Ŀ¼�������������ļ���ѹ���ļ��е�·��
            ZipEntry entry = new ZipEntry(path);
            zos.putNextEntry(entry);// ����һ��Ŀ¼�����

            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.flush();
            fis.close();
            zos.closeEntry();// �رյ�ǰĿ¼����㣬���������ƶ���һ��Ŀ¼�����
        }
    }
	
	public static void unzip(String fileName, String path)  
    {  
        FileOutputStream fos = null;  
        InputStream is = null;  
        JOptionPane.showMessageDialog(null,"��ѹ�ɹ�","��ѹ�ɹ�",JOptionPane.WARNING_MESSAGE);  
    }
}
