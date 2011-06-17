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
package com.mebigfatguy.mongobrowser.actions;

import java.awt.event.ActionEvent;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.dialogs.ConnectionDialog;
import com.mongodb.Mongo;

/**
 * an action for connecting to a mongo database
 */
public class ConnectAction extends AbstractAction {

	private static final long serialVersionUID = -4704951174439411332L;
	private MongoContext context;
	
	public ConnectAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.Connect));
		context = ctxt;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			ConnectionDialog cd = new ConnectionDialog();
			cd.setLocationRelativeTo(null);
			cd.setModal(true);
			cd.setVisible(true);
			if (cd.isOK()) {
				String host = cd.getHost();
				int port = cd.getPort();
				context.setServer(new Mongo(host, port));
			}
		} catch (UnknownHostException uhe) {
			JOptionPane.showMessageDialog(null, uhe.getMessage());
		}
	}
}
