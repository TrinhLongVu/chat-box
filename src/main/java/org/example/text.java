package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class text extends JFrame {

    private JList<String> jList;
    private DefaultListModel<String> listModel;

    public text() {
        // Set the title of the JFrame
        setTitle("JList Example");

        // Create an array of data for the JList
        String[] data = {"1", "2", "3", "4"};

        // Create a DefaultListModel to dynamically add/remove items
        listModel = new DefaultListModel<>();
        for (String item : data) {
            listModel.addElement(item);
        }

        // Create a JList with the DefaultListModel
        jList = new JList<>(listModel);

        // Set the selection mode to allow multiple selections
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Create a JScrollPane to hold the JList in case there are too many items to fit
        JScrollPane scrollPane = new JScrollPane(jList);

        // Create a button
        JButton displayButton = new JButton("Display Selected Items");

        // Add an ActionListener to the button
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected values from the JList
                int[] selectedIndices = jList.getSelectedIndices();

                // Display the selected values (you can modify this part as needed)
                StringBuilder selectedItems = new StringBuilder("Selected Items: ");
                for (int index : selectedIndices) {
                    selectedItems.append(listModel.getElementAt(index)).append(" ");
                }
                JOptionPane.showMessageDialog(text.this, selectedItems.toString());
            }
        });

        // Create a panel to hold the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(displayButton);

        // Set layout manager for the JFrame
        setLayout(new BorderLayout());

        // Add the JScrollPane and button panel to the JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set the default close operation and size of the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        // Set the JFrame to be visible
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the GUI code on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new text());
    }
}

