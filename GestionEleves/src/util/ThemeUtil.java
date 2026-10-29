package util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

// Thème graphique moderne : couleurs, polices, composants stylisés
public class ThemeUtil {

    // ===========================
    // Couleurs principales
    // ===========================
    public static final Color PRIMARY        = new Color(41, 98, 255);
    public static final Color PRIMARY_DARK   = new Color(30, 72, 190);
    public static final Color PRIMARY_LIGHT  = new Color(232, 240, 255);
    public static final Color PRIMARY_VERY_LIGHT = new Color(245, 248, 255);

    // ===========================
    // Couleurs sémantiques
    // ===========================
    public static final Color DANGER         = new Color(229, 57, 53);
    public static final Color DANGER_DARK    = new Color(183, 28, 28);
    public static final Color DANGER_LIGHT   = new Color(255, 235, 235);

    public static final Color SUCCESS        = new Color(46, 125, 50);
    public static final Color SUCCESS_DARK   = new Color(27, 94, 32);
    public static final Color SUCCESS_LIGHT  = new Color(232, 245, 233);

    public static final Color WARNING        = new Color(255, 160, 0);
    public static final Color WARNING_LIGHT  = new Color(255, 248, 225);

    public static final Color INFO           = new Color(2, 136, 209);
    public static final Color INFO_LIGHT     = new Color(225, 245, 254);

    // ===========================
    // Fonds
    // ===========================
    public static final Color BG_DARK        = new Color(18, 25, 45);
    public static final Color BG_DARK_ALT    = new Color(10, 15, 30);
    public static final Color BG_LIGHT       = new Color(245, 246, 250);
    public static final Color BG_LIGHT_ALT   = new Color(238, 240, 245);
    public static final Color BG_WHITE       = new Color(255, 255, 255);
    public static final Color BG_CARD        = new Color(255, 255, 255);

    // ===========================
    // Texte
    // ===========================
    public static final Color TEXT_PRIMARY   = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color TEXT_LIGHT     = new Color(255, 255, 255);
    public static final Color TEXT_HINT      = new Color(180, 180, 180);
    public static final Color TEXT_DISABLED  = new Color(200, 200, 200);

    // ===========================
    // Bordures
    // ===========================
    public static final Color BORDER         = new Color(224, 224, 224);
    public static final Color BORDER_FOCUS   = PRIMARY;
    public static final Color BORDER_LIGHT   = new Color(238, 238, 242);

    // ===========================
    // Tableau
    // ===========================
    public static final Color TABLE_HEADER   = new Color(250, 250, 252);
    public static final Color TABLE_ROW_ODD  = BG_WHITE;
    public static final Color TABLE_ROW_EVEN = new Color(247, 248, 252);
    public static final Color TABLE_ROW_HOVER = new Color(232, 240, 255);
    public static final Color TABLE_SELECTION = new Color(41, 98, 255, 30);

    // ===========================
    // Sidebar
    // ===========================
    public static final Color SIDEBAR_BG     = new Color(18, 25, 45);
    public static final Color SIDEBAR_HOVER  = new Color(30, 40, 65);
    public static final Color SIDEBAR_ACTIVE = PRIMARY;
    public static final Color SIDEBAR_TEXT   = new Color(255, 255, 255, 160);
    public static final Color SIDEBAR_ICON   = new Color(255, 255, 255, 200);

    // ===========================
    // Graphiques / stats
    // ===========================
    public static final Color CHART_BLUE     = new Color(41, 98, 255);
    public static final Color CHART_GREEN    = new Color(0, 200, 83);
    public static final Color CHART_ORANGE   = new Color(255, 145, 0);
    public static final Color CHART_PURPLE   = new Color(156, 39, 176);
    public static final Color CHART_TEAL     = new Color(0, 150, 136);
    public static final Color CHART_PINK     = new Color(233, 30, 99);

    // ===========================
    // Polices
    // ===========================
    private static final String FONT_FAMILY = "SansSerif";
    public static final Font FONT_TITLE   = new Font(FONT_FAMILY, Font.BOLD, 26);
    public static final Font FONT_HEADING = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font FONT_SUBHEADING = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_BODY    = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_SMALL   = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_TINY    = new Font(FONT_FAMILY, Font.PLAIN, 10);
    public static final Font FONT_TABLE   = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_TABLE_HEADER = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_BUTTON  = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_SIDEBAR = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_METRIC  = new Font(FONT_FAMILY, Font.BOLD, 24);

    // ===========================
    // Composants stylisés
    // ===========================

    public static JButton createPrimaryButton(String text) {
        return createColoredButton(text, PRIMARY, PRIMARY_DARK, TEXT_LIGHT);
    }

    public static JButton createSuccessButton(String text) {
        return createColoredButton(text, SUCCESS, SUCCESS_DARK, TEXT_LIGHT);
    }

