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

import java.util.ResourceBundle;

/**
 * manages the resource bundle properties file for this application
 */
public class MongoBundle {

	/**
	 * an enumeration of all the possible entries in the bundle
	 */
	public enum Key {
		OK("mongo.ok"),
		Cancel("mongo.cancel"),
		Title("mongo.title"),
		Servers("mongo.servers"),
		Connect("mongo.connect"),
		Disconnect("mongo.disconnect"),
		ConnectToServer("mongo.connecttoserver"),
		Server("mongo.server"),
		Port("mongo.port"),
		Database("mongo.database"),
		NewDatabase("mongo.newdatabase"),
		NewCollection("mongo.newcollection"),
		ManageIndices("mongo.manageindices"),
		NewObject("mongo.newobject"),
		NewKeyValue("mongo.newkeyvalue"),
		RemoveIndex("mongo.removeindex"),
		AddIndex("mongo.addindex"),
		Key("mongo.key"),
		Value("mongo.value"),
		Integer("mongo.integer"),
		Double("mongo.double"),
		Float("mongo.float"),
		String("mongo.string"),
		Date("mongo.date"),
		Object("mongo.object"),
		Delete("mongo.delete"),
		IndexName("mongo.indexname"),
		IndexFields("mongo.indexfields"),
		IndexPrefix("mongo.indexprefix"),
		DateFormat("mongo.dateformat"),
		MonthValues("mongo.monthvalues");

		private String id;

		/**
		 * creates a key given the properties file name
		 * 
		 * @param id
		 *            the properties file entry name
		 */
		Key(String id) {
			this.id = id;
		}

		/**
		 * retrieves the properties file entry name for this Key
		 * 
		 * @return the properties file entry name id
		 */
		public String id() {
			return id;
		}
	};

	private static ResourceBundle bundle = ResourceBundle.getBundle("com/mebigfatguy/mongobrowser/resources/resource");

	/**
	 * protects this class from being instantiated as it is meant to be accessed
	 * as a static class
	 */
	private MongoBundle() {

	}

	/**
	 * retrieves a string from a resource bundle given a key
	 * 
	 * @param key
	 *            the key of the property item that is to be retrieved
	 * @return the string representing the localized name
	 */
	public static String getString(Key key) {
		return bundle.getString(key.id());
	}
}
