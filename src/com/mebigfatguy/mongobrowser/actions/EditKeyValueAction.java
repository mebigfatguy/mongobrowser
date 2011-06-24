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

import javax.swing.AbstractAction;
import javax.swing.JTree;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.TreeUtils;
import com.mebigfatguy.mongobrowser.dialogs.KeyValueDialog;
import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * an action for editing a mongo object's key value pair
 */
public class EditKeyValueAction extends AbstractAction {

	private static final long serialVersionUID = 6217724540802572545L;
	private final MongoContext context;

	/**
	 * constructs an action to handle editing a new key/value pair
	 * 
	 * @param ctxt
	 *            the state context
	 */
	public EditKeyValueAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.EditKeyValue));
		context = ctxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTree tree = context.getTree();
		MongoTreeNode[] selectedNodes = TreeUtils.getSelectedNodes(tree);

		if (selectedNodes.length == 1) {

			DBObject dbObject = (DBObject) ((MongoTreeNode) selectedNodes[0].getParent()).getUserObject();
			MongoTreeNode.KV keyValue = ((MongoTreeNode.KV) selectedNodes[0].getUserObject());

			String key = keyValue.getKey();
			Object value = keyValue.getValue();

			KeyValueDialog dialog = new KeyValueDialog(key, value);
			dialog.setLocationRelativeTo(tree);
			dialog.setModal(true);
			dialog.setVisible(true);

			if (dialog.isOK()) {
				key = dialog.getKey();
				value = dialog.getValue();

				dbObject.put(key, value);
				keyValue.setValue(value);

				MongoTreeNode collectionNode = TreeUtils.findCollectionNode(selectedNodes[0]);
				DBCollection collection = (DBCollection) collectionNode.getUserObject();
				collection.save(dbObject);
			}
		}
	}
}
