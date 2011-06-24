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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.SwingUtils;
import com.mongodb.BasicDBObject;

/**
 * a dialog for collecting key/value pairs for a mongo object property
 */
public class KeyValueDialog extends JDialog {

	private static final long serialVersionUID = 4909101478144542212L;

	private JTextField keyField;
	private JComboBox valueTypeBox;
	private JTextField valueField;
	private JButton okButton;
	private JButton cancelButton;
	private boolean ok = false;

	/**
	 * constructs a dialog to collect a key value for a mongo object's property
	 */
	public KeyValueDialog() {
		this(null, null);
	}

	/**
	 * constructs a dialog to collect a key value for a mongo object's property
	 */
	public KeyValueDialog(String key, Object value) {
		setTitle(MongoBundle.getString(MongoBundle.Key.NewKeyValue));
		initComponents();
		initListeners();

		if (key != null) {
			keyField.setText(key);
			keyField.setEnabled(false);
			installValue(value);
		}

		pack();
	}

	/**
	 * adds and lays out the components in the dialog
	 */
	private void initComponents() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(4, 4));
		cp.add(createFormPanel(), BorderLayout.CENTER);
		cp.add(createCtrlPanel(), BorderLayout.SOUTH);
	}

	/**
	 * creates the panel for collecting the key and value
	 * 
	 * @return a panel holding the input fields
	 */
	private JPanel createFormPanel() {
		JPanel p = new JPanel();
		p.setLayout(new FormLayout("6dlu, pref, 5dlu, 200px, 5dlu, pref, 6dlu", "6dlu, pref, 2dlu, pref, 6dlu"));
		CellConstraints cc = new CellConstraints();

		JLabel keyLabel = new JLabel(MongoBundle.getString(MongoBundle.Key.Key));
		p.add(keyLabel, cc.xy(2, 2));

		keyField = new JTextField();
		p.add(keyField, cc.xy(4, 2));

		keyLabel.setLabelFor(keyField);

		JLabel valueLabel = new JLabel(MongoBundle.getString(MongoBundle.Key.Value));
		p.add(valueLabel, cc.xy(2, 4));

		valueField = new JTextField();
		p.add(valueField, cc.xy(4, 4));

		valueTypeBox = new JComboBox();
		DefaultComboBoxModel model = (DefaultComboBoxModel) valueTypeBox.getModel();
		model.addElement(new IntegerValueType());
		model.addElement(new DoubleValueType());
		model.addElement(new FloatValueType());
		model.addElement(new StringValueType());
		model.addElement(new DateValueType());
		model.addElement(new ObjectValueType());
		valueTypeBox.setSelectedIndex(3);
		p.add(valueTypeBox, cc.xy(6, 4));

		valueLabel.setLabelFor(valueField);

		return p;
	}

	/**
	 * creates a panel holding the ok and cancel buttons
	 * 
	 * @return the ok/cancel button panel
	 */
	private JPanel createCtrlPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalGlue());

		okButton = new JButton(MongoBundle.getString(MongoBundle.Key.OK));
		p.add(okButton);
		p.add(Box.createHorizontalStrut(10));

		cancelButton = new JButton(MongoBundle.getString(MongoBundle.Key.Cancel));
		p.add(cancelButton);
		p.add(Box.createHorizontalStrut(10));

		SwingUtils.sizeUniformly(okButton, cancelButton);

		return p;
	}

	/**
	 * installs the listeners
	 */
	private void initListeners() {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				ok = true;
				dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		valueTypeBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					ValueType vt = (ValueType) ie.getItem();
					vt.installDocument(valueField);
				}
			}
		});

		getRootPane().setDefaultButton(okButton);
	}

	/**
	 * did the user click ok
	 * 
	 * @return if the user clicked ok
	 */
	public boolean isOK() {
		return ok;
	}

	/**
	 * gets the key value from the form
	 * 
	 * @return the key value
	 */
	public String getKey() {
		return keyField.getText();
	}

	/**
	 * gets the typed value from the dialog
	 * 
	 * @return the typed value
	 */
	public Object getValue() {
		return ((ValueType) valueTypeBox.getSelectedItem()).getValue(valueField);
	}

	/**
	 * updates the key value dialog based on the value, this is kind of crufty,
	 * would be nicer to handle the popup a better way
	 * 
	 * @param value
	 *            the value to set
	 */
	private void installValue(Object value) {
		if (value != null) {
			DefaultComboBoxModel model = (DefaultComboBoxModel) valueTypeBox.getModel();

			if (value instanceof Integer) {
				valueTypeBox.setSelectedIndex(0);
				ValueType vt = (ValueType) model.getElementAt(0);
				vt.installDocument(valueField);
				valueField.setText(String.valueOf(value));
			} else if (value instanceof Double) {
				valueTypeBox.setSelectedIndex(1);
				ValueType vt = (ValueType) model.getElementAt(1);
				vt.installDocument(valueField);
				valueField.setText(String.valueOf(value));
			} else if (value instanceof Float) {
				valueTypeBox.setSelectedIndex(2);
				ValueType vt = (ValueType) model.getElementAt(2);
				vt.installDocument(valueField);
				valueField.setText(String.valueOf(value));
			} else if (value instanceof String) {
				valueTypeBox.setSelectedIndex(3);
				ValueType vt = (ValueType) model.getElementAt(3);
				vt.installDocument(valueField);
				valueField.setText((String) value);
			} else if (value instanceof Date) {
				valueTypeBox.setSelectedIndex(4);
				ValueType vt = (ValueType) model.getElementAt(4);
				vt.installDocument(valueField);
				SimpleDateFormat sdf = new SimpleDateFormat(MongoBundle.getString(MongoBundle.Key.DateFormat));
				valueField.setText(sdf.format((Date) value));
			}
		}
	}

	/**
	 * interface for items that are put into the Value type combobox
	 */
	interface ValueType {
		/**
		 * switch the JTextField's model based on the type of value
		 * 
		 * @param field
		 *            the component who's document should be modified
		 */
		void installDocument(JTextField field);

		/**
		 * get the value object of the field, based on the type of this value
		 * 
		 * @param field
		 *            the component to get the value from
		 * @return a value object
		 */
		Object getValue(JTextField field);
	}

	/**
	 * a value type representing an Integer object
	 */
	static class IntegerValueType implements ValueType {

		/**
		 * installs an IntegerDocument as the field's model
		 * 
		 * @param field
		 *            the text edit field to install the model
		 */
		@Override
		public void installDocument(JTextField field) {
			try {
				String val = field.getText();
				field.setText("");
				field.setDocument(new IntegerDocument());
				field.getDocument().insertString(0, val, null);
				field.setEnabled(true);
			} catch (BadLocationException ble) {
			}
		}

		/**
		 * get the field's values as an Integer
		 * 
		 * @param field
		 *            the component that holds the integer value
		 * @return an Integer that is the value of the text field
		 */
		@Override
		public Object getValue(JTextField field) {
			return Integer.valueOf(field.getText());
		}

		/**
		 * returns the display value shown in the combo box
		 * 
		 * @return the string 'Integer'
		 */
		@Override
		public String toString() {
			return MongoBundle.getString(MongoBundle.Key.Integer);
		}
	}

	/**
	 * a value type representing an Double object
	 */
	static class DoubleValueType implements ValueType {

		/**
		 * installs an DoubleDocument as the field's model
		 * 
		 * @param field
		 *            the text edit field to install the model
		 */
		@Override
		public void installDocument(JTextField field) {
			try {
				String val = field.getText();
				field.setText("");
				field.setDocument(new DoubleDocument());
				field.getDocument().insertString(0, val, null);
				field.setEnabled(true);
			} catch (BadLocationException ble) {
			}
		}

		/**
		 * get the field's values as an Double
		 * 
		 * @param field
		 *            the component that holds the double value
		 * @return an Double that is the value of the text field
		 */
		@Override
		public Object getValue(JTextField field) {
			return Double.valueOf(field.getText());
		}

		/**
		 * returns the display value shown in the combo box
		 * 
		 * @return the string 'Double'
		 */
		@Override
		public String toString() {
			return MongoBundle.getString(MongoBundle.Key.Double);
		}
	}

	/**
	 * a value type representing an Float object
	 */
	static class FloatValueType implements ValueType {

		/**
		 * installs an FloatDocument as the field's model
		 * 
		 * @param field
		 *            the text edit field to install the model
		 */
		@Override
		public void installDocument(JTextField field) {
			try {
				String val = field.getText();
				field.setText("");
				field.setDocument(new FloatDocument());
				field.getDocument().insertString(0, val, null);
				field.setEnabled(true);
			} catch (BadLocationException ble) {
			}
		}

		/**
		 * get the field's values as an Float
		 * 
		 * @param field
		 *            the component that holds the float value
		 * @return an Float that is the value of the text field
		 */
		@Override
		public Object getValue(JTextField field) {
			return Float.valueOf(field.getText());
		}

		/**
		 * returns the display value shown in the combo box
		 * 
		 * @return the string 'Float'
		 */
		@Override
		public String toString() {
			return MongoBundle.getString(MongoBundle.Key.Float);
		}
	}

	/**
	 * a value type representing an String object
	 */
	static class StringValueType implements ValueType {

		/**
		 * installs an PlainDocument as the field's model
		 * 
		 * @param field
		 *            the text edit field to install the model
		 */
		@Override
		public void installDocument(JTextField field) {
			try {
				String val = field.getText();
				field.setText("");
				field.setDocument(new PlainDocument());
				field.getDocument().insertString(0, val, null);
				field.setEnabled(true);
			} catch (BadLocationException ble) {
			}
		}

		/**
		 * get the field's values as an String
		 * 
		 * @param field
		 *            the component that holds the string value
		 * @return an String that is the value of the text field
		 */
		@Override
		public Object getValue(JTextField field) {
			return field.getText();
		}

		/**
		 * returns the display value shown in the combo box
		 * 
		 * @return the string 'String'
		 */
		@Override
		public String toString() {
			return MongoBundle.getString(MongoBundle.Key.String);
		}
	}

	/**
	 * a value type representing a Date object
	 */
	static class DateValueType implements ValueType {

		/**
		 * installs an PlainDocument as the field's model
		 * 
		 * @param field
		 *            the text edit field to install the model
		 */
		@Override
		public void installDocument(JTextField field) {
			try {
				String val = field.getText();
				field.setText("");
				field.setDocument(new DateDocument());
				field.getDocument().insertString(0, val, null);
				field.setEnabled(true);
			} catch (BadLocationException ble) {
			}
		}

		/**
		 * get the field's values as an String
		 * 
		 * @param field
		 *            the component that holds the string value
		 * @return an Date that is the value of the text field
		 */
		@Override
		public Object getValue(JTextField field) {
			Pattern p = Pattern.compile(MongoBundle.getString(MongoBundle.Key.DateRegex));

			Calendar c = Calendar.getInstance();

			Matcher m = p.matcher(field.getText());
			if (m.matches() || m.hitEnd()) {
				c.clear();

				int groupCounts = m.groupCount();
				if (groupCounts > 0) {
					String[] monthVals = MongoBundle.getString(MongoBundle.Key.MonthValues).split(",");
					String month = m.group(1);
					for (String mv : monthVals) {
						if (month.startsWith(mv)) {
							c.set(Calendar.MONTH, Integer.parseInt(mv.split(":")[1]));
						}
					}
					if (groupCounts > 1) {
						c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(m.group(2)));
						if (groupCounts > 2) {
							c.set(Calendar.YEAR, Integer.parseInt(m.group(3)));
							if (groupCounts > 3) {
								String[] time = m.group(4).split(":");

								if (time.length > 0) {
									c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
									if (time.length > 1) {
										c.set(Calendar.MINUTE, Integer.parseInt(time[1]));
										if (time.length > 2) {
											c.set(Calendar.SECOND, Integer.parseInt(time[2]));
										}
									}
								}
							}
						}
					}
				}
			}

			return c.getTime();
		}

		/**
		 * returns the display value shown in the combo box
		 * 
		 * @return the string 'String'
		 */
		@Override
		public String toString() {
			return MongoBundle.getString(MongoBundle.Key.Date);
		}
	}

	/**
	 * a value type representing an BasicDBObject object
	 */
	static class ObjectValueType implements ValueType {

		/**
		 * installs an PlainDocument as the field's model and disables the field
		 * 
		 * @param field
		 *            the text edit field to install the model
		 */
		@Override
		public void installDocument(JTextField field) {
			field.setText("");
			field.setDocument(new PlainDocument());
			field.setEnabled(false);
		}

		/**
		 * get the field's values as an BasicDBObject
		 * 
		 * @param field
		 *            the component that holds the string value
		 * @return an String that is the value of the text field
		 */
		@Override
		public Object getValue(JTextField field) {
			return new BasicDBObject();
		}

		/**
		 * returns the display value shown in the combo box
		 * 
		 * @return the string 'Object'
		 */
		@Override
		public String toString() {
			return MongoBundle.getString(MongoBundle.Key.Object);
		}
	}
}
