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
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * an action for creating a mongo object
 */
public class NewObjectAction extends AbstractAction {

	private static final long serialVersionUID = 5752147095730092598L;
	private MongoContext context;
	
	public NewObjectAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.NewObject));
		context = ctxt;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JTree tree = context.getTree();
		TreePath path = tree.getSelectionPath();
		MongoTreeNode collectionNode = (MongoTreeNode)path.getLastPathComponent();
		DBCollection dbCollection = (DBCollection) collectionNode.getUserObject();
		BasicDBObject dbObj = new BasicDBObject();
		dbCollection.insert(dbObj);
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		MongoTreeNode objectNode = new MongoTreeNode(dbObj, false);
		collectionNode.add(objectNode);
		MongoTreeNode slug = new MongoTreeNode();
		objectNode.add(slug);
		model.nodeStructureChanged((MongoTreeNode)model.getRoot());
		TreePath selection = new TreePath(objectNode.getPath());
		tree.scrollPathToVisible(selection);
		tree.setSelectionPath(selection);
	}

}
