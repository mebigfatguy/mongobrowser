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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.SwingUtils;
import com.mebigfatguy.mongobrowser.model.IndexDescription;

public class ManageIndicesDialog extends JDialog {

	private static final long serialVersionUID = -4263800895451732866L;

	private JTable indicesTable;
	private JButton removeIndexButton;
	private JButton addIndexButton;
	private JTextField indexNameField;
	private JButton okButton;
	private JButton cancelButton;
	private boolean ok = false;

	public ManageIndicesDialog(List<IndexDescription> indices) {
		setTitle(MongoBundle.getString(MongoBundle.Key.ManageIndices));
		initComponents(indices);
		initListeners();
		pack();
	}

	/**
	 * did the user click ok
	 * 
	 * @return if the user clicked ok
	 */
	public boolean isOK() {
		return ok;
	}

	public List<IndexDescription> getIndicesNames() {

		ManageIndicesModel model = (ManageIndicesModel) indicesTable.getModel();
		return model.getIndices();
	}

	private void initComponents(List<IndexDescription> indices) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(4, 4));
		cp.add(createIndicesListPanel(indices), BorderLayout.CENTER);
		cp.add(createCtrlPanel(), BorderLayout.SOUTH);

		SwingUtils.sizeUniformly(okButton, cancelButton, addIndexButton, removeIndexButton);
	}

	private void initListeners() {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				ok = true;
				dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		addIndexButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IndexDescription index = new IndexDescription(indexNameField.getText(), new HashMap<String, Boolean>());
				ManageIndicesModel model = (ManageIndicesModel) indicesTable.getModel();
				model.add(index);
			}
		});
	}

	private JPanel createIndicesListPanel(List<IndexDescription> indices) {
		JPanel p = new JPanel();
		p.setLayout(new FormLayout("6dlu, pref, 5dlu, pref, 6dlu",
				"6dlu, 12dlu:grow, pref, 12dlu:grow, 6dlu, pref, 6dlu"));
		CellConstraints cc = new CellConstraints();

		ManageIndicesModel model = new ManageIndicesModel(indices);

		indicesTable = new JTable(model);
		p.add(new JScrollPane(indicesTable), cc.xywh(2, 2, 1, 3));

		removeIndexButton = new JButton(MongoBundle.getString(MongoBundle.Key.RemoveIndex));
		p.add(removeIndexButton, cc.xy(4, 3));

		indexNameField = new JTextField(20);
		p.add(indexNameField, cc.xy(2, 6));

		addIndexButton = new JButton(MongoBundle.getString(MongoBundle.Key.AddIndex));
		p.add(addIndexButton, cc.xy(4, 6));

		return p;
	}

	/**
	 * creates a panel holding the ok and cancel buttons
	 * 
	 * @return the ok/cancel button panel
	 */
	private JPanel createCtrlPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalGlue());

		okButton = new JButton(MongoBundle.getString(MongoBundle.Key.OK));
		p.add(okButton);
		p.add(Box.createHorizontalStrut(10));

		cancelButton = new JButton(MongoBundle.getString(MongoBundle.Key.Cancel));
		p.add(cancelButton);
		p.add(Box.createHorizontalStrut(10));

		return p;
	}
}
