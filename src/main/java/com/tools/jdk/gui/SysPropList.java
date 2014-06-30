package com.tools.jdk.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

/**
 * Generic main program.
 */
public class SysPropList {
    public static void main(String[] args) {
        JFrame window = new JFrame("System Properties");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new SysPropListGUI());
        window.pack();
        window.setVisible(true);
    }
}

/**
 * Panel to display the (limited) GUI intereface.
 */
class SysPropListGUI extends JPanel {
    JTextArea m_propertiesTA = new JTextArea(40, 60);

    /**
     * Constructor sets layout, adds component(s), sets values
     */
    public SysPropListGUI() {
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(m_propertiesTA), BorderLayout.CENTER);

        // ... Add property list data to text area.
        Properties pr = System.getProperties();
        TreeSet propKeys = new TreeSet(pr.keySet()); // TreeSet sorts keys
        for (Iterator it = propKeys.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            m_propertiesTA.append("" + key + "=" + pr.get(key) + "\n");
        }
    }
}