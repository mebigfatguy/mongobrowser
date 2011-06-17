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

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 * a JTextComponent model object that only allows float value input
 */
public class FloatDocument extends PlainDocument {

	private static final long serialVersionUID = 569269763667696934L;

	/**
	 * intercepts string insertions to make sure that the values to be put into
	 * a text component is only an float value
	 * 
	 * @param pos where the text is being inserted
	 * @param insertStr the new text that was typed
	 * @param atts the attributes for the text (unused)
	 */
	@Override
	public void insertString(int pos, String insertStr, AttributeSet atts) throws BadLocationException {
		StringBuilder text = new StringBuilder(getText(0, getLength()));
		try {
			text.insert(pos, insertStr);
			Float.parseFloat(text.toString());
			super.insertString(pos, insertStr, atts);
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();	
		}
	}

}
