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
package com.mebigfatguy.mongobrowser;

import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;

/**
 * a collection of utility methods used in a static context
 */
public class TreeUtils {
	
	private TreeUtils() {	
	}
	
	/**
	 * retrieves the collection node that owns the requested node
	 * 
	 * @param fromNode the node to find the collection for
	 * @return the collection node that owns this node
	 */
	public static MongoTreeNode findCollectionNode(MongoTreeNode fromNode) {
		while (fromNode.getType() != MongoTreeNode.Type.Collection) {
			fromNode = (MongoTreeNode)fromNode.getParent();
		}
		
		return fromNode;
	}
}
