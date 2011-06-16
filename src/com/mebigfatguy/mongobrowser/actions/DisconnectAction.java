/*
 * mongobrowser - a webstart gui application for viewing, 
 *                editing and administering a Mongo Database
 * Copyright 2009-2010 MeBigFatGuy.com
 * Copyright 2009-2010 Dave Brosius
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

import javax.swing.AbstractAction;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;

/**
 * an action for disconnecting from a mongo database
 */
public class DisconnectAction extends AbstractAction {

	private static final long serialVersionUID = -5795316226405751888L;
	private MongoContext context;
	
	public DisconnectAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.Disconnect));
		context = ctxt;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		context.setDatabase(null);
		context.setServer(null);
	}
}
