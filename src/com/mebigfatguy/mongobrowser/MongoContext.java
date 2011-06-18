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
package com.mebigfatguy.mongobrowser;

import javax.swing.JTree;

import com.mebigfatguy.mongobrowser.dialogs.MongoTreeNode;
import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * a mediator interface for getting all the key items used in the application
 */
public interface MongoContext {

	void setTree(JTree tree);

	JTree getTree();

	void setSelectedNodes(MongoTreeNode... node);

	MongoTreeNode[] getSelectedNodes();

	void setServer(Mongo server);

	Mongo getServer();

	void setDatabase(DB db);

	DB getDatabase();
}
