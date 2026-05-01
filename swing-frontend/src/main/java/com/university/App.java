package com.university;

import com.formdev.flatlaf.FlatDarkLaf;
import com.university.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set Dark Mode Look and Feel for premium feel
            FlatDarkLaf.setup();
            
            // Customize some UI defaults to match React app's primary-900 look
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("ScrollBar.thumbArc", 12);
            
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
