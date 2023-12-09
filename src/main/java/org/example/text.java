package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class text {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Panel Switching Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            CardLayout cardLayout = new CardLayout();
            mainPanel.setLayout(cardLayout);

            JPanel panel1 = createPanel("Panel 1", Color.RED);
            JPanel panel2 = createPanel("Panel 2", Color.BLUE);

            mainPanel.add(panel1, "Panel 1");
            mainPanel.add(panel2, "Panel 2");

            panel1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(mainPanel, "Panel 2");
                }
            });

            frame.getContentPane().setBackground(Color.WHITE);
            frame.add(mainPanel);

            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static JPanel createPanel(String label, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(jLabel);
        return panel;
    }
}
