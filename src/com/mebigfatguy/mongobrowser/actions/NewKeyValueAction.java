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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.TreeUtils;
import com.mebigfatguy.mongobrowser.dialogs.KeyValueDialog;
import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * an action for creating a mongo object's key value pair
 */
public class NewKeyValueAction extends AbstractAction {

	private static final long serialVersionUID = -500965537578361564L;
	private MongoContext context;
	
	public NewKeyValueAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.NewKeyValue));
		context = ctxt;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JTree tree = context.getTree();
		KeyValueDialog dialog = new KeyValueDialog();
		dialog.setLocationRelativeTo(tree);
		dialog.setModal(true);
		dialog.setVisible(true);
		if (dialog.isOK()) {
			String key = dialog.getKey();
			Object value = dialog.getValue();
			TreePath path = tree.getSelectionPath();
			MongoTreeNode selectedNode = (MongoTreeNode)path.getLastPathComponent();
			DBObject object;
			
			if (selectedNode.getType() == MongoTreeNode.Type.KeyValue) {
				object = (DBObject) ((MongoTreeNode.KV)selectedNode.getUserObject()).getValue();
			} else {
				object = (DBObject) selectedNode.getUserObject();
			}
			
			object.put(key, value);
			MongoTreeNode kv = new MongoTreeNode(new MongoTreeNode.KV(key, object.get(key)), false);
			selectedNode.add(kv);
			if (value instanceof DBObject) {
				MongoTreeNode slug = new MongoTreeNode();
				kv.add(slug);
			}
			MongoTreeNode collectionNode = TreeUtils.findCollectionNode(selectedNode);
			DBCollection collection = (DBCollection)collectionNode.getUserObject();
			collection.save(object);
			DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
			model.nodeStructureChanged((MongoTreeNode)model.getRoot());
			TreePath selection = new TreePath(kv.getPath());
			tree.scrollPathToVisible(selection);
			tree.setSelectionPath(selection);
		}
	}
}
