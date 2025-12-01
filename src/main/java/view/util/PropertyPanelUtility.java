package view.util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PropertyPanelUtility {
    public static TitledBorder makeSectionBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 11),
                Color.WHITE
        );
    }

    public static JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(makeSectionBorder(title));
        return panel;
    }

    public static GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        return gbc;
    }

    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);        // all main labels white
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        return label;
    }

    // Small inline labels like "X:" / "Y:"
    public static JLabel createInlineLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);        // make them visible
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        return label;
    }

    public static JLabel createSubHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 11f));
        return label;
    }

    public static JTextField smallField(String defaultText) {
        JTextField field = new JTextField(defaultText);

        // keep these boxes narrow
        Dimension size = new Dimension(89, 22);
        field.setPreferredSize(size);
        field.setMinimumSize(size);
        field.setMaximumSize(size);

        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        return field;
    }

    public static void styleCombo(JComboBox<String> combo) {
        // 1. Define the Font ONCE to ensure consistency
        // Using Arial Plain 12 to match your other inputs
        Font inputFont = new Font("Arial", Font.PLAIN, 11);
        combo.setFont(inputFont);

        // 2. Set the Custom UI (Background/Arrow fix)
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(new Color(60, 60, 60)); // Dark Grey Background
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            protected JButton createArrowButton() {
                javax.swing.plaf.basic.BasicArrowButton button = new javax.swing.plaf.basic.BasicArrowButton(
                        javax.swing.plaf.basic.BasicArrowButton.SOUTH,
                        new Color(60, 60, 60),
                        new Color(60, 60, 60),
                        Color.WHITE,
                        new Color(60, 60, 60)
                );
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });

        // 3. Set Component Colors
        combo.setBackground(new Color(60, 60, 60));
        combo.setForeground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        combo.setOpaque(true);

        // 4. Set the Renderer (The Critical Fix for Fonts)
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // CRITICAL: Force the renderer to use the same font as the main box
                setFont(inputFont);

                if (isSelected) {
                    setBackground(new Color(80, 80, 80));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(new Color(60, 60, 60));
                    setForeground(Color.WHITE);
                }
                setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                return this;
            }
        });
    }

    public static JButton createAddButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                // Enable anti-aliasing for smooth edges
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // A. Draw Background
                if (getModel().isPressed()) {
                    g2.setColor(new Color(40, 40, 40)); // Darker when pressed
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(70, 70, 70)); // Lighter when hovered
                } else {
                    g2.setColor(getBackground()); // Default
                }
                g2.fillRect(0, 0, getWidth(), getHeight());

                // B. Draw the Plus (+) Sign
                g2.setColor(getForeground());

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int armLength = 4; // How long the arms are from the center (total width = 8)
                int thickness = 2; // Thickness of the lines

                // Draw Horizontal Bar
                g2.fillRect(centerX - armLength, centerY - (thickness / 2), armLength * 2, thickness);

                // Draw Vertical Bar
                g2.fillRect(centerX - (thickness / 2), centerY - armLength, thickness, armLength * 2);

                g2.dispose();
            }
        };

        // 2. Basic Properties
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false); // No dotted line
        button.setBorder(BorderFactory.createLineBorder(new Color(131, 131, 131)));

        // 3. Colors (Used by the custom paintComponent above)
        button.setBackground(new Color(57, 57, 57));
        button.setForeground(Color.WHITE);

        // 4. Size explicitly (Icon size)
        button.setPreferredSize(new Dimension(18, 18));

        return button;
    }

    public static JButton createDeleteButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                // Enable anti-aliasing for smooth edges
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // A. Draw Background
                if (getModel().isPressed()) {
                    g2.setColor(new Color(40, 40, 40)); // Darker when pressed
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(70, 70, 70)); // Lighter when hovered
                } else {
                    g2.setColor(getBackground()); // Default
                }
                g2.fillRect(0, 0, getWidth(), getHeight());

                // B. Draw the Plus (+) Sign
                g2.setColor(getForeground());

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int armLength = 4; // How long the arms are from the center (total width = 8)
                int thickness = 2; // Thickness of the lines

                // Draw Horizontal Bar
                g2.fillRect(centerX - armLength, centerY - (thickness / 2), armLength * 2, thickness);

                g2.dispose();
            }
        };

        // 2. Basic Properties
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false); // No dotted line
        button.setBorder(BorderFactory.createLineBorder(new Color(131, 131, 131)));

        // 3. Colors (Used by the custom paintComponent above)
        button.setBackground(new Color(57, 57, 57));
        button.setForeground(Color.WHITE);

        // 4. Size explicitly (Icon size)
        button.setPreferredSize(new Dimension(18, 18));

        return button;
    }

    public static JButton createEditButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                // Enable anti-aliasing for smooth edges
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // A. Draw Background
                if (getModel().isPressed()) {
                    g2.setColor(new Color(40, 40, 40)); // Darker when pressed
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(70, 70, 70)); // Lighter when hovered
                } else {
                    g2.setColor(getBackground()); // Default
                }
                g2.fillRect(0, 0, getWidth(), getHeight());

                // B. Draw the Edit Symbol (Pencil)
                g2.setColor(getForeground());

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                // Rotate 45 degrees to make the pencil diagonal
                g2.rotate(Math.toRadians(45), centerX, centerY);

                // 1. Draw Body (Rectangle)
                // Width 4, Height 6
                g2.fillRect(centerX - 2, centerY - 3, 4, 6);

                // 2. Draw Tip (Triangle) - Pointing 'Up' relative to rotation
                Polygon tip = new Polygon();
                tip.addPoint(centerX - 2, centerY - 3); // Base Left
                tip.addPoint(centerX + 2, centerY - 3); // Base Right
                tip.addPoint(centerX, centerY - 7);     // Tip Point
                g2.fill(tip);

                // 3. Draw Eraser/Cap (Small Rectangle at bottom)
                g2.fillRect(centerX - 2, centerY + 4, 4, 2);

                g2.dispose();
            }
        };

        // 2. Basic Properties
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(131, 131, 131)));

        // 3. Colors
        button.setBackground(new Color(57, 57, 57));
        button.setForeground(Color.WHITE);

        // 4. Size explicitly (Icon size)
        button.setPreferredSize(new Dimension(18, 18));

        return button;
    }
}
