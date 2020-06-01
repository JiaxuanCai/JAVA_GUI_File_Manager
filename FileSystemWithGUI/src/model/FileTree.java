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

/*	自定义数据结果FileTree，用于模仿Windows风格文件管理器，在左侧生成一颗文件树
 *  修改自Github_Kirill Grouchnikov开源项目
 *  
 */

public class FileTree extends JPanel { 
    //文件树系统的外观
	protected static FileSystemView fsv = FileSystemView.getFileSystemView();
    //重新渲染外观
	public static class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    private Map<String, Icon> iconCache = new HashMap<String, Icon>();
    private Map<File, String> rootNameCache = new HashMap<File, String>();
    //缓存文件名和文件图标来加速重新渲染
    public Icon[] GetFileIcons(String path){
        Icon[] icons = null;
        String[] files = AccessFile.getSingleName(path);
        for(int i = 0; i < files.length; ++i){
        	icons[i] = this.iconCache.get(files[i]);
        }
        return icons;
    }
        
    //获得图标
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            FileTreeNode ftn = (FileTreeNode) value; //文件树的结点
            File file = ftn.file; 
            String filename = "";
            if (file != null) { //确保文件不为空，如果为空则进行例外操作
                if (ftn.isFileSystemRoot) {
                    filename = this.rootNameCache.get(file);//调用成员函数得到文件名称
                    if (filename == null) {
                        filename = fsv.getSystemDisplayName(file);
                        this.rootNameCache.put(file, filename);//输出外观
                    }              
                } else {
                    filename = file.getName();//同上理，直接得到文件名称
                }
            }
            JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
                    filename, sel, expanded, leaf, row, hasFocus); //外观结果
            if (file != null) {
                Icon icon = this.iconCache.get(filename); //从缓存中得到图标
                if (icon == null) {
                    icon = fsv.getSystemIcon(file); //文件名
                    this.iconCache.put(filename, icon);
                }
                result.setIcon(icon);
            }
            return result;
        }
    }
	//文件树中的一个结点
    private static class FileTreeNode implements TreeNode {
        private File file;//结点对应的文件
        private File[] children;//结点若是文件夹，结点的子文件
        private TreeNode parent;//结点位于的文件夹
        private boolean isFileSystemRoot;//结点是否为系统根目录
        //构函，新建一个树节点
        public FileTreeNode(File file, boolean isFileSystemRoot, TreeNode parent) {
            this.file = file;
            this.isFileSystemRoot = isFileSystemRoot;
            this.parent = parent;
            this.children = this.file.listFiles();
            if (this.children == null)
                this.children = new File[0];//初始化使得至少有一个子节点
        }
        
        public String toString(){
        	if(file != null)
        	return file.getAbsolutePath();
        	else
        		return "";
        }
        //使用子节点列表新建结点
        public FileTreeNode(File[] children) {
            this.file = null;
            this.parent = null;
            this.children = children;
        }
        public Enumeration<?> children() {
            final int elementCount = this.children.length; //根据子节点的尺寸操作
            return new Enumeration<File>() {
                int count = 0;
                public boolean hasMoreElements() {
                    return this.count < elementCount;
                }
                public File nextElement() {
                    if (this.count < elementCount) {
                        return FileTreeNode.this.children[this.count++];
                    }
                    throw new NoSuchElementException("Vector Enumeration");//异常
                }
            };
 
        }
        public boolean getAllowsChildren() {//得到孩子结点许可
            return true;
        }
        public TreeNode getChildAt(int childIndex) {//工具函数
            return new FileTreeNode(this.children[childIndex],
                    this.parent == null, this);
        }
        public int getChildCount() {//同上
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
    
    private JTree tree;//实例在此
    public FileTree() {//创建文件树状面板
        this.setLayout(new GridLayout());
        File[] roots = File.listRoots(); //根目录显示
        FileTreeNode rootTreeNode = new FileTreeNode(roots); //新建根目录
        this.tree = new JTree(rootTreeNode); //新建树对象
        this.tree.setCellRenderer(new FileTreeCellRenderer()); //初始化
        this.tree.setRootVisible(false);
        this.tree.addTreeSelectionListener(new TreeSelectionListener() { //响应鼠标操作的初始化
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
        final JScrollPane jsp = new JScrollPane(this.tree); //树的滚动栏
        jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.add(jsp, BorderLayout.CENTER);
    }
 
    public static void main(String[] args) {//测试文件树功能是否完整（写一部分，调一部分）
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
