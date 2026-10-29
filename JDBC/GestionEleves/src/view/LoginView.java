package view;

import controller.AuthController;
import model.Utilisateur;
import util.IconProvider;
import util.ThemeUtil;

import javax.swing.*;
import java.awt.*;

// Fenêtre de connexion avec fond dégradé + mode création de compte
public class LoginView extends JFrame {

    private final AuthController authController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;
    private JPanel card;
    private JPanel loginPanel;
    private JPanel createPanel;
    private JRadioButton loginMode, createMode;
    private JTextField createNameField, createUsernameField;
    private JPanel createPassPanel, createConfirmPanel, createAdminPassPanel;
    private boolean isCreateMode = false;

    public LoginView(AuthController authController) {
        this.authController = authController;
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestion Elèves - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ThemeUtil.BG_DARK,
                        0, getHeight(), new Color(13, 71, 161));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        card = new ThemeUtil.RoundedPanel(16, ThemeUtil.BG_WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 25, 40));

        // Logo + title
        JLabel logoLabel = new JLabel(IconProvider.logoBlue());
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logoLabel);
        card.add(Box.createVerticalStrut(8));

        JLabel titleLabel = new JLabel("Gestion des Elèves", SwingConstants.CENTER);
        titleLabel.setFont(ThemeUtil.FONT_TITLE);
        titleLabel.setForeground(ThemeUtil.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(20));

        // Mode toggle
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        modePanel.setBackground(ThemeUtil.BG_WHITE);
        modePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modePanel.setMaximumSize(new Dimension(340, 32));

        ButtonGroup modeGroup = new ButtonGroup();
        loginMode = new JRadioButton("Se connecter", true);
        loginMode.setFont(ThemeUtil.FONT_BODY);
        loginMode.setBackground(ThemeUtil.BG_WHITE);
        loginMode.setFocusPainted(false);
        loginMode.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createMode = new JRadioButton("Créer un compte");
        createMode.setFont(ThemeUtil.FONT_BODY);
        createMode.setBackground(ThemeUtil.BG_WHITE);
        createMode.setFocusPainted(false);
        createMode.setCursor(new Cursor(Cursor.HAND_CURSOR));
        modeGroup.add(loginMode); modeGroup.add(createMode);
        modePanel.add(loginMode); modePanel.add(createMode);
        card.add(modePanel);
        card.add(Box.createVerticalStrut(16));

        // Login panel
        loginPanel = buildLoginPanel();
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginPanel);

        // Create account panel
        createPanel = buildCreatePanel();
        createPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createPanel.setVisible(false);
        card.add(createPanel);

        // Error label
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(ThemeUtil.FONT_SMALL);
        errorLabel.setForeground(ThemeUtil.DANGER);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(12));

        // Action button
        loginButton = ThemeUtil.createPrimaryButton("  Connexion  ");
        loginButton.setMaximumSize(new Dimension(300, 44));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            if (isCreateMode) handleCreateAccount();
            else handleLogin();
        });
        card.add(loginButton);
        card.add(Box.createVerticalStrut(8));

        JButton quitBtn = new JButton("Quitter");
        quitBtn.setFont(ThemeUtil.FONT_SMALL);
        quitBtn.setForeground(ThemeUtil.TEXT_SECONDARY);
        quitBtn.setContentAreaFilled(false);
        quitBtn.setBorderPainted(false);
        quitBtn.setFocusPainted(false);
        quitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitBtn.addActionListener(e -> System.exit(0));
        card.add(quitBtn);

        // Mode listeners
        loginMode.addActionListener(e -> switchMode(false));
        createMode.addActionListener(e -> switchMode(true));

        background.add(card);
        pack();
        setSize(440, isCreateMode ? 630 : 500);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(loginButton);
    }

    private JPanel buildLoginPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(ThemeUtil.BG_WHITE);
        p.setMaximumSize(new Dimension(340, 200));

        JLabel userLbl = new JLabel("Nom d'utilisateur");
        userLbl.setFont(ThemeUtil.FONT_SMALL);
        userLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        userLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(userLbl);
        p.add(Box.createVerticalStrut(5));

        usernameField = ThemeUtil.createStyledTextField(20);
        usernameField.setMaximumSize(new Dimension(320, 42));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(usernameField);
        p.add(Box.createVerticalStrut(14));

        JLabel passLbl = new JLabel("Mot de passe");
        passLbl.setFont(ThemeUtil.FONT_SMALL);
        passLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        passLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(passLbl);
        p.add(Box.createVerticalStrut(5));

        JPanel pwPanel = ThemeUtil.createPasswordFieldWithToggle(20);
        pwPanel.setMaximumSize(new Dimension(320, 42));
        pwPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField = ThemeUtil.getPasswordFieldFromPanel(pwPanel);
        p.add(pwPanel);

        return p;
    }

    private JPanel buildCreatePanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(ThemeUtil.BG_WHITE);
        p.setMaximumSize(new Dimension(340, 400));

        JLabel nameLbl = new JLabel("Nom complet");
        nameLbl.setFont(ThemeUtil.FONT_SMALL);
        nameLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(nameLbl);
        p.add(Box.createVerticalStrut(5));
        createNameField = ThemeUtil.createStyledTextField(20);
        createNameField.setMaximumSize(new Dimension(320, 42));
        createNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(createNameField);
        p.add(Box.createVerticalStrut(12));

        JLabel userLbl = new JLabel("Nom d'utilisateur");
        userLbl.setFont(ThemeUtil.FONT_SMALL);
        userLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        userLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(userLbl);
        p.add(Box.createVerticalStrut(5));
        createUsernameField = ThemeUtil.createStyledTextField(20);
        createUsernameField.setMaximumSize(new Dimension(320, 42));
        createUsernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(createUsernameField);
        p.add(Box.createVerticalStrut(12));

        JLabel passLbl = new JLabel("Mot de passe");
        passLbl.setFont(ThemeUtil.FONT_SMALL);
        passLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        passLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(passLbl);
        p.add(Box.createVerticalStrut(5));
        createPassPanel = ThemeUtil.createPasswordFieldWithToggle(20);
        createPassPanel.setMaximumSize(new Dimension(320, 42));
        createPassPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(createPassPanel);
        p.add(Box.createVerticalStrut(12));

        JLabel confLbl = new JLabel("Confirmer le mot de passe");
        confLbl.setFont(ThemeUtil.FONT_SMALL);
        confLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        confLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(confLbl);
        p.add(Box.createVerticalStrut(5));
        createConfirmPanel = ThemeUtil.createPasswordFieldWithToggle(20);
        createConfirmPanel.setMaximumSize(new Dimension(320, 42));
        createConfirmPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(createConfirmPanel);
        p.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeUtil.BORDER);
        sep.setMaximumSize(new Dimension(320, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(sep);
        p.add(Box.createVerticalStrut(12));

        JLabel adminLbl = new JLabel("Mot de passe administrateur (obligatoire)");
        adminLbl.setFont(ThemeUtil.FONT_SMALL);
        adminLbl.setForeground(ThemeUtil.DANGER);
        adminLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(adminLbl);
        p.add(Box.createVerticalStrut(5));
        createAdminPassPanel = ThemeUtil.createPasswordFieldWithToggle(20);
        createAdminPassPanel.setMaximumSize(new Dimension(320, 42));
        createAdminPassPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(createAdminPassPanel);

        return p;
    }

    private void switchMode(boolean create) {
        isCreateMode = create;
        loginPanel.setVisible(!create);
        createPanel.setVisible(create);
        loginButton.setText(create ? "  Créer le compte  " : "  Connexion  ");
        errorLabel.setVisible(false);
        setSize(440, create ? 660 : 500);
        setLocationRelativeTo(null);
        if (create) createNameField.requestFocus();
        else usernameField.requestFocus();
    }

    private void handleLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());
        if (u.isEmpty() || p.isEmpty()) { showError("Veuillez remplir tous les champs."); return; }

        Utilisateur user = authController.login(u, p);
        if (user != null) {
            errorLabel.setVisible(false);
            new MainView(user).setVisible(true);
            dispose();
        } else {
            showError("Nom d'utilisateur ou mot de passe incorrect.");
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void handleCreateAccount() {
        String nom = createNameField.getText().trim();
        String username = createUsernameField.getText().trim();
        String pass = new String(ThemeUtil.getPasswordFieldFromPanel(createPassPanel).getPassword());
        String confirm = new String(ThemeUtil.getPasswordFieldFromPanel(createConfirmPanel).getPassword());
        String adminPass = new String(ThemeUtil.getPasswordFieldFromPanel(createAdminPassPanel).getPassword());

        if (nom.isEmpty() || username.isEmpty() || pass.isEmpty() || confirm.isEmpty() || adminPass.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }
        if (pass.length() < 4) {
            showError("Le mot de passe doit faire au moins 4 caractères.");
            return;
        }
        if (!pass.equals(confirm)) {
            showError("Les mots de passe ne correspondent pas.");
            return;
        }
        if (!authController.verifyAdminPassword(adminPass)) {
            showError("Mot de passe administrateur incorrect.");
            ThemeUtil.getPasswordFieldFromPanel(createAdminPassPanel).setText("");
            return;
        }

        Utilisateur u = new Utilisateur(nom, username, pass, "USER");
        if (authController.createUser(u)) {
            JOptionPane.showMessageDialog(this, "Compte créé avec succès !\nVous pouvez maintenant vous connecter.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            switchMode(false);
            usernameField.setText(username);
            passwordField.setText("");
            passwordField.requestFocus();
        } else {
            showError("Erreur : l'utilisateur existe peut-être déjà.");
        }
    }

    private void showError(String msg) { errorLabel.setText(msg); errorLabel.setVisible(true); }
}
