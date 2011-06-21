package com.mebigfatguy.mongobrowser.dialogs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.mebigfatguy.mongobrowser.model.IndexField;
import com.mebigfatguy.mongobrowser.model.IndexFieldList;

public class IndexFieldListCellRenderer implements TableCellRenderer {

	private final JPanel panel = new JPanel();
	private final List<JLabel> fieldLabels = new ArrayList<JLabel>();

	public IndexFieldListCellRenderer() {
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		panel.setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		panel.removeAll();
		panel.setFont(table.getFont());

		if (value instanceof IndexFieldList) {
			IndexFieldList fields = (IndexFieldList) value;

			for (int i = 0; i < fields.size(); i++) {
				if (fieldLabels.size() <= i) {
					fieldLabels.add(new JLabel());
				}

				JLabel l = fieldLabels.get(i);
				l.setFont(table.getFont());
				IndexField field = fields.get(i);
				l.setText(field.getFieldName());
				panel.add(fieldLabels.get(i));
			}
		}
		return panel;
	}
}
