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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JTree;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.dialogs.ManageIndicesDialog;
import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class ManageIndicesAction extends AbstractAction {

	private static final long serialVersionUID = 554299884297317739L;
	private final MongoContext context;

	public ManageIndicesAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.ManageIndices));
		context = ctxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTree tree = context.getTree();

		MongoTreeNode[] nodes = context.getSelectedNodes();
		List<String> indices = new ArrayList<String>();
		DBCollection collection = (DBCollection) nodes[0].getUserObject();
		List<DBObject> dbIndices = collection.getIndexInfo();
		for (DBObject dbIndex : dbIndices) {
			String indexName = ((Map<String, String>) dbIndex.get("key")).keySet().iterator().next();
			indices.add(indexName);
		}

		ManageIndicesDialog dialog = new ManageIndicesDialog(indices);
		dialog.setLocationRelativeTo(tree);
		dialog.setModal(true);
		dialog.setVisible(true);

		if (dialog.isOK()) {
			indices = dialog.getIndicesNames();
		}
	}
}
