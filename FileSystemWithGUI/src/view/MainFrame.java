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

/*	文件操作系统的主界面
 *  属于MVC设计结构中的view部分
 *  承担与用户交互的界面的任务
 */

public class MainFrame extends JFrame implements ActionListener{
	public static MainFrame _instance; //类外无需构造对象，直接访问这个静态的实例
	JPanel ShowPanel,TreePanel;//树状文件面板和文件显示面板
	FileTree filesTree;//自定义数据结构文件树
	JScrollPane ScrollShow, TreeShow;//两个面板的滚动条
    DefaultMutableTreeNode node;
    public String Cur_URL = "";//当前路径
	public Map<String, String> Maps = new HashMap<String,String>();
	//文件列表的相关变量
	JList<String> list;
	public DefaultListModel defaultListModel;
	public Stack<String> stack, stack_return;
	JPopupMenu jPopupMenu = null; //单个文件/文件夹点开的列表
	JPopupMenu jPopupMenu2 = null; //磁盘点开的列表
	JPopupMenu jPopupMenu3 = null; //多个文件/文件夹点开的列表
	JPopupMenu jPopupMenu4 = null; //zip压缩文件点开的列表
	JMenuItem[] JMIs = new JMenuItem[10]; //列表1的选项
	JMenuItem[] JMIs2 = new JMenuItem[5]; //列表2的
	JMenuItem delete = new JMenuItem("删除"); 
	JMenuItem delete2 = new JMenuItem("删除");
	JMenuItem zip=new JMenuItem("压缩");
	JMenuItem unzip=new JMenuItem("解压");
	public Icon[] AllIcons = new Icon[999999];//存储搜索得到的文件图标
	public int Icon_Counter = 0;
	
