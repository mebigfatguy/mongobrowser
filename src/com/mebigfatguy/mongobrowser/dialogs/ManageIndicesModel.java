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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.model.IndexDescription;
import com.mebigfatguy.mongobrowser.model.IndexFieldList;

public class ManageIndicesModel extends AbstractTableModel {

	private static final long serialVersionUID = 2062960191211526719L;

	private final List<IndexDescription> indices;

	public ManageIndicesModel(List<IndexDescription> indexInfo) {
		indices = indexInfo;
	}

	public List<IndexDescription> getIndices() {
		return indices;
	}

	public void add(IndexDescription index) {
		indices.add(index);
		fireTableRowsInserted(indices.size() - 1, indices.size() - 1);
	}

	public void removeAt(int index) {
		indices.remove(index);
		fireTableRowsDeleted(index, index);
	}

	@Override
	public int getRowCount() {
		return indices.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IndexDescription index = indices.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return index.getIndexName();
			case 1:
				return index.getIndexFieldList();
			default:
				throw new IllegalArgumentException("columnIndex = " + columnIndex);
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		IndexDescription index = indices.get(rowIndex);
		switch (columnIndex) {
			case 0:
				index.setIndexName((String) value);
			break;
			case 1:
				index.setIndexFieldList((IndexFieldList) value);
			break;
			default:
				throw new IllegalArgumentException("columnIndex = " + columnIndex);
		}

		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return MongoBundle.getString(MongoBundle.Key.IndexName);
			case 1:
				return MongoBundle.getString(MongoBundle.Key.IndexFields);
			default:
				throw new IllegalArgumentException("columnIndex = " + columnIndex);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return IndexFieldList.class;
			default:
				throw new IllegalArgumentException("columnIndex = " + columnIndex);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		String name = (String) getValueAt(rowIndex, 0);
		if ("_id_".equals(name)) {
			return false;
		}
		return (columnIndex == 0) || (columnIndex == 1);
	}
}
