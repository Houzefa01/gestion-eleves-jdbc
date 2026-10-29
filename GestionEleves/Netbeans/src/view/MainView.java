package view;

import controller.EleveController;
import model.Eleve;
import util.DBConnection;
import util.IconProvider;
import util.ThemeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// Fenetre principale avec sidebar, tableau editable et auto-refresh.
public class MainView extends JFrame {

    private final EleveController eleveController;
    private final model.Utilisateur currentUser;
    private JTable elevesTable;
    private EditableTableModel tableModel;
    private JTextField searchField;
    private JLabel statusLabel, countLabel, dbLabel;
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean editing = false;
    private String currentDb = DBConnection.getCurrentDbName();

    public MainView(model.Utilisateur user) {
        this.currentUser = user;
        this.eleveController = new EleveController();
        initComponents();
        loadEleves();
        // Auto-refresh toutes les 30 secondes (seulement si pas en cours d'edition)
        new Timer(30000, e -> { if (!editing) loadEleves(); }).start();
    }

    private void initComponents() {
        setTitle("Gestion des Elèves");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 750);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(createSidebar(), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(ThemeUtil.BG_LIGHT);
        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createTablePanel(), BorderLayout.CENTER);
        main.add(createStatusBar(), BorderLayout.SOUTH);
        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel createSidebar() {
        JPanel sb = new JPanel();
        sb.setPreferredSize(new Dimension(220, 0));
        sb.setBackground(ThemeUtil.SIDEBAR_BG);
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBorder(BorderFactory.createEmptyBorder(25, 18, 25, 18));

        sb.add(new JLabel(IconProvider.logoWhite()));
        sb.add(Box.createVerticalStrut(6));

        JLabel name = new JLabel("Gestion Elèves");
        name.setFont(ThemeUtil.FONT_HEADING);
        name.setForeground(ThemeUtil.TEXT_LIGHT);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(name);
        sb.add(Box.createVerticalStrut(30));

        sb.add(sidebarBtn("Tableau de bord", IconProvider.dashboardWhite(), true));
        JButton addBtn = sidebarBtn("Nouvel élève", IconProvider.addWhite(), false);
        addBtn.addActionListener(e -> handleAdd());
        sb.add(addBtn);

        if ("ADMIN".equals(currentUser.getRole())) {
            sb.add(Box.createVerticalStrut(6));
            JButton usersBtn = sidebarBtn("Utilisateurs", IconProvider.userWhite(), false);
            usersBtn.addActionListener(e -> {
                new UserManagementView(this, currentUser).setVisible(true);
                loadEleves();
            });
            sb.add(usersBtn);
        }
        sb.add(Box.createVerticalStrut(30));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255,255,255,30));
        sep.setMaximumSize(new Dimension(180, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(sep);
        sb.add(Box.createVerticalStrut(20));

        JPanel userP = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        userP.setBackground(ThemeUtil.SIDEBAR_BG);
        userP.setAlignmentX(Component.LEFT_ALIGNMENT);
        userP.setMaximumSize(new Dimension(200, 40));
        userP.add(new JLabel(IconProvider.userWhite()));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ThemeUtil.SIDEBAR_BG);
        JLabel ul = new JLabel(currentUser.getNom());
        ul.setFont(ThemeUtil.FONT_BODY); ul.setForeground(ThemeUtil.TEXT_LIGHT);
        JLabel rl = new JLabel(currentUser.getRole());
        rl.setFont(ThemeUtil.FONT_SMALL); rl.setForeground(ThemeUtil.SIDEBAR_ICON);
        info.add(ul); info.add(rl);
        userP.add(info);
        sb.add(userP);
        sb.add(Box.createVerticalGlue());

        JButton logout = new JButton("  Déconnexion", IconProvider.logoutWhite());
        logout.setFont(ThemeUtil.FONT_BODY);
        logout.setForeground(ThemeUtil.SIDEBAR_ICON);
        logout.setBackground(ThemeUtil.SIDEBAR_BG);
        logout.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        logout.setFocusPainted(false);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.setMaximumSize(new Dimension(200, 42));
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setHorizontalAlignment(SwingConstants.LEFT);
        logout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { logout.setBackground(ThemeUtil.SIDEBAR_HOVER); }
            public void mouseExited(MouseEvent e) { logout.setBackground(ThemeUtil.SIDEBAR_BG); }
        });
        logout.addActionListener(e -> handleLogout());
        sb.add(logout);
        return sb;
    }

    private JButton sidebarBtn(String text, ImageIcon icon, boolean active) {
        JButton b = new JButton("  " + text, icon);
        b.setFont(ThemeUtil.FONT_BODY);
        b.setForeground(active ? ThemeUtil.TEXT_LIGHT : ThemeUtil.SIDEBAR_ICON);
        b.setBackground(active ? ThemeUtil.SIDEBAR_ACTIVE : ThemeUtil.SIDEBAR_BG);
        b.setBorder(new ThemeUtil.RoundedBorder(8, active ? ThemeUtil.SIDEBAR_ACTIVE : ThemeUtil.SIDEBAR_BG));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(200, 38));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setOpaque(true);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (!active) b.setBackground(ThemeUtil.SIDEBAR_HOVER); }
            public void mouseExited(MouseEvent e) { if (!active) b.setBackground(ThemeUtil.SIDEBAR_BG); }
        });
        return b;
    }

    private JPanel createHeader() {
        JPanel h = new JPanel(new BorderLayout(15, 0));
        h.setBackground(ThemeUtil.BG_WHITE);
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,ThemeUtil.BORDER),
                BorderFactory.createEmptyBorder(18,28,18,28)));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(ThemeUtil.BG_WHITE);
        JLabel t = new JLabel("Tableau de bord");
        t.setFont(ThemeUtil.FONT_HEADING); t.setForeground(ThemeUtil.TEXT_PRIMARY);
        left.add(t);
        countLabel = new JLabel("Chargement...");
        countLabel.setFont(ThemeUtil.FONT_SMALL); countLabel.setForeground(ThemeUtil.TEXT_SECONDARY);
        left.add(countLabel);
        h.add(left, BorderLayout.WEST);

        // Centre : toggle base de donnees
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        center.setBackground(ThemeUtil.BG_WHITE);
        JLabel dbIcon = new JLabel(IconProvider.refreshBlue());
        center.add(dbIcon);
        dbLabel = new JLabel(currentDb.equals("access") ? "MS Access" : "MariaDB");
        dbLabel.setFont(ThemeUtil.FONT_SUBHEADING);
        dbLabel.setForeground(ThemeUtil.PRIMARY);
        center.add(dbLabel);
        JButton dbToggle = new JButton("Basculer");
        dbToggle.setFont(ThemeUtil.FONT_SMALL);
        dbToggle.setForeground(ThemeUtil.TEXT_LIGHT);
        dbToggle.setBackground(ThemeUtil.INFO);
        dbToggle.setBorder(new ThemeUtil.RoundedBorder(6, ThemeUtil.INFO));
        dbToggle.setFocusPainted(false);
        dbToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dbToggle.setPreferredSize(new Dimension(85, 32));
        dbToggle.addActionListener(e -> switchDatabase());
        center.add(dbToggle);
        h.add(center, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(ThemeUtil.BG_WHITE);

        JButton add = new JButton("  Ajouter", IconProvider.addWhite());
        add.setFont(ThemeUtil.FONT_BUTTON);
        add.setForeground(ThemeUtil.TEXT_LIGHT);
        add.setBackground(ThemeUtil.PRIMARY);
        add.setBorder(new ThemeUtil.RoundedBorder(8, ThemeUtil.PRIMARY));
        add.setFocusPainted(false);
        add.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add.setPreferredSize(new Dimension(150, 40));
        add.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { add.setBackground(ThemeUtil.PRIMARY_DARK); }
            public void mouseExited(MouseEvent e) { add.setBackground(ThemeUtil.PRIMARY); }
        });
        add.addActionListener(e -> handleAdd());
        right.add(add);

        JButton ref = new JButton("  Actualiser", IconProvider.refreshBlue());
        ref.setFont(ThemeUtil.FONT_BUTTON);
        ref.setForeground(ThemeUtil.PRIMARY);
        ref.setBackground(ThemeUtil.BG_WHITE);
        ref.setBorder(new ThemeUtil.RoundedBorder(8, ThemeUtil.PRIMARY));
        ref.setFocusPainted(false);
        ref.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ref.setPreferredSize(new Dimension(160, 40));
        ref.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { ref.setBackground(ThemeUtil.PRIMARY_LIGHT); }
            public void mouseExited(MouseEvent e) { ref.setBackground(ThemeUtil.BG_WHITE); }
        });
        ref.addActionListener(e -> loadEleves());
        right.add(ref);

        h.add(right, BorderLayout.EAST);
        return h;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(ThemeUtil.BG_LIGHT);
        p.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(ThemeUtil.BG_LIGHT);
        top.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JPanel sb = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sb.setBackground(ThemeUtil.BG_WHITE);
        sb.setBorder(BorderFactory.createCompoundBorder(
                new ThemeUtil.RoundedBorder(8, ThemeUtil.BORDER),
                BorderFactory.createEmptyBorder(5, 14, 5, 14)));
        sb.add(new JLabel(IconProvider.searchGray()));
        searchField = new JTextField(25);
        searchField.setFont(ThemeUtil.FONT_BODY);
        searchField.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        searchField.setBackground(ThemeUtil.BG_WHITE);
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher nom, prénom, classe...");
        sb.add(searchField);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });
        top.add(sb, BorderLayout.WEST);

        JButton del = new JButton("  Supprimer", IconProvider.trashRed());
        del.setFont(ThemeUtil.FONT_BUTTON);
        del.setForeground(ThemeUtil.DANGER);
        del.setBackground(ThemeUtil.BG_WHITE);
        del.setBorder(new ThemeUtil.RoundedBorder(8, ThemeUtil.DANGER));
        del.setFocusPainted(false);
        del.setCursor(new Cursor(Cursor.HAND_CURSOR));
        del.setPreferredSize(new Dimension(150, 42));
        del.addActionListener(e -> handleDelete());
        del.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { del.setBackground(ThemeUtil.DANGER_LIGHT); }
            public void mouseExited(MouseEvent e) { del.setBackground(ThemeUtil.BG_WHITE); }
        });
        top.add(del, BorderLayout.EAST);
        p.add(top, BorderLayout.NORTH);

        String[] cols = {"ID","Nom","Prénom","Date naissance","Sexe","Adresse","Téléphone","Email","Classe"};
        tableModel = new EditableTableModel(cols, 0);
        elevesTable = new JTable(tableModel);
        // Editeur combo pour Sexe
        elevesTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JComboBox<>(new String[]{"M","F"})));
        elevesTable.setRowHeight(42);
        elevesTable.setShowGrid(true);
        elevesTable.setGridColor(ThemeUtil.BORDER_LIGHT);
        elevesTable.setIntercellSpacing(new Dimension(0, 0));
        elevesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        elevesTable.getTableHeader().setReorderingAllowed(false);
        elevesTable.getTableHeader().setPreferredSize(new Dimension(0, 44));
        elevesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // Tri par clic sur en-tete de colonne
        elevesTable.setAutoCreateRowSorter(true);

        // Editeur combo pour Classe
        elevesTable.getColumnModel().getColumn(8).setCellEditor(
            new DefaultCellEditor(new JComboBox<>(new String[]{"L1","L2","L3","M1","M2"})));

        elevesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JTableHeader hdr = elevesTable.getTableHeader();
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
                        BorderFactory.createEmptyBorder(8,12,8,12)));
                l.setHorizontalAlignment(SwingConstants.LEFT);
                return l;
            }
        });

        // Double-clic sur ligne = ouvrir formulaire
        elevesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && elevesTable.getSelectedRow() != -1) {
                    int row = elevesTable.rowAtPoint(e.getPoint());
                    int col = elevesTable.columnAtPoint(e.getPoint());
                    if (col != -1) {
                        editing = true;
                        handleEdit();
                        editing = false;
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(elevesTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(ThemeUtil.BG_WHITE);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ThemeUtil.BG_WHITE);
        card.setBorder(new ThemeUtil.RoundedBorder(12, ThemeUtil.BORDER));
        card.add(sp, BorderLayout.CENTER);
        p.add(card, BorderLayout.CENTER);
        return p;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(ThemeUtil.BG_WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1,0,0,0,ThemeUtil.BORDER),
                BorderFactory.createEmptyBorder(8,28,8,28)));
        statusLabel = new JLabel("Prêt");
        statusLabel.setFont(ThemeUtil.FONT_SMALL);
        statusLabel.setForeground(ThemeUtil.TEXT_SECONDARY);
        bar.add(statusLabel, BorderLayout.WEST);
        JLabel v = new JLabel("v1.0.0");
        v.setFont(ThemeUtil.FONT_SMALL);
        v.setForeground(ThemeUtil.TEXT_SECONDARY);
        bar.add(v, BorderLayout.EAST);
        return bar;
    }

    private void loadEleves() {
        tableModel.setRowCount(0);
        try {
            List<Eleve> list;
            String kw = searchField.getText().trim();
            list = kw.isEmpty() ? eleveController.getAllEleves() : eleveController.searchEleves(kw);
            for (Eleve e : list) {
                tableModel.addRow(new Object[]{
                    e.getId(), e.getNom(), e.getPrenom(),
                    e.getDateNaissance()!=null ? e.getDateNaissance().format(DF) : "",
                    e.getSexe(),
                    e.getAdresse()!=null?e.getAdresse():"",
                    e.getTelephone()!=null?e.getTelephone():"",
                    e.getEmail()!=null?e.getEmail():"",
                    e.getClasse()
                });
            }
            countLabel.setText(list.size() + " élève(s)");
            statusLabel.setText("Données chargées");
        } catch (Exception ex) { statusLabel.setText("Erreur : " + ex.getMessage()); }
    }

    private void doSearch() { SwingUtilities.invokeLater(this::loadEleves); }

    private void handleAdd() {
        new EleveFormView(this, null).setVisible(true);
        loadEleves();
    }

    private void handleEdit() {
        int r = elevesTable.getSelectedRow(); if (r==-1) return;
        Eleve e = eleveController.getEleveById((int)tableModel.getValueAt(r,0));
        if (e!=null) { new EleveFormView(this, e).setVisible(true); loadEleves(); }
    }

    private void handleDelete() {
        int r = elevesTable.getSelectedRow(); if (r==-1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un élève."); return; }
        int id = (int)tableModel.getValueAt(r,0);
        String n = (String)tableModel.getValueAt(r,1);
        String p = (String)tableModel.getValueAt(r,2);
        if (JOptionPane.showConfirmDialog(this, "Supprimer "+p+" "+n+" ?",
                "Confirmation", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            eleveController.deleteEleve(id);
            statusLabel.setText("Elève supprimé");
            loadEleves();
        }
    }

    private void handleLogout() {
        if (JOptionPane.showConfirmDialog(this, "Se déconnecter ?",
                "Déconnexion", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            new LoginView().setVisible(true);
            dispose();
        }
    }

    private void switchDatabase() {
        String newDb = currentDb.equals("access") ? "mariadb" : "access";
        try {
            DBConnection.init(newDb);
            currentDb = newDb;
            dbLabel.setText(newDb.equals("access") ? "MS Access" : "MariaDB");

            controller.AuthController ac = new controller.AuthController();
            ac.initializeDefaultUsers();
            eleveController.initializeDefaultEleves();

            loadEleves();
            statusLabel.setText("Base changee : " + dbLabel.getText());
        } catch (Exception ex) {
            statusLabel.setText("Erreur bascule : " + ex.getMessage());
            // Revenir a l'ancienne base
            try { DBConnection.init(currentDb); } catch (Exception ignored) {}
        }
    }

    // TableModel avec support d'edition inline (sauf colonne ID)
    private class EditableTableModel extends DefaultTableModel {
        public EditableTableModel(String[] cols, int rows) { super(cols, rows); }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col != 0; // ID non editable
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            if (col == 0) return;
            super.setValueAt(value, row, col);
            editing = true;

            try {
                int id = (int) getValueAt(row, 0);
                Eleve e = eleveController.getEleveById(id);
                if (e == null) { statusLabel.setText("Eleve introuvable"); return; }

                String val = (value != null) ? value.toString().trim() : "";
                switch (col) {
                    case 1: if (val.isEmpty()) { statusLabel.setText("Nom obligatoire"); loadEleves(); return; }
                        e.setNom(val); break;
                    case 2: if (val.isEmpty()) { statusLabel.setText("Prenom obligatoire"); loadEleves(); return; }
                        e.setPrenom(val); break;
                    case 3:
                        try { e.setDateNaissance(LocalDate.parse(val, DF)); }
                        catch (DateTimeParseException ex) {
                            statusLabel.setText("Date invalide (jj/mm/aaaa)");
                            loadEleves(); return;
                        }
                        break;
                    case 4: e.setSexe(val); break;
                    case 5: e.setAdresse(val.isEmpty() ? null : val); break;
                    case 6: e.setTelephone(val.isEmpty() ? null : val); break;
                    case 7: e.setEmail(val.isEmpty() ? null : val); break;
                    case 8: if (val.isEmpty()) { statusLabel.setText("Classe obligatoire"); loadEleves(); return; }
                        e.setClasse(val); break;
                }

                if (eleveController.updateEleve(e)) {
                    statusLabel.setText("Modification enregistree");
                } else {
                    statusLabel.setText("Erreur d'enregistrement");
                }
            } catch (Exception ex) {
                statusLabel.setText("Erreur : " + ex.getMessage());
            }
            editing = false;
        }
    }
}
