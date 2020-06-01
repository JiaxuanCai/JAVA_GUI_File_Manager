package view;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/*	��д������Ⱦ��
 *  ����������Ⱦͼ��
 *  �е����û������Ľ��������
 */

public class MyCellRenderer extends JLabel implements ListCellRenderer {
	 Icon[] icons;
	//ArrayList<Icon> icons;

	public MyCellRenderer() {
	};

	public MyCellRenderer(Icon[] icons) {
		// TODO Auto-generated constructor stub
		this.icons = icons;
		//System.out.println("��ʼ��icons��icons��С:"+icons.size());
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		String s = value.toString();
		setText(s);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// ������Ϊ5�Ŀհױ߿�
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		//System.out.println("CellRender�õ���index:" + index);
		setIcon(icons[index]);// ����ͼƬ
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);
		return this;
	}

}