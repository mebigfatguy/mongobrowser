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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.SwingUtils;

/**
 * a dialog for requesting server and port information for a mongo server
 */
public class ConnectionDialog extends JDialog {

	private static final long serialVersionUID = -3210253285939498235L;
	private JTextField serverField;
	private JTextField portField;
	private JButton okButton;
	private JButton cancelButton;
	private boolean ok = false;

	/**
	 * constructs the dialog
	 */
	public ConnectionDialog() {
		setTitle(MongoBundle.getString(MongoBundle.Key.ConnectToServer));
		initComponents();
		initListeners();
		pack();
	}

	/**
	 * creates and lays out the components
	 */
	private void initComponents() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(4, 4));
		cp.add(createFormPanel(), BorderLayout.CENTER);
		cp.add(createCtrlPanel(), BorderLayout.SOUTH);
	}

	/**
	 * create the panel that houses the input form
	 * 
	 * @return the form panel
	 */
	private JPanel createFormPanel() {
		JPanel p = new JPanel();
		p.setLayout(new FormLayout("6dlu, pref, 5dlu, 200px, 6dlu", "6dlu, pref, 2dlu, pref, 6dlu"));
		CellConstraints cc = new CellConstraints();

		JLabel serverLabel = new JLabel(MongoBundle.getString(MongoBundle.Key.Server));
		p.add(serverLabel, cc.xy(2, 2));

		serverField = new JTextField();
		p.add(serverField, cc.xy(4, 2));
		serverField.setText("localhost");

		serverLabel.setLabelFor(serverField);

		JLabel portLabel = new JLabel(MongoBundle.getString(MongoBundle.Key.Port));
		p.add(portLabel, cc.xy(2, 4));

		portField = new JTextField();
		portField.setDocument(new IntegerDocument());
		p.add(portField, cc.xy(4, 4));
		portField.setText("27017");

		portLabel.setLabelFor(portField);

		return p;
	}

	/**
	 * creates the panel that houses the ok and cancel buttons
	 * 
	 * @return the control panel
	 */
	private JPanel createCtrlPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalGlue());

		okButton = new JButton(MongoBundle.getString(MongoBundle.Key.OK));
		p.add(okButton);
		p.add(Box.createHorizontalStrut(10));

		cancelButton = new JButton(MongoBundle.getString(MongoBundle.Key.Cancel));
		p.add(cancelButton);
		p.add(Box.createHorizontalStrut(10));

		SwingUtils.sizeUniformly(okButton, cancelButton);

		return p;
	}

	/**
	 * initializes the event listeners
	 */
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

		getRootPane().setDefaultButton(okButton);
	}

	/**
	 * retrieves whether or not the user clicked the ok button
	 * 
	 * @return if the user clicked the ok button
	 */
	public boolean isOK() {
		return ok;
	}

	/**
	 * get the user supplied host name of the server
	 * 
	 * @return the host name or ip address of the server
	 */
	public String getHost() {
		return serverField.getText();
	}

	/**
	 * get the user supplied port of the server
	 * 
	 * @return the port of the server
	 */
	public int getPort() {
		return Integer.parseInt(portField.getText());
	}

}
