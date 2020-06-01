package view;
import control.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import model.*;

/*	�ļ�����ϵͳ��������
 *  ����MVC��ƽṹ�е�view����
 *  �е����û������Ľ��������
 */

public class MainFrame extends JFrame implements ActionListener{
	public static MainFrame _instance; //�������蹹�����ֱ�ӷ��������̬��ʵ��
	JPanel ShowPanel,TreePanel;//��״�ļ������ļ���ʾ���
	FileTree filesTree;//�Զ������ݽṹ�ļ���
	JScrollPane ScrollShow, TreeShow;//�������Ĺ�����
    DefaultMutableTreeNode node;
    public String Cur_URL = "";//��ǰ·��
	public Map<String, String> Maps = new HashMap<String,String>();
	//�ļ��б����ر���
	JList<String> list;
	public DefaultListModel defaultListModel;
	public Stack<String> stack, stack_return;
	JPopupMenu jPopupMenu = null; //�����ļ�/�ļ��е㿪���б�
	JPopupMenu jPopupMenu2 = null; //���̵㿪���б�
	JPopupMenu jPopupMenu3 = null; //����ļ�/�ļ��е㿪���б�
	JPopupMenu jPopupMenu4 = null; //zipѹ���ļ��㿪���б�
	JMenuItem[] JMIs = new JMenuItem[10]; //�б�1��ѡ��
	JMenuItem[] JMIs2 = new JMenuItem[5]; //�б�2��
	JMenuItem delete = new JMenuItem("ɾ��"); 
	JMenuItem delete2 = new JMenuItem("ɾ��");
	JMenuItem zip=new JMenuItem("ѹ��");
	JMenuItem unzip=new JMenuItem("��ѹ");
	public Icon[] AllIcons = new Icon[999999];//�洢�����õ����ļ�ͼ��
	public int Icon_Counter = 0;
	
	public MainFrame(){//������
		this._instance = this; //���蹹������
		this.setTitle("FileManager_WithGUI_Author@Caijiaxuan");
		this.setSize(1000,600);
		this.getContentPane().setLayout(null);
		Init();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
	}
	