	public MainFrame(){//主界面
		this._instance = this; //无需构建对象
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
		//中部文件列表
        stack = new Stack<String>();
        stack_return = new Stack<String>();
        ShowPanel.setSize(800, 610);
        ShowPanel.setLocation(190, 10);
        ShowPanel.setLayout(null);    
        list = new JList<String>();
        jPopupMenu = new JPopupMenu();//文件/文件夹的属性菜单
        jPopupMenu2 = new JPopupMenu();//磁盘的属性菜单
        JMIs[0] = new JMenuItem("打开");
        JMIs[1] = new JMenuItem("删除");
        JMIs[2] = new JMenuItem("重命名");
        JMIs[3] = new JMenuItem("新建文件夹");
        JMIs[4] = new JMenuItem("拷贝");
        JMIs[5] = new JMenuItem("粘贴");
        JMIs[6] = new JMenuItem("加密/解密");
        JMIs[7] = new JMenuItem("压缩");
        for(int k = 0; k < 8; ++k){//文件/文件夹的属性菜单初始化
        	JMIs[k].addActionListener(this);
        	jPopupMenu.add(JMIs[k]);            	
        }
        
        
        JMIs2[0] = new JMenuItem("打开");
        for(int k = 0; k < 1; ++k){//磁盘的属性菜单初始化
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
        
        Home_List();//显示磁盘根目录
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(list.getSelectedIndex() != -1){
					if(e.getClickCount() == 1){//单击list时，暂无事件
					
					}else if(e.getClickCount() == 2){//双击list时，打开文件或进入该子目录
						System.out.println(list.getSelectedValue());
						twoClick(list.getSelectedValue());												
					}
					if(e.getButton() == 3){//右击list时，打开菜单栏
						if(Cur_URL != ""){
							if(list.getSelectedValue().endsWith("zip"))
							{
								jPopupMenu4.show(list,e.getX(),e.getY());
							}
							else if(list.getSelectedValuesList().size() == 1){
								jPopupMenu.show(list,e.getX(),e.getY()); //如果右击的是单个文件夹和文件，则应打开一个功能齐全的菜单栏
							}else if(list.getSelectedValuesList().size() > 1){//如果选中多个文件夹和文件，则只支持删除和压缩功能
								jPopupMenu3.show(list, e.getX(), e.getY());
							}
						}		                 
						else if(Cur_URL == "" && list.getSelectedValuesList().size() == 1){
							jPopupMenu2.show(list, e.getX(), e.getY()); //如果右击的是磁盘，菜单栏中只含有“打开”和“属性”功能
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
		
		//左侧目录树状图
        TreePanel.setSize(190,610);
        TreePanel.setLocation(5, 10);
        TreePanel.setLayout(null); 
        filesTree = new FileTree();
        TreeShow = new JScrollPane(filesTree);
        TreeShow.setBounds(5, 5, 185, 520);
        TreePanel.add(TreeShow);
        this.add(TreePanel);                   	
	}
	
	public void twoClick(String choice){//点击两次时的事件
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
	
	public void Home_List(){//回到初始磁盘界面
		List<String> Disks = DirectoryHelp.findDisk();
		defaultListModel = new DefaultListModel();
		for(int i = 0; i < Disks.size(); ++i){
			defaultListModel.addElement(Disks.get(i));
		}
		Icon[] icons = AccessIcon.getAllIcon("HOME");//得到根目录下的图标
		list.setModel(defaultListModel);
		list.setCellRenderer(new MyCellRenderer(icons));
		Cur_URL = "";
		stack.push(Cur_URL);
	}
	
	public void OpenIt(File file){//调用电脑中的程序“打开”文件的方法
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void Go_There(){//想去哪，就去哪（核心跳转函数）
			if(Cur_URL != ""){//Cur_URL非空，就跳入目标目录
				defaultListModel.clear();
				String[] getString = AccessFile.getSingleName(Cur_URL);		
				for(int i = 0; i < getString.length; ++i){
					defaultListModel.addElement(getString[i]);		
				}	
				Icon[] icons = AccessIcon.getAllIcon(Cur_URL);
				list.setModel(defaultListModel);
				list.setCellRenderer(new MyCellRenderer(icons));
				
			}else{//Cur_URL为空时，就跳转回根目录
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
		
		if(e.getSource() == JMIs[0] || e.getSource() == JMIs2[0]){	//打开文件/文件夹/磁盘
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
		
		else if(e.getSource() == JMIs[1]){//删除单个文件/文件夹
			File file = new File(Cur_URL + "/" + list.getSelectedValue());
			int n;
			if(file.isFile()){
				n = JOptionPane.showConfirmDialog(null, "确定要删除文件 " + file.getName() + " 么?", "文件删除",JOptionPane.YES_NO_OPTION);
			}else{
				n = JOptionPane.showConfirmDialog(null, "确定要删除 " + file.getName() + " 及其目录下的文件么?", "文件夹删除",JOptionPane.YES_NO_OPTION);
			}
			if(n == 0){
				DeleteFileAndFolder.delete(Cur_URL + list.getSelectedValue() +  "\\");
				Go_There();
			}			
		}
		
		else if(e.getSource() == delete2){//删除压缩包
			File file = new File(Cur_URL + "/" + list.getSelectedValue());
			int n;
			n = JOptionPane.showConfirmDialog(null, "确定要删除文件 " + file.getName() + " 么?", "文件删除",JOptionPane.YES_NO_OPTION);
			if(n == 0){
				DeleteFileAndFolder.delete(Cur_URL + list.getSelectedValue() +  "\\");
				Go_There();
			}			
		}
		
		
		
		else if(e.getSource() == delete){//多选下的删除
			List<String> selected_str = list.getSelectedValuesList();
			File file;
			int num = selected_str.size();
			int n = JOptionPane.showConfirmDialog(null, "确定要删除 " + selected_str.get(0) + " 等" + num + "项么?", "文件删除",JOptionPane.YES_NO_OPTION);
			if(n == 0){
					for(int i = 0; i < selected_str.size(); ++i){
						DeleteFileAndFolder.delete(Cur_URL + selected_str.get(i) +  "\\");
					}		
					Go_There();
				}
			}						
		
		else if(e.getSource() == JMIs[2]){//重命名
			String before = list.getSelectedValue();
			File file = new File(Cur_URL + before + "\\");
			String after = "";
			if(file.isDirectory()){
				after = (String) JOptionPane.showInputDialog(null, "请输入新文件夹名:\n", "重命名", JOptionPane.PLAIN_MESSAGE, null, null,
		                list.getSelectedValue());
			}else{
				after = (String) JOptionPane.showInputDialog(null, "请输入新文件名:\n", "重命名", JOptionPane.PLAIN_MESSAGE, null, null,
		                list.getSelectedValue());
			}			
			if(before != after && after != null){
				new File(Cur_URL + before + "\\").renameTo(new File(Cur_URL + after + "\\"));
				Go_There();//刷新列表
			}else{
				Go_There();
			}
		}
		
		else if(e.getSource() == JMIs[3]){//新建文件夹
			String name = JOptionPane.showInputDialog("输入新建文件夹的名称 ");
			String newdir=Cur_URL + "\\"+name;
			File file=new File(newdir);
			if(!file.exists()){
				file.mkdir();
			}
			JOptionPane.showMessageDialog(null,"创建文件夹成功","创建文件夹成功",JOptionPane.WARNING_MESSAGE);  
			Go_There();
		}
		
		else if(e.getSource() == JMIs[4]){//拷贝
			CopyFileAndFolder.init();
			CopyFileAndFolder.generateDir(Cur_URL + "\\" + list.getSelectedValue());
		}
		
		else if(e.getSource() == JMIs[5])  {//粘贴
			String fromDir=CopyFileAndFolder.dir;
			if(CopyFileAndFolder.dirtype==1) { //判断类型为文件 调用对应方法
				String toDir=Cur_URL+"\\"+CopyFileAndFolder.fName;
				File file=new File(toDir);
				if(!file.exists())
				{
					try {file.createNewFile();}  //抛出异常
					catch (IOException e1) {}
				}
				try {PasteFileAndFolder.pasteFile(fromDir, toDir);} 
				catch (IOException e1) { }
				Go_There();
			}
			
			if(CopyFileAndFolder.dirtype==2) { //判断类型为文件夹，调用对应方法
				String toDir=Cur_URL+"\\"+CopyFileAndFolder.fName;
				try { PasteFileAndFolder.pasteFolder(fromDir, toDir);} 
				catch (IOException e1) {}
			}
			Go_There();
		}
		
		else if(e.getSource() == JMIs[6]){//加密解密
			String dir=Cur_URL + "\\" + list.getSelectedValue();
			String key = JOptionPane.showInputDialog("输入秘钥 ");
			LockAndUnlock.encryptFile(dir, Cur_URL, key);
			JOptionPane.showMessageDialog(null,"加密/解密成功","加密/解密成功",JOptionPane.WARNING_MESSAGE);  
			Go_There();
		}
		
		else if(e.getSource()==JMIs[7])//单个文件或文件夹的压缩
		{
			String dir=Cur_URL + "\\" + list.getSelectedValue();
			try {
				ZipAndUnzip.zipDirectory(dir);//直接调用压缩方法，但要注意抛出异常
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Go_There();
		}
		
		else if(e.getSource()==zip)//多选文件或文件夹的压缩
		{
			List<String> selected_str = list.getSelectedValuesList();
			
			String zfname = JOptionPane.showInputDialog("输入压缩文件名称");
			String zfolderURL= Cur_URL+"\\"+zfname;//首先根据用户输入的压缩文件名称，建立同名文件夹，把所有选中的内容存入文件夹
			File zfolder=new File(zfolderURL);
			if(!zfolder.exists())
				zfolder.mkdir();
			
			for(int i = 0; i < selected_str.size(); ++i){//获得文件夹中所有的文件
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
				
				if(CopyFileAndFolder.dirtype==2) {//粘贴内容
					String toDir=zfolderURL+"\\"+CopyFileAndFolder.fName;
					try { PasteFileAndFolder.pasteFolder(fromDir, toDir);} 
					catch (IOException e1) {}
				}
			}
			
			try { //因为已经创建了文件夹，直接调用函数压缩这个文件夹即可
				ZipAndUnzip.zipDirectory(zfolderURL);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null,"压缩成功","压缩成功",JOptionPane.WARNING_MESSAGE);  
			DeleteFileAndFolder.delete(zfolderURL);//删除文件夹
			Go_There();
		}
		
		else if (e.getSource()==unzip)//对压缩文件进行解压
		{
			String dir=Cur_URL + "\\" + list.getSelectedValue();
			try {//直接调用解压方法
				ZipAndUnzip.unzip(dir,Cur_URL);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Go_There();
		}
	}
}

