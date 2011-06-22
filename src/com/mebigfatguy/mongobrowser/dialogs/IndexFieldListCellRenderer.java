package com.mebigfatguy.mongobrowser.dialogs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import sun.swing.DefaultLookup;

import com.mebigfatguy.mongobrowser.model.IndexField;
import com.mebigfatguy.mongobrowser.model.IndexFieldList;

public class IndexFieldListCellRenderer extends JPanel implements TableCellRenderer {

	private static final long serialVersionUID = 1447611436027408402L;

	private static final Icon ASCENDING = new ImageIcon(
			IndexFieldListCellRenderer.class.getResource("/com/mebigfatguy/mongobrowser/resources/ascending.png"));
	private static final Icon DESCENDING = new ImageIcon(
			IndexFieldListCellRenderer.class.getResource("/com/mebigfatguy/mongobrowser/resources/descending.png"));

	private final List<JLabel> fieldLabels = new ArrayList<JLabel>();

	public IndexFieldListCellRenderer() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		removeAll();
		setFont(table.getFont());

		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}

		if (hasFocus) {
			setBorder(isSelected ? DefaultLookup.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder")
					: DefaultLookup.getBorder(this, ui, "Table.focusCellHighlightBorder"));
		} else {
			setBorder(DefaultLookup.getBorder(this, ui, "Table.cellNoFocusBorder"));
		}

		if (value instanceof IndexFieldList) {
			IndexFieldList fields = (IndexFieldList) value;

			for (int i = 0; i < fields.size(); i++) {
				if (fieldLabels.size() <= i) {
					JLabel l = new JLabel();
					fieldLabels.add(l);
					l.setHorizontalTextPosition(SwingConstants.LEFT);
					l.setVerticalTextPosition(SwingConstants.CENTER);
				}

				JLabel l = fieldLabels.get(i);
				l.setFont(table.getFont());
				IndexField field = fields.get(i);
				l.setText(field.getFieldName());
				l.setIcon(field.isAscending() ? ASCENDING : DESCENDING);
				add(fieldLabels.get(i));
			}
		}
		return this;
	}
}
