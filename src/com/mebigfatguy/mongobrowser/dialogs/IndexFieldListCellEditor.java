/*
 * mongobrowser - a webstart gui application for viewing, 
 *                editing and administering a Mongo Database
 * Copyright 2009-2011 MeBigFatGuy.com
 * Copyright 2009-2011 Dave Brosius
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *    
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations 
 * under the License. 
 */
package com.mebigfatguy.mongobrowser.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.mebigfatguy.mongobrowser.model.IndexField;
import com.mebigfatguy.mongobrowser.model.IndexFieldList;

public class IndexFieldListCellEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 3996145084996105628L;

	private static final Icon ASCENDING = new ImageIcon(
			IndexFieldListCellEditor.class.getResource("/com/mebigfatguy/mongobrowser/resources/ascending.png"));
	private static final Icon DESCENDING = new ImageIcon(
			IndexFieldListCellEditor.class.getResource("/com/mebigfatguy/mongobrowser/resources/descending.png"));

	private final JPanel panel = new JPanel();
	private final List<FieldControl> controls = new ArrayList<FieldControl>();

	public IndexFieldListCellEditor() {
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		panel.setOpaque(true);
	}

	@Override
	public Object getCellEditorValue() {
		IndexFieldList fieldList = new IndexFieldList();

		for (FieldControl control : controls) {
			String fieldName = control.nameField.getText().trim();

			if (!fieldName.isEmpty()) {
				fieldList.add(fieldName, control.directionButton.getIcon().equals(ASCENDING));
			}
		}

		return fieldList;

	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		panel.removeAll();
		panel.setFont(table.getFont());

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
			panel.setForeground(table.getSelectionForeground());
		} else {
			panel.setBackground(table.getBackground());
			panel.setForeground(table.getForeground());
		}

		if (value instanceof IndexFieldList) {
			IndexFieldList fields = (IndexFieldList) value;

			for (FieldControl control : controls) {
				control.nameField.setText("");
			}

			for (int i = 0; i < fields.size(); i++) {
				if (controls.size() <= i) {
					controls.add(new FieldControl());
				}

				IndexField field = fields.get(i);
				FieldControl control = controls.get(i);
				control.nameField.setFont(table.getFont());
				control.nameField.setText(field.getFieldName());
				control.setAscending(field.isAscending());
				panel.add(control.nameField);
				panel.add(control.directionButton);
			}

			if (controls.size() <= fields.size()) {
				controls.add(new FieldControl());
			}

			FieldControl control = controls.get(fields.size());
			control.nameField.setFont(table.getFont());
			control.nameField.setText("");
			control.setAscending(true);
			panel.add(control.nameField);
			panel.add(control.directionButton);
		}

		return panel;
	}

	static class FieldControl {
		final JTextField nameField = new JTextField(5);
		private final JButton directionButton = new JButton();
		boolean isAscending = true;

		public FieldControl() {
			directionButton.setPreferredSize(new Dimension(ASCENDING.getIconWidth(), ASCENDING.getIconHeight()));
			directionButton.setBorderPainted(false);
			directionButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setAscending(!isAscending);
				}

			});
		}

		public void setAscending(boolean ascending) {
			directionButton.setIcon(ascending ? ASCENDING : DESCENDING);
			isAscending = ascending;
		}
	}
}
