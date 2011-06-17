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
import javax.swing.tree.DefaultTreeModel;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.TreeUtils;
import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * an action for deleting a mongo object
 */
public class DeleteAction extends AbstractAction {

	private static final long serialVersionUID = 9057255369669193765L;
	private MongoContext context;
	
	public DeleteAction(MongoContext ctxt) {
		super(MongoBundle.getString(MongoBundle.Key.Delete));
		context = ctxt;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		MongoTreeNode node = context.getSelectedNode();
		switch (node.getType()) {
			case Collection: {
				DBCollection collection = (DBCollection)node.getUserObject();
				collection.drop();
				MongoTreeNode dbNode = (MongoTreeNode)node.getParent();
				dbNode.remove(node);
				DefaultTreeModel model = (DefaultTreeModel)context.getTree().getModel();
				model.nodeStructureChanged(dbNode);
			}
			break;
			
			case Object: {
				DBObject object = (DBObject)node.getUserObject();
				MongoTreeNode collectionNode = TreeUtils.findCollectionNode(node);
				DBCollection collection = (DBCollection)collectionNode.getUserObject();
				collection.remove(object);
				collectionNode.remove(node);
				DefaultTreeModel model = (DefaultTreeModel)context.getTree().getModel();
				model.nodeStructureChanged(collectionNode);				
			}
			break;
			
			case KeyValue: {
				MongoTreeNode.KV kv = (MongoTreeNode.KV)node.getUserObject();
				String key = kv.getKey();
				if (!key.startsWith("_")) {
					MongoTreeNode objectNode = (MongoTreeNode)node.getParent();
					DBObject object = (DBObject)objectNode.getUserObject();
					object.removeField(key);
					MongoTreeNode collectionNode = TreeUtils.findCollectionNode(objectNode);
					DBCollection collection = (DBCollection)collectionNode.getUserObject();
					collection.save(object);
					objectNode.remove(node);
					DefaultTreeModel model = (DefaultTreeModel)context.getTree().getModel();
					model.nodeStructureChanged(objectNode);
				}
			}
			break;
		}
	}
}
