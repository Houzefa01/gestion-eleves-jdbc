package view;

import controller.AuthController;
import model.Utilisateur;
import util.IconProvider;
import util.ThemeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserManagementView extends JDialog {

    private final Window parent;
    private final AuthController authController;
    private final model.Utilisateur currentUser;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public UserManagementView(Window parent, model.Utilisateur currentUser) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.parent = parent;
        this.authController = new AuthController();
        this.currentUser = currentUser;
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        setTitle("Gestion des utilisateurs");
        setSize(750, 500);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        root.setBackground(ThemeUtil.BG_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ThemeUtil.BG_LIGHT);
        JLabel title = new JLabel("Gestion des utilisateurs", IconProvider.userBlue(), SwingConstants.LEFT);
        title.setFont(ThemeUtil.FONT_TITLE);
        title.setForeground(ThemeUtil.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        JButton addBtn = new JButton("  Ajouter", IconProvider.addWhite());
        addBtn.setFont(ThemeUtil.FONT_BUTTON);
        addBtn.setForeground(ThemeUtil.TEXT_LIGHT);
        addBtn.setBackground(ThemeUtil.SUCCESS);
        addBtn.setBorder(new ThemeUtil.RoundedBorder(8, ThemeUtil.SUCCESS));
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(140, 38));
        addBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { addBtn.setBackground(ThemeUtil.SUCCESS_DARK); }
            public void mouseExited(MouseEvent e) { addBtn.setBackground(ThemeUtil.SUCCESS); }
        });
        addBtn.addActionListener(e -> { showUserForm(null); loadUsers(); });
        header.add(addBtn, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);
        root.add(Box.createVerticalStrut(12), BorderLayout.CENTER);

        // Table
        String[] cols = {"ID", "Nom", "Nom d'utilisateur", "Rôle"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setRowHeight(38);
        userTable.setShowGrid(true);
        userTable.setGridColor(ThemeUtil.BORDER_LIGHT);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.getTableHeader().setPreferredSize(new Dimension(0, 38));
        userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, sel, foc, row, col);
                setFont(ThemeUtil.FONT_TABLE);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (sel) { setBackground(ThemeUtil.TABLE_SELECTION); setForeground(ThemeUtil.TEXT_PRIMARY); }
                else { setBackground(row%2==0 ? ThemeUtil.TABLE_ROW_EVEN : ThemeUtil.TABLE_ROW_ODD); setForeground(ThemeUtil.TEXT_PRIMARY); }
                return c;
            }
        });
        JTableHeader hdr = userTable.getTableHeader();
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, sel, foc, row, col);
                l.setFont(ThemeUtil.FONT_TABLE_HEADER);
                l.setBackground(ThemeUtil.TABLE_HEADER);
                l.setForeground(ThemeUtil.TEXT_SECONDARY);
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0,0,2,0,ThemeUtil.BORDER),
                        BorderFactory.createEmptyBorder(6,12,6,12)));
                return l;
            }
        });
        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { if (e.getClickCount()==2) handleEdit(); }
        });

        JScrollPane sp = new JScrollPane(userTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(ThemeUtil.BG_WHITE);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ThemeUtil.BG_WHITE);
        card.setBorder(new ThemeUtil.RoundedBorder(12, ThemeUtil.BORDER));
        card.add(sp, BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER, 1);

        // Footer
        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.setBackground(ThemeUtil.BG_LIGHT);
        footer.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(ThemeUtil.BG_LIGHT);

        JButton editBtn = new JButton("  Modifier", IconProvider.editBlue());
        editBtn.setFont(ThemeUtil.FONT_BUTTON);
        editBtn.setForeground(ThemeUtil.PRIMARY);
        editBtn.setBackground(ThemeUtil.BG_WHITE);
        editBtn.setBorder(new ThemeUtil.RoundedBorder(8, ThemeUtil.PRIMARY));
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setPreferredSize(new Dimension(140, 36));
        editBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { editBtn.setBackground(ThemeUtil.PRIMARY_LIGHT); }
            public void mouseExited(MouseEvent e) { editBtn.setBackground(ThemeUtil.BG_WHITE); }
        });
        editBtn.addActionListener(e -> { handleEdit(); loadUsers(); });
        btnPanel.add(editBtn);

        JButton delBtn = new JButton("  Supprimer", IconProvider.trashRed());
        delBtn.setFont(ThemeUtil.FONT_BUTTON);
        delBtn.setForeground(ThemeUtil.DANGER);
        delBtn.setBackground(ThemeUtil.BG_WHITE);
        delBtn.setBorder(new ThemeUtil.RoundedBorder(8, ThemeUtil.DANGER));
        delBtn.setFocusPainted(false);
        delBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        delBtn.setPreferredSize(new Dimension(150, 36));
        delBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { delBtn.setBackground(ThemeUtil.DANGER_LIGHT); }
            public void mouseExited(MouseEvent e) { delBtn.setBackground(ThemeUtil.BG_WHITE); }
        });
        delBtn.addActionListener(e -> { handleDelete(); loadUsers(); });
        btnPanel.add(delBtn);
        footer.add(btnPanel, BorderLayout.WEST);

        statusLabel = new JLabel("Prêt");
        statusLabel.setFont(ThemeUtil.FONT_SMALL);
        statusLabel.setForeground(ThemeUtil.TEXT_SECONDARY);
        footer.add(statusLabel, BorderLayout.EAST);

        JButton closeBtn = new JButton("Fermer");
        closeBtn.setFont(ThemeUtil.FONT_BUTTON);
        closeBtn.setForeground(ThemeUtil.TEXT_SECONDARY);
        closeBtn.setBackground(ThemeUtil.BG_LIGHT);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        footer.add(closeBtn, BorderLayout.SOUTH);

        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<Utilisateur> users = authController.getAllUsers();
        for (Utilisateur u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getNom(), u.getUsername(), u.getRole()});
        }
        statusLabel.setText(users.size() + " utilisateur(s)");
    }

    private void handleEdit() {
        int r = userTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur."); return; }
        int id = (int) tableModel.getValueAt(r, 0);
        Utilisateur u = authController.getUserById(id);
        if (u != null) showUserForm(u);
    }

    private void handleDelete() {
        int r = userTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur."); return; }
        int id = (int) tableModel.getValueAt(r, 0);
        String name = (String) tableModel.getValueAt(r, 1);
        String role = (String) tableModel.getValueAt(r, 3);

        if (role.equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Impossible de supprimer le compte administrateur.", "Interdit", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (id == currentUser.getId()) {
            JOptionPane.showMessageDialog(this, "Impossible de supprimer votre propre compte.", "Interdit", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Supprimer l'utilisateur \"" + name + "\" ?",
                "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            authController.deleteUser(id);
            statusLabel.setText("Utilisateur supprimé");
        }
    }

    private void showUserForm(Utilisateur existing) {
        boolean isEdit = (existing != null);

        JDialog form = new JDialog(this, isEdit ? "Modifier l'utilisateur" : "Nouvel utilisateur", true);
        form.setResizable(false);
        form.setSize(400, isEdit ? 400 : 500);
        form.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));
        panel.setBackground(ThemeUtil.BG_WHITE);

        JLabel nameLbl = new JLabel("Nom complet");
        nameLbl.setFont(ThemeUtil.FONT_SMALL); nameLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameLbl);
        panel.add(Box.createVerticalStrut(4));
        JTextField nameField = ThemeUtil.createStyledTextField(20);
        nameField.setMaximumSize(new Dimension(350, 40));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (isEdit) nameField.setText(existing.getNom());
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(12));

        JLabel userLbl = new JLabel("Nom d'utilisateur");
        userLbl.setFont(ThemeUtil.FONT_SMALL); userLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(userLbl);
        panel.add(Box.createVerticalStrut(4));
        JTextField userField = ThemeUtil.createStyledTextField(20);
        userField.setMaximumSize(new Dimension(350, 40));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (isEdit) userField.setText(existing.getUsername());
        panel.add(userField);
        panel.add(Box.createVerticalStrut(12));

        JPanel newPassPanel = null;
        JPanel confirmPassPanel = null;
        if (!isEdit) {
            JLabel passLbl = new JLabel("Mot de passe");
            passLbl.setFont(ThemeUtil.FONT_SMALL); passLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
            passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(passLbl);
            panel.add(Box.createVerticalStrut(4));
            newPassPanel = ThemeUtil.createPasswordFieldWithToggle(20);
            newPassPanel.setMaximumSize(new Dimension(350, 40));
            newPassPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(newPassPanel);
            panel.add(Box.createVerticalStrut(12));

            JLabel confLbl = new JLabel("Confirmer le mot de passe");
            confLbl.setFont(ThemeUtil.FONT_SMALL); confLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
            confLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(confLbl);
            panel.add(Box.createVerticalStrut(4));
            confirmPassPanel = ThemeUtil.createPasswordFieldWithToggle(20);
            confirmPassPanel.setMaximumSize(new Dimension(350, 40));
            confirmPassPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(confirmPassPanel);
            panel.add(Box.createVerticalStrut(12));
        }

        JLabel roleLbl = new JLabel("Rôle");
        roleLbl.setFont(ThemeUtil.FONT_SMALL); roleLbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        roleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(roleLbl);
        panel.add(Box.createVerticalStrut(4));
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
        roleBox.setFont(ThemeUtil.FONT_BODY);
        roleBox.setMaximumSize(new Dimension(350, 40));
        roleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (isEdit) roleBox.setSelectedItem(existing.getRole());
        panel.add(roleBox);
        panel.add(Box.createVerticalStrut(20));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(ThemeUtil.BG_WHITE);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton cancel = ThemeUtil.createOutlineButton("Annuler");
        JButton save = ThemeUtil.createPrimaryButton(isEdit ? "Modifier" : "Créer");
        btnRow.add(cancel); btnRow.add(save);
        panel.add(btnRow);

        final JPanel fp = newPassPanel, cp = confirmPassPanel;
        cancel.addActionListener(e -> form.dispose());
        save.addActionListener(e -> {
            String nom = nameField.getText().trim();
            String username = userField.getText().trim();
            String role = (String) roleBox.getSelectedItem();
            if (nom.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(form, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isEdit) {
                String pass = new String(ThemeUtil.getPasswordFieldFromPanel(fp).getPassword());
                String conf = new String(ThemeUtil.getPasswordFieldFromPanel(cp).getPassword());
                if (pass.isEmpty()) {
                    JOptionPane.showMessageDialog(form, "Mot de passe obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (pass.length() < 4) {
                    JOptionPane.showMessageDialog(form, "Mot de passe trop court (min 4).", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!pass.equals(conf)) {
                    JOptionPane.showMessageDialog(form, "Les mots de passe ne correspondent pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Utilisateur nu = new Utilisateur(nom, username, pass, role);
                if (authController.createUser(nu)) {
                    JOptionPane.showMessageDialog(form, "Utilisateur créé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    form.dispose();
                } else {
                    JOptionPane.showMessageDialog(form, "Erreur (username déjà utilisé ?).", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                existing.setNom(nom);
                existing.setUsername(username);
                existing.setRole(role);
                if (authController.updateUser(existing)) {
                    JOptionPane.showMessageDialog(form, "Utilisateur modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    form.dispose();
                } else {
                    JOptionPane.showMessageDialog(form, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        form.setContentPane(panel);
        form.setVisible(true);
    }
}
