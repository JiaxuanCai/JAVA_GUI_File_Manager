package model;

import model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import control.*;
import view.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.util.*;

/*	�Զ������ݽ��FileTree������ģ��Windows����ļ������������������һ���ļ���
 *  �޸���Github_Kirill Grouchnikov��Դ��Ŀ
 *  
 */

public class FileTree extends JPanel { 
    //�ļ���ϵͳ�����
	protected static FileSystemView fsv = FileSystemView.getFileSystemView();
    //������Ⱦ���
	public static class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    private Map<String, Icon> iconCache = new HashMap<String, Icon>();
    private Map<File, String> rootNameCache = new HashMap<File, String>();
    //�����ļ������ļ�ͼ��������������Ⱦ
    public Icon[] GetFileIcons(String path){
        Icon[] icons = null;
        String[] files = AccessFile.getSingleName(path);
        for(int i = 0; i < files.length; ++i){
        	icons[i] = this.iconCache.get(files[i]);
        }
        return icons;
    }
        
    //���ͼ��
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            FileTreeNode ftn = (FileTreeNode) value; //�ļ����Ľ��
            File file = ftn.file; 
            String filename = "";
            if (file != null) { //ȷ���ļ���Ϊ�գ����Ϊ��������������
                if (ftn.isFileSystemRoot) {
                    filename = this.rootNameCache.get(file);//���ó�Ա�����õ��ļ�����
                    if (filename == null) {
                        filename = fsv.getSystemDisplayName(file);
                        this.rootNameCache.put(file, filename);//������
                    }              
                } else {
                    filename = file.getName();//ͬ����ֱ�ӵõ��ļ�����
                }
            }
            JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
                    filename, sel, expanded, leaf, row, hasFocus); //��۽��
            if (file != null) {
                Icon icon = this.iconCache.get(filename); //�ӻ����еõ�ͼ��
                if (icon == null) {
                    icon = fsv.getSystemIcon(file); //�ļ���
                    this.iconCache.put(filename, icon);
                }
                result.setIcon(icon);
            }
            return result;
        }
    }
	//�ļ����е�һ�����
    private static class FileTreeNode implements TreeNode {
        private File file;//����Ӧ���ļ�
        private File[] children;//��������ļ��У��������ļ�
        private TreeNode parent;//���λ�ڵ��ļ���
        private boolean isFileSystemRoot;//����Ƿ�Ϊϵͳ��Ŀ¼
        //�������½�һ�����ڵ�
        public FileTreeNode(File file, boolean isFileSystemRoot, TreeNode parent) {
            this.file = file;
            this.isFileSystemRoot = isFileSystemRoot;
            this.parent = parent;
            this.children = this.file.listFiles();
            if (this.children == null)
                this.children = new File[0];//��ʼ��ʹ��������һ���ӽڵ�
        }
        
        public String toString(){
        	if(file != null)
        	return file.getAbsolutePath();
        	else
        		return "";
        }
        //ʹ���ӽڵ��б��½����
        public FileTreeNode(File[] children) {
            this.file = null;
            this.parent = null;
            this.children = children;
        }
        public Enumeration<?> children() {
            final int elementCount = this.children.length; //�����ӽڵ�ĳߴ����
            return new Enumeration<File>() {
                int count = 0;
                public boolean hasMoreElements() {
                    return this.count < elementCount;
                }
                public File nextElement() {
                    if (this.count < elementCount) {
                        return FileTreeNode.this.children[this.count++];
                    }
                    throw new NoSuchElementException("Vector Enumeration");//�쳣
                }
            };
 
        }
        public boolean getAllowsChildren() {//�õ����ӽ�����
            return true;
        }
        public TreeNode getChildAt(int childIndex) {//���ߺ���
            return new FileTreeNode(this.children[childIndex],
                    this.parent == null, this);
        }
        public int getChildCount() {//ͬ��
            return this.children.length;
        }
        public int getIndex(TreeNode node) {
            FileTreeNode ftn = (FileTreeNode) node;
            for (int i = 0; i < this.children.length; i++) {
                if (ftn.file.equals(this.children[i]))
                    return i;
            }
            return -1;
        }

        public TreeNode getParent() {
            return this.parent;
        }
        public boolean isLeaf() {
            return (this.getChildCount() == 0);
        }
    }
    
    private JTree tree;//ʵ���ڴ�
    public FileTree() {//�����ļ���״���
        this.setLayout(new GridLayout());
        File[] roots = File.listRoots(); //��Ŀ¼��ʾ
        FileTreeNode rootTreeNode = new FileTreeNode(roots); //�½���Ŀ¼
        this.tree = new JTree(rootTreeNode); //�½�������
        this.tree.setCellRenderer(new FileTreeCellRenderer()); //��ʼ��
        this.tree.setRootVisible(false);
        this.tree.addTreeSelectionListener(new TreeSelectionListener() { //��Ӧ�������ĳ�ʼ��
        	   public void valueChanged(TreeSelectionEvent e) {
        		   if(tree.getLastSelectedPathComponent() == null) return;
        		   String chooseUrl = tree.getLastSelectedPathComponent().toString();
        		   File temp_file = new File(chooseUrl);
        		   if(temp_file.isDirectory()){
        			   MainFrame._instance.stack.push(MainFrame._instance.Cur_URL);
        			   MainFrame._instance.Cur_URL = chooseUrl;        			 
        			   MainFrame._instance.Go_There();        			   
        		   }
        	   }
        });    
        final JScrollPane jsp = new JScrollPane(this.tree); //���Ĺ�����
        jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.add(jsp, BorderLayout.CENTER);
    }
 
    public static void main(String[] args) {//�����ļ��������Ƿ�������дһ���֣���һ���֣�
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("File tree");
                frame.setSize(500, 400);
                frame.setLocationRelativeTo(null);
                frame.add(new FileTree());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
