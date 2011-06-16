package com.mebigfatguy.mongobrowser.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
		setTitle(MongoBundle.getString(MongoBundle.Key.NewKeyValue));
		initComponents();
		initListeners();
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
		DefaultComboBoxModel model = (DefaultComboBoxModel)valueTypeBox.getModel();
		model.addElement(new IntegerValueType());
		model.addElement(new DoubleValueType());
		model.addElement(new FloatValueType());
		model.addElement(new StringValueType());
		model.addElement(new ObjectValueType());
		valueTypeBox.setSelectedIndex(2);
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
		
		return p;
	}
	
	/**
	 * installs the listeners
	 */
	private void initListeners() {
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ok = true;
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		
		valueTypeBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					ValueType vt = (ValueType)ie.getItem();
					vt.installDocument(valueField);
				}
			}
		});
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
		return ((ValueType)valueTypeBox.getSelectedItem()).getValue(valueField);
	}
	
	/**
	 * interface for items that are put into the Value type combobox
	 */
	interface ValueType {
		/**
		 * switch the JTextField's model based on the type of value
		 * 
		 * @param field the component who's document should be modified
		 */
		void installDocument(JTextField field);
		/**
		 * get the value object of the field, based on the type of this value
		 * 
		 * @param field the component to get the value from
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
		 * @param field the text edit field to install the model
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
		 * @param field the component that holds the integer value
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
		 * @param field the text edit field to install the model
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
		 * @param field the component that holds the double value
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
		 * @param field the text edit field to install the model
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
		 * @param field the component that holds the float value
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
		 * @param field the text edit field to install the model
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
		 * @param field the component that holds the string value
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
	 * a value type representing an BasicDBObject object
	 */
	static class ObjectValueType implements ValueType {
		
		/**
		 * installs an PlainDocument as the field's model
		 * and disables the field
		 * 
		 * @param field the text edit field to install the model
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
		 * @param field the component that holds the string value
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
