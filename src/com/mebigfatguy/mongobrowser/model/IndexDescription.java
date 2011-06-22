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
package com.mebigfatguy.mongobrowser.model;

import java.io.Serializable;

public class IndexDescription implements Comparable<IndexDescription>, Serializable {

	private static final long serialVersionUID = 1616892606319350318L;

	private String indexName;
	private IndexFieldList indexFields;

	public IndexDescription(String name, IndexFieldList fields) {
		indexName = name;
		indexFields = fields;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String name) {
		indexName = name;
	}

	public IndexFieldList getIndexFieldList() {
		return indexFields;
	}

	public void setIndexFieldList(IndexFieldList fieldList) {
		indexFields = fieldList;
	}

	@Override
	public int hashCode() {
		return indexName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IndexDescription) {
			IndexDescription that = (IndexDescription) o;

			return indexName.equals(that.indexName);
		}

		return false;
	}

	@Override
	public int compareTo(IndexDescription o) {
		return indexName.compareTo(o.indexName);
	}
}
