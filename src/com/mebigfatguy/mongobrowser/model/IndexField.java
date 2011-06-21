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

public class IndexField {

	private final String fieldName;
	private final boolean ascending;

	public IndexField(String name, boolean asc) {

		fieldName = name;
		ascending = asc;
	}

	public String getFieldName() {
		return fieldName;
	}

	public boolean isAscending() {
		return ascending;
	}

	@Override
	public int hashCode() {
		return fieldName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IndexField) {
			IndexField that = (IndexField) o;
			return fieldName.equals(that.fieldName);
		}

		return false;
	}
}
