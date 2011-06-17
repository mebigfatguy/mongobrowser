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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.actions.DeleteAction;
import com.mebigfatguy.mongobrowser.actions.NewCollectionAction;
import com.mebigfatguy.mongobrowser.actions.NewKeyValueAction;
import com.mebigfatguy.mongobrowser.actions.NewObjectAction;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * the panel the houses the button bar for all the controls
 */
public class MongoControlPanel extends JPanel implements MongoPanel {

	private static final long serialVersionUID = 1439280424726915624L;
	private final MongoContext context;
	private JComboBox dbComboBox;
	private JButton dbNewCollectionButton;
	private JButton dbNewObjectButton;
	private JButton dbNewKeyValueButton;
	private JButton dbDeleteButton;

	/**
	 * constructs the button bar panel
	 * 
	 * @param ctxt
	 *            the mediator object for the dialog
	 */
	public MongoControlPanel(MongoContext ctxt) {
		context = ctxt;
		initComponents();
		initListeners();
	}

	@Override
	public void init() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Mongo db = context.getServer();
				List<String> databases = db.getDatabaseNames();
				DefaultComboBoxModel model = (DefaultComboBoxModel) dbComboBox
						.getModel();
				for (String database : databases) {
					model.addElement(database);
				}
				model.addElement(new Object() {
					@Override
					public String toString() {
						return "New Database...";
					}
				});
				dbComboBox.setEnabled(true);
				context.setSelectedNode(null);
			}
		});

	}

	@Override
	public void term() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DefaultComboBoxModel model = (DefaultComboBoxModel) dbComboBox
						.getModel();
				model.removeAllElements();
				dbComboBox.setEnabled(false);
				context.setSelectedNode(null);
			}
		});
	}

	/**
	 * resets the enabled state of all the controls based on new selection
	 * 
	 * @param selectedNode
	 *            the tree node that is currently selected
	 */
	public void adjustEnabled(MongoTreeNode selectedNode) {
		if (selectedNode == null) {
			dbNewCollectionButton.setEnabled(true);
			dbNewObjectButton.setEnabled(false);
			dbNewKeyValueButton.setEnabled(false);
			dbDeleteButton.setEnabled(false);
		} else {
			switch (selectedNode.getType()) {
			case Collection:
				dbNewCollectionButton.setEnabled(true);
				dbNewKeyValueButton.setEnabled(false);
				if (selectedNode.isReadOnly()) {
					dbNewObjectButton.setEnabled(false);
					dbDeleteButton.setEnabled(false);
				} else {
					dbNewObjectButton.setEnabled(true);
					dbDeleteButton.setEnabled(true);
				}

				break;

			case Object:
				dbNewCollectionButton.setEnabled(true);
				dbNewObjectButton.setEnabled(false);
				if (selectedNode.isReadOnly()) {
					dbNewKeyValueButton.setEnabled(false);
					dbDeleteButton.setEnabled(false);
				} else {
					dbNewKeyValueButton.setEnabled(true);
					dbDeleteButton.setEnabled(true);
				}
				break;

			case KeyValue:
				dbNewCollectionButton.setEnabled(true);
				dbNewObjectButton.setEnabled(false);

				if (selectedNode.isReadOnly()) {
					dbNewKeyValueButton.setEnabled(false);
					dbDeleteButton.setEnabled(false);
				} else {
					MongoTreeNode.KV kv = (MongoTreeNode.KV) selectedNode
							.getUserObject();
					Object value = kv.getValue();
					dbNewKeyValueButton.setEnabled(value instanceof DBObject);
					dbDeleteButton.setEnabled(!kv.getKey().startsWith("_"));
				}
				break;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		Paint savePaint = g2d.getPaint();
		try {
			GradientPaint gp = new GradientPaint(0, 0, getBackground(), 0,
					getHeight(), Color.GRAY);

			g2d.setPaint(gp);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			super.setOpaque(false);
			super.paintComponent(g);
		} finally {
			super.setOpaque(true);
			g2d.setPaint(savePaint);
		}
	}

	private void initComponents() {
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		setLayout(new FormLayout(
				"3dlu, pref, 1dlu, 200px:grow, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref",
				"pref"));
		CellConstraints cc = new CellConstraints();

		JLabel dbLabel = new JLabel(
				MongoBundle.getString(MongoBundle.Key.Database));
		dbComboBox = new JComboBox(new DefaultComboBoxModel());
		dbComboBox.setEnabled(false);
		dbLabel.setLabelFor(dbComboBox);
		add(dbLabel, cc.xy(2, 1));
		add(dbComboBox, cc.xy(4, 1));

		dbNewCollectionButton = new JButton(new NewCollectionAction(context));
		ImageIcon icon = new ImageIcon(
				MongoControlPanel.class
						.getResource("/com/mebigfatguy/mongobrowser/resources/newcollection.png"));
		dbNewCollectionButton.setIcon(icon);
		dbNewCollectionButton.setText("");
		dbNewCollectionButton.setPreferredSize(new Dimension(icon
				.getIconWidth(), icon.getIconHeight()));
		dbNewCollectionButton.setToolTipText(MongoBundle
				.getString(MongoBundle.Key.NewCollection));
		add(dbNewCollectionButton, cc.xy(6, 1));
		dbNewCollectionButton.setEnabled(false);

		dbNewObjectButton = new JButton(new NewObjectAction(context));
		icon = new ImageIcon(
				MongoControlPanel.class
						.getResource("/com/mebigfatguy/mongobrowser/resources/newobject.png"));
		dbNewObjectButton.setIcon(icon);
		dbNewObjectButton.setText("");
		dbNewObjectButton.setPreferredSize(new Dimension(icon.getIconWidth(),
				icon.getIconHeight()));
		dbNewObjectButton.setToolTipText(MongoBundle
				.getString(MongoBundle.Key.NewObject));
		add(dbNewObjectButton, cc.xy(8, 1));
		dbNewObjectButton.setEnabled(false);

		dbNewKeyValueButton = new JButton(new NewKeyValueAction(context));
		icon = new ImageIcon(
				MongoControlPanel.class
						.getResource("/com/mebigfatguy/mongobrowser/resources/newkeyvalue.png"));
		dbNewKeyValueButton.setIcon(icon);
		dbNewKeyValueButton.setText("");
		dbNewKeyValueButton.setPreferredSize(new Dimension(icon.getIconWidth(),
				icon.getIconHeight()));
		dbNewKeyValueButton.setToolTipText(MongoBundle
				.getString(MongoBundle.Key.NewKeyValue));
		add(dbNewKeyValueButton, cc.xy(10, 1));
		dbNewKeyValueButton.setEnabled(false);

		dbDeleteButton = new JButton(new DeleteAction(context));
		icon = new ImageIcon(
				MongoControlPanel.class
						.getResource("/com/mebigfatguy/mongobrowser/resources/delete.png"));
		dbDeleteButton.setIcon(icon);
		dbDeleteButton.setText(null);
		dbDeleteButton.setPreferredSize(new Dimension(icon.getIconWidth(), icon
				.getIconHeight()));
		dbDeleteButton.setToolTipText(MongoBundle
				.getString(MongoBundle.Key.Delete));
		add(dbDeleteButton, cc.xy(12, 1));
		dbDeleteButton.setEnabled(false);
	}

	private void initListeners() {
		dbComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					Object sel = dbComboBox.getSelectedItem();
					if (sel instanceof String) {
						context.setDatabase(context.getServer().getDB(
								(String) sel));
					} else {
						String dbName = JOptionPane.showInputDialog(MongoBundle
								.getString(MongoBundle.Key.NewDatabase));
						if (dbName != null) {
							context.setDatabase(context.getServer().getDB(
									dbName));
							DefaultComboBoxModel model = (DefaultComboBoxModel) dbComboBox
									.getModel();
							model.insertElementAt(dbName, model.getSize() - 1);
						}
					}
				} else {
					context.setDatabase(null);
				}
			}

		});
	}

}
