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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.mebigfatguy.mongobrowser.MongoBundle;

public class DateDocument extends PlainDocument {

	private static Pattern DATE_PATTERN = Pattern.compile(MongoBundle.getString(MongoBundle.Key.DateRegex));

	/**
	 * intercepts string insertions to make sure that the values to be put into
	 * a text component is only a date value
	 * 
	 * @param pos
	 *            where the text is being inserted
	 * @param insertStr
	 *            the new text that was typed
	 * @param atts
	 *            the attributes for the text (unused)
	 */
	@Override
	public void insertString(int pos, String insertStr, AttributeSet atts) throws BadLocationException {
		StringBuilder text = new StringBuilder(getText(0, getLength()));
		text.insert(pos, insertStr);

		Matcher m = DATE_PATTERN.matcher(text.toString());
		if (m.matches() || m.hitEnd()) {
			super.insertString(pos, insertStr, atts);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
}
