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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mebigfatguy.mongobrowser.MongoBundle;

public class ManageIndicesDialog extends JDialog {

	private static final long serialVersionUID = -1871109721924940686L;

	private JList indicesList;
	private JButton removeIndexButton;
	private JButton addIndexButton;
	private JTextField indexNameField;
	private JButton okButton;
	private JButton cancelButton;
	private boolean ok = false;

	public ManageIndicesDialog(List<String> names) {
		setTitle(MongoBundle.getString(MongoBundle.Key.ManageIndices));
		initComponents(names);
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

	public List<String> getIndicesNames() {

		List<String> names = new ArrayList<String>();
		DefaultListModel model = (DefaultListModel) indicesList.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			names.add((String) model.get(i));
		}

		return names;
	}

	private void initComponents(List<String> names) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(4, 4));
		cp.add(createIndicesListPanel(names), BorderLayout.CENTER);
		cp.add(createCtrlPanel(), BorderLayout.SOUTH);
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
	}

	private JPanel createIndicesListPanel(List<String> names) {
		JPanel p = new JPanel();
		p.setLayout(new FormLayout("6dlu, pref, 5dlu, pref, 6dlu",
				"6dlu, 12dlu:grow, pref, 12dlu:grow, 6dlu, pref, 6dlu"));
		CellConstraints cc = new CellConstraints();

		DefaultListModel model = new DefaultListModel();
		for (String name : names) {
			model.addElement(name);
		}

		indicesList = new JList(model);
		p.add(new JScrollPane(indicesList), cc.xywh(2, 2, 1, 3));

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
		p.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
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