    public static JButton createDangerButton(String text) {
        return createColoredButton(text, DANGER, DANGER_DARK, TEXT_LIGHT);
    }

    public static JButton createWarningButton(String text) {
        return createColoredButton(text, WARNING, new Color(230, 140, 0), TEXT_LIGHT);
    }

    public static JButton createOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(PRIMARY);
        btn.setBackground(BG_WHITE);
        btn.setBorder(new RoundedBorder(8, PRIMARY));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY_LIGHT); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(BG_WHITE); }
        });
        return btn;
    }

    private static JButton createColoredButton(String text, Color bg, Color hover, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBorder(new RoundedBorder(8, bg));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(6, BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return field;
    }

    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(6, BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return field;
    }

    public static JPanel createPasswordFieldWithToggle(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(6, BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setEchoChar('\u2022');

        JButton toggle = new JButton(new EyeClosedIcon());
        toggle.setContentAreaFilled(false);
        toggle.setBorderPainted(false);
        toggle.setFocusPainted(false);
        toggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggle.setPreferredSize(new Dimension(28, 28));
        toggle.setToolTipText("Afficher le mot de passe");

        toggle.addActionListener(ev -> {
            if (field.getEchoChar() == 0) {
                field.setEchoChar('\u2022');
                toggle.setIcon(new EyeClosedIcon());
                toggle.setToolTipText("Afficher le mot de passe");
            } else {
                field.setEchoChar((char) 0);
                toggle.setIcon(new EyeOpenIcon());
                toggle.setToolTipText("Masquer le mot de passe");
            }
        });

        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(6, BORDER),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        panel.add(field, BorderLayout.CENTER);
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        btnWrap.setBackground(BG_WHITE);
        btnWrap.add(toggle);
        panel.add(btnWrap, BorderLayout.EAST);
        panel.putClientProperty("passwordField", field);
        return panel;
    }

    public static JPasswordField getPasswordFieldFromPanel(JPanel panel) {
        return (JPasswordField) panel.getClientProperty("passwordField");
    }

    public static void applyGlobalTheme() {
        UIManager.put("Panel.background", BG_LIGHT);
        UIManager.put("OptionPane.background", BG_LIGHT);
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
        UIManager.put("Label.font", FONT_BODY);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.font", FONT_BODY);
        UIManager.put("TextField.caretForeground", PRIMARY);
        UIManager.put("Table.font", FONT_TABLE);
        UIManager.put("Table.headerFont", FONT_TABLE_HEADER);
        UIManager.put("Table.rowHeight", 38);
        UIManager.put("Table.gridColor", new Color(235, 235, 240));
        UIManager.put("Table.selectionBackground", TABLE_SELECTION);
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("TableHeader.background", TABLE_HEADER);
        UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
        UIManager.put("TableHeader.font", FONT_TABLE_HEADER);
        UIManager.put("TableHeader.cellBorder", BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("ScrollPane.background", BG_LIGHT);
        UIManager.put("ComboBox.font", FONT_BODY);
    }

    // ===========================
    // Icônes de mot de passe (Java2D)
    // ===========================

    public static class EyeClosedIcon implements Icon {
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(TEXT_SECONDARY);
            g2.setStroke(new BasicStroke(1.8f));

            // eye outline
            g2.draw(new Ellipse2D.Double(x + 3, y + 5, 14, 11));

            // crossed line through eye
            g2.drawLine(x + 3, y + 5, x + 17, y + 16);

            g2.setStroke(new BasicStroke(1f));
            g2.draw(new Ellipse2D.Double(x + 8, y + 8, 4, 4));
            g2.dispose();
        }
        @Override public int getIconWidth() { return 20; }
        @Override public int getIconHeight() { return 20; }
    }

    public static class EyeOpenIcon implements Icon {
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(TEXT_SECONDARY);
            g2.setStroke(new BasicStroke(1.8f));

            // eye outline
            GeneralPath path = new GeneralPath();
            path.moveTo(x + 3, y + 10);
            path.curveTo(x + 6, y + 4, x + 14, y + 4, x + 17, y + 10);
            path.curveTo(x + 14, y + 16, x + 6, y + 16, x + 3, y + 10);
            path.closePath();
            g2.draw(path);

            // pupil
            g2.fill(new Ellipse2D.Double(x + 8, y + 8, 4, 4));
            g2.dispose();
        }
        @Override public int getIconWidth() { return 20; }
        @Override public int getIconHeight() { return 20; }
    }

    // ===========================
    // Classes utilitaires UI
    // ===========================

    public static class RoundedBorder implements Border {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) { return new Insets(radius/2, radius/2, radius/2, radius/2); }
        @Override
        public boolean isBorderOpaque() { return false; }
    }

    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }
}
