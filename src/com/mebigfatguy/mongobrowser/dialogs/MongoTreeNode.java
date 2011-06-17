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

import javax.swing.tree.DefaultMutableTreeNode;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -1710144820086785938L;

	public enum Type {Root, Collection, Object, KeyValue, ExpansionSlug};
	
	/**
	 * a dummy object used as a constructor differentiator for the root of the tree
	 */
	public static class Root {}
	
	/**
	 * holds the key value of a mongo object property
	 */
	public static class KV {
		private String key;
		private Object value;
		
		public KV(String k, Object v) {
			key = k;
			value = v;
		}
		
		public String getKey() {
			return key;
		}
		
		public Object getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return key + " : " + value;
		}
	}
	
	Type nodeType;
	boolean readOnly;
	
	/**
	 * constructs a tree node representing an expansion slug
	 */
	public MongoTreeNode() {
		super(null);
		nodeType = Type.ExpansionSlug;
		readOnly = false;
	}
	
	/**
	 * constructs a tree node representing the root of the tree
	 * 
	 * @param root a unused tag to differentiate constructors
	 */
	public MongoTreeNode(Root root) {
		super(null);
		nodeType = Type.Root;
		readOnly = false;
	}
	
	/**
	 * constructs a tree node representing a mongo collection
	 * 
	 * @param dbCollection the mongo collection to represent
	 * @param rdOnly is this collection read only
	 */
	public MongoTreeNode(DBCollection dbCollection, boolean rdOnly) {
		super(dbCollection);
		nodeType = Type.Collection;
		readOnly = rdOnly;
	}
	
	/**
	 * constructs a tree node representing a mongo object
	 * 
	 * @param dbObject the mongo object to represent
	 * @param rdOnly is this object read only
	 */
	public MongoTreeNode(DBObject dbObject, boolean rdOnly) {
		super(dbObject);
		nodeType = Type.Object;
		readOnly = rdOnly;
	}	
	
	/**
	 * constructs a tree node representing a mongo key/value
	 * 
	 * @param dbKV the key value object to represent
	 * @param rdOnly is this key value read only
	 */
	public MongoTreeNode(KV dbKV, boolean rdOnly) {
		super(dbKV);
		nodeType = Type.KeyValue;
		readOnly = rdOnly;
	}

	/**
	 * retrieves the type of this node
	 * 
	 * @return the nodes type
	 */
	public Type getType() {
		return nodeType;
	}
	
	/**
	 * retrieves whether this node is readonly
	 * @return if this tree node is readonly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}
	
	/**
	 * returns a user viewable string
	 * 
	 * @return a string representing this node
	 */
	@Override
	public String toString() {
		switch (nodeType) {	
			case Collection:
			case Object:
			case KeyValue:
				return getUserObject().toString();
				
			case Root:
			case ExpansionSlug:
			default:
				return "";
		}
	}
}