	public void Init(){
		ShowPanel = new JPanel();
		TreePanel = new JPanel();
		//�в��ļ��б�
        stack = new Stack<String>();
        stack_return = new Stack<String>();
        ShowPanel.setSize(800, 610);
        ShowPanel.setLocation(190, 10);
        ShowPanel.setLayout(null);    
        list = new JList<String>();
        jPopupMenu = new JPopupMenu();//�ļ�/�ļ��е����Բ˵�
        jPopupMenu2 = new JPopupMenu();//���̵����Բ˵�
        JMIs[0] = new JMenuItem("��");
        JMIs[1] = new JMenuItem("ɾ��");
        JMIs[2] = new JMenuItem("������");
        JMIs[3] = new JMenuItem("�½��ļ���");
        JMIs[4] = new JMenuItem("����");
        JMIs[5] = new JMenuItem("ճ��");
        JMIs[6] = new JMenuItem("����/����");
        JMIs[7] = new JMenuItem("ѹ��");
        for(int k = 0; k < 8; ++k){//�ļ�/�ļ��е����Բ˵���ʼ��
        	JMIs[k].addActionListener(this);
        	jPopupMenu.add(JMIs[k]);            	
        }
        
        
        JMIs2[0] = new JMenuItem("��");
        for(int k = 0; k < 1; ++k){//���̵����Բ˵���ʼ��
        	JMIs2[k].addActionListener(this);
        	jPopupMenu2.add(JMIs2[k]);            	
        }    
        jPopupMenu3 = new JPopupMenu();
        delete.addActionListener(this);
        jPopupMenu3.add(delete);
        zip.addActionListener(this);
        jPopupMenu3.add(zip);
        
        jPopupMenu4 = new JPopupMenu();
        delete2.addActionListener(this);
        jPopupMenu4.add(delete2);
        unzip.addActionListener(this);
        jPopupMenu4.add(unzip);
        
        list.add(jPopupMenu4);
        list.add(jPopupMenu3);
        list.add(jPopupMenu2);
        list.add(jPopupMenu);
        
        Home_List();//��ʾ���̸�Ŀ¼
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(list.getSelectedIndex() != -1){
					if(e.getClickCount() == 1){//����listʱ�������¼�
					
					}else if(e.getClickCount() == 2){//˫��listʱ�����ļ���������Ŀ¼
						System.out.println(list.getSelectedValue());
						twoClick(list.getSelectedValue());												
					}
					if(e.getButton() == 3){//�һ�listʱ���򿪲˵���
						if(Cur_URL != ""){
							if(list.getSelectedValue().endsWith("zip"))
							{
								jPopupMenu4.show(list,e.getX(),e.getY());
							}
							else if(list.getSelectedValuesList().size() == 1){
								jPopupMenu.show(list,e.getX(),e.getY()); //����һ����ǵ����ļ��к��ļ�����Ӧ��һ��������ȫ�Ĳ˵���
							}else if(list.getSelectedValuesList().size() > 1){//���ѡ�ж���ļ��к��ļ�����ֻ֧��ɾ����ѹ������
								jPopupMenu3.show(list, e.getX(), e.getY());
							}
						}		                 
						else if(Cur_URL == "" && list.getSelectedValuesList().size() == 1){
							jPopupMenu2.show(list, e.getX(), e.getY()); //����һ����Ǵ��̣��˵�����ֻ���С��򿪡��͡����ԡ�����
						}
						
					}
				}
			}
		});	
	        
		ScrollShow = new JScrollPane(list);
		ShowPanel.add(ScrollShow);
		ScrollShow.setSize(790, 520);
		ScrollShow.setLocation(5, 5);
		this.add(ShowPanel);
		
		//���Ŀ¼��״ͼ
        TreePanel.setSize(190,610);
        TreePanel.setLocation(5, 10);
        TreePanel.setLayout(null); 
        filesTree = new FileTree();
        TreeShow = new JScrollPane(filesTree);
        TreeShow.setBounds(5, 5, 185, 520);
        TreePanel.add(TreeShow);
        this.add(TreePanel);                   	
	}
	
	public void twoClick(String choice){//�������ʱ���¼�
			choice += "\\";		
			File file = new File(Cur_URL + choice);
			if(file.isDirectory()){
				Cur_URL += choice;	
				stack.push(Cur_URL);
				Go_There();
			}
			else
			{
				OpenIt(file);
			}
	}
	
	public void Home_List(){//�ص���ʼ���̽���
		List<String> Disks = DirectoryHelp.findDisk();
		defaultListModel = new DefaultListModel();
		for(int i = 0; i < Disks.size(); ++i){
			defaultListModel.addElement(Disks.get(i));
		}
		Icon[] icons = AccessIcon.getAllIcon("HOME");//�õ���Ŀ¼�µ�ͼ��
		list.setModel(defaultListModel);
		list.setCellRenderer(new MyCellRenderer(icons));
		Cur_URL = "";
		stack.push(Cur_URL);
	}
	
	public void OpenIt(File file){//���õ����еĳ��򡰴򿪡��ļ��ķ���
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void Go_There(){//��ȥ�ģ���ȥ�ģ�������ת������
			if(Cur_URL != ""){//Cur_URL�ǿգ�������Ŀ��Ŀ¼
				defaultListModel.clear();
				String[] getString = AccessFile.getSingleName(Cur_URL);		
				for(int i = 0; i < getString.length; ++i){
					defaultListModel.addElement(getString[i]);		
				}	
				Icon[] icons = AccessIcon.getAllIcon(Cur_URL);
				list.setModel(defaultListModel);
				list.setCellRenderer(new MyCellRenderer(icons));
				
			}else{//Cur_URLΪ��ʱ������ת�ظ�Ŀ¼
				Home_List();
			}
	}	
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        MainFrame m = new MainFrame();     
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == JMIs[0] || e.getSource() == JMIs2[0]){	//���ļ�/�ļ���/����
			String url = Cur_URL + list.getSelectedValue();
			if(Cur_URL != ""){
				url += "\\";
			}
				File file = new File(url);
			if(file.isDirectory()){
				twoClick(url);
			}else{
				OpenIt(file);				
			}			
		}
		
		else if(e.getSource() == JMIs[1]){//ɾ�������ļ�/�ļ���
			File file = new File(Cur_URL + "/" + list.getSelectedValue());
			int n;
			if(file.isFile()){
				n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ���ļ� " + file.getName() + " ô?", "�ļ�ɾ��",JOptionPane.YES_NO_OPTION);
			}else{
				n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ�� " + file.getName() + " ����Ŀ¼�µ��ļ�ô?", "�ļ���ɾ��",JOptionPane.YES_NO_OPTION);
			}
			if(n == 0){
				DeleteFileAndFolder.delete(Cur_URL + list.getSelectedValue() +  "\\");
				Go_There();
			}			
		}
		
		else if(e.getSource() == delete2){//ɾ��ѹ����
			File file = new File(Cur_URL + "/" + list.getSelectedValue());
			int n;
			n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ���ļ� " + file.getName() + " ô?", "�ļ�ɾ��",JOptionPane.YES_NO_OPTION);
			if(n == 0){
				DeleteFileAndFolder.delete(Cur_URL + list.getSelectedValue() +  "\\");
				Go_There();
			}			
		}
		
		
		
		else if(e.getSource() == delete){//��ѡ�µ�ɾ��
			List<String> selected_str = list.getSelectedValuesList();
			File file;
			int num = selected_str.size();
			int n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ�� " + selected_str.get(0) + " ��" + num + "��ô?", "�ļ�ɾ��",JOptionPane.YES_NO_OPTION);
			if(n == 0){
					for(int i = 0; i < selected_str.size(); ++i){
						DeleteFileAndFolder.delete(Cur_URL + selected_str.get(i) +  "\\");
					}		
					Go_There();
				}
			}						
		
		else if(e.getSource() == JMIs[2]){//������
			String before = list.getSelectedValue();
			File file = new File(Cur_URL + before + "\\");
			String after = "";
			if(file.isDirectory()){
				after = (String) JOptionPane.showInputDialog(null, "���������ļ�����:\n", "������", JOptionPane.PLAIN_MESSAGE, null, null,
		                list.getSelectedValue());
			}else{
				after = (String) JOptionPane.showInputDialog(null, "���������ļ���:\n", "������", JOptionPane.PLAIN_MESSAGE, null, null,
		                list.getSelectedValue());
			}			
			if(before != after && after != null){
				new File(Cur_URL + before + "\\").renameTo(new File(Cur_URL + after + "\\"));
				Go_There();//ˢ���б�
			}else{
				Go_There();
			}
		}
		
		else if(e.getSource() == JMIs[3]){//�½��ļ���
			String name = JOptionPane.showInputDialog("�����½��ļ��е����� ");
			String newdir=Cur_URL + "\\"+name;
			File file=new File(newdir);
			if(!file.exists()){
				file.mkdir();
			}
			JOptionPane.showMessageDialog(null,"�����ļ��гɹ�","�����ļ��гɹ�",JOptionPane.WARNING_MESSAGE);  
			Go_There();
		}
		
		else if(e.getSource() == JMIs[4]){//����
			CopyFileAndFolder.init();
			CopyFileAndFolder.generateDir(Cur_URL + "\\" + list.getSelectedValue());
		}
		
		else if(e.getSource() == JMIs[5])  {//ճ��
			String fromDir=CopyFileAndFolder.dir;
			if(CopyFileAndFolder.dirtype==1) { //�ж�����Ϊ�ļ� ���ö�Ӧ����
				String toDir=Cur_URL+"\\"+CopyFileAndFolder.fName;
				File file=new File(toDir);
				if(!file.exists())
				{
					try {file.createNewFile();}  //�׳��쳣
					catch (IOException e1) {}
				}
				try {PasteFileAndFolder.pasteFile(fromDir, toDir);} 
				catch (IOException e1) { }
				Go_There();
			}
			
			if(CopyFileAndFolder.dirtype==2) { //�ж�����Ϊ�ļ��У����ö�Ӧ����
				String toDir=Cur_URL+"\\"+CopyFileAndFolder.fName;
				try { PasteFileAndFolder.pasteFolder(fromDir, toDir);} 
				catch (IOException e1) {}
			}
			Go_There();
		}
		
		else if(e.getSource() == JMIs[6]){//���ܽ���
			String dir=Cur_URL + "\\" + list.getSelectedValue();
			String key = JOptionPane.showInputDialog("������Կ ");
			LockAndUnlock.encryptFile(dir, Cur_URL, key);
			JOptionPane.showMessageDialog(null,"����/���ܳɹ�","����/���ܳɹ�",JOptionPane.WARNING_MESSAGE);  
			Go_There();
		}
		
		else if(e.getSource()==JMIs[7])//�����ļ����ļ��е�ѹ��
		{
			String dir=Cur_URL + "\\" + list.getSelectedValue();
			try {
				ZipAndUnzip.zipDirectory(dir);//ֱ�ӵ���ѹ����������Ҫע���׳��쳣
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Go_There();
		}
		
		else if(e.getSource()==zip)//��ѡ�ļ����ļ��е�ѹ��
		{
			List<String> selected_str = list.getSelectedValuesList();
			
			String zfname = JOptionPane.showInputDialog("����ѹ���ļ�����");
			String zfolderURL= Cur_URL+"\\"+zfname;//���ȸ����û������ѹ���ļ����ƣ�����ͬ���ļ��У�������ѡ�е����ݴ����ļ���
			File zfolder=new File(zfolderURL);
			if(!zfolder.exists())
				zfolder.mkdir();
			
			for(int i = 0; i < selected_str.size(); ++i){//����ļ��������е��ļ�
				CopyFileAndFolder.init();
				CopyFileAndFolder.generateDir(Cur_URL + "\\" + selected_str.get(i));
				String fromDir=CopyFileAndFolder.dir;
				if(CopyFileAndFolder.dirtype==1) {
					String toDir=zfolderURL+"\\"+CopyFileAndFolder.fName;
					File file=new File(toDir);
					if(!file.exists())
					{
						try {file.createNewFile();} 
						catch (IOException e1) {}
					}
					try {PasteFileAndFolder.pasteFile(fromDir, toDir);} 
					catch (IOException e1) { }
				}
				
				if(CopyFileAndFolder.dirtype==2) {//ճ������
					String toDir=zfolderURL+"\\"+CopyFileAndFolder.fName;
					try { PasteFileAndFolder.pasteFolder(fromDir, toDir);} 
					catch (IOException e1) {}
				}
			}
			
			try { //��Ϊ�Ѿ��������ļ��У�ֱ�ӵ��ú���ѹ������ļ��м���
				ZipAndUnzip.zipDirectory(zfolderURL);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null,"ѹ���ɹ�","ѹ���ɹ�",JOptionPane.WARNING_MESSAGE);  
			DeleteFileAndFolder.delete(zfolderURL);//ɾ���ļ���
			Go_There();
		}
		
		else if (e.getSource()==unzip)//��ѹ���ļ����н�ѹ
		{
			String dir=Cur_URL + "\\" + list.getSelectedValue();
			try {//ֱ�ӵ��ý�ѹ����
				ZipAndUnzip.unzip(dir,Cur_URL);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Go_There();
		}
	}
}

