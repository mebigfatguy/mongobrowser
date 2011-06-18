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
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * an action for creating a new mongo collection
 */
public class NewCollectionAction extends AbstractAction {

	private static final long serialVersionUID = 9090870672875251498L;

	private MongoContext context;

	public NewCollectionAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.NewCollection));
		context = ctxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTree tree = context.getTree();
		String collectionName = JOptionPane.showInputDialog(tree, MongoBundle.getString(MongoBundle.Key.NewCollection));
		if (collectionName != null) {
			DB db = context.getDatabase();
			DBCollection dbCollection = db.getCollection(collectionName);
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			MongoTreeNode root = (MongoTreeNode) model.getRoot();
			MongoTreeNode collectionNode = new MongoTreeNode(dbCollection, false);
			root.add(collectionNode);
			MongoTreeNode slug = new MongoTreeNode();
			collectionNode.add(slug);
			model.nodeStructureChanged(root);
			TreePath selection = new TreePath(collectionNode.getPath());
			tree.scrollPathToVisible(selection);
			tree.setSelectionPath(selection);
		}
	}

}
