/*
 * mongobrowser - a webstart gui application for viewing, 
 *                editing and administering a Mongo Database
 * Copyright 2009-2010 MeBigFatGuy.com
 * Copyright 2009-2010 Dave Brosius
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTree;

import com.mebigfatguy.mongobrowser.MongoBundle;
import com.mebigfatguy.mongobrowser.MongoContext;
import com.mebigfatguy.mongobrowser.actions.ConnectAction;
import com.mebigfatguy.mongobrowser.actions.DisconnectAction;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * the main frame of the browser
 */
public class MongoBrowserFrame extends JFrame {

    private static final long serialVersionUID = 8152287910101198703L;
    private JMenuItem connectItem;
    private JMenuItem disconnectItem;
    private MongoControlPanel ctrlPanel;
    private MongoDataPanel dataPanel;
    private Mediator mediator = new Mediator();
    
    /**
     * constructs the main frame
     */
    public MongoBrowserFrame() {
        super(MongoBundle.getString(MongoBundle.Key.Title));
        initComponents();
        initMenus();
        initListeners();
        pack();
    }

    private void initComponents() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(4, 4));
        ctrlPanel = new MongoControlPanel(mediator);
        cp.add(ctrlPanel, BorderLayout.NORTH);
        dataPanel = new MongoDataPanel(mediator);
        cp.add(dataPanel, BorderLayout.CENTER);
    }
    
    private void initMenus() {
        
        JMenuBar mb = new JMenuBar();
        JMenu databasesMenu = new JMenu(MongoBundle.getString(MongoBundle.Key.Servers));
        connectItem = new JMenuItem(new ConnectAction(mediator));
        databasesMenu.add(connectItem);
        disconnectItem = new JMenuItem(new DisconnectAction(mediator));
        mb.add(databasesMenu);    
        databasesMenu.add(disconnectItem);
        
        setJMenuBar(mb);
    }
    
    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                System.exit(0);
            }
        });
    }
    
    /**
     * a mediator for all the actions that occur in the frame
     */
    class Mediator implements MongoContext {
        
        private JTree activeTree;
        private MongoTreeNode activeNode;
        private Mongo activeServer;
        private DB activeDatabase;
        
        @Override
        public JTree getTree() {
            return activeTree;
        }

        @Override
        public void setTree(JTree tree) {
            activeTree = tree;
        }
        
        @Override
        public MongoTreeNode getSelectedNode() {
            return activeNode;
        }
        
        @Override
        public void setSelectedNode(MongoTreeNode node) {
            activeNode = node;
            ctrlPanel.adjustEnabled(node);
        }

        @Override
        public Mongo getServer() {
            return activeServer;
        }
    
        @Override
        public void setServer(Mongo server) {
            activeServer = server;
            connectItem.setEnabled(server == null);
            disconnectItem.setEnabled(server != null);
            if (server != null) {
                ctrlPanel.init();
                dataPanel.init();
            } else {
                ctrlPanel.term();
                dataPanel.term();
            }
        }
        
        public DB getDatabase() {
            return activeDatabase;
        }
        
        public void setDatabase(DB database) {
            activeDatabase = database;
            if (activeDatabase != null) 
                dataPanel.init();
            else
                dataPanel.term();
        }
    }

    public void startupConnection(final String host, final int port)
            throws UnknownHostException, MongoException {
        
        try {
            mediator.setServer(new Mongo(host, port));
        } catch (Exception e) {
            
            connectItem.setEnabled(true);
            disconnectItem.setEnabled(false);
            ctrlPanel.term();
            dataPanel.term();
            
            if (e instanceof UnknownHostException)
                throw (UnknownHostException) e;
            else if (e instanceof MongoException)
                throw (MongoException) e;
        }
    }
    
}
