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

import java.awt.Component;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode.KV;
import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode.Type;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoTreeCellRenderer extends DefaultTreeCellRenderer {

	Icon indexIcon;

	public MongoTreeCellRenderer() {
		indexIcon = new ImageIcon(getClass().getResource("/com/mebigfatguy/mongobrowser/resources/index.png"));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		MongoTreeNode treeNode = (MongoTreeNode) value;

		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		label.setHorizontalTextPosition(JLabel.RIGHT);
		label.setVerticalTextPosition(JLabel.CENTER);

		label.setIcon(null);

		if (treeNode.getType() == Type.KeyValue) {
			MongoTreeNode parentTreeNode = treeNode;

			do {
				parentTreeNode = (MongoTreeNode) parentTreeNode.getParent();
			} while ((parentTreeNode != null) && (parentTreeNode.getType() != Type.Collection));

			if (parentTreeNode != null) {

				DBCollection collection = (DBCollection) parentTreeNode.getUserObject();
				List<DBObject> indices = collection.getIndexInfo();
				String key = ((KV) treeNode.getUserObject()).getKey();

				for (DBObject index : indices) {
					BasicDBObject kvIndex = (BasicDBObject) index.get("key");
					if (kvIndex.get(key) != null) {
						label.setIcon(indexIcon);
						break;
					}
				}
			}
		}

		return label;
	}
}
