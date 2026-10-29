package view;

import controller.EleveController;
import model.Eleve;
import util.IconProvider;
import util.ThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Formulaire Ajout/Modification d'un élève.
public class EleveFormView extends JDialog {

    private final Window parent;
    private final Eleve existingEleve;
    private final EleveController eleveController;
    private final boolean isEditMode;

    private JTextField nomF, prenomF, dateF, adrF, telF, emailF, classeF;
    private JComboBox<String> sexeC;
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EleveFormView(Window parent, Eleve e) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.parent = parent;
        this.existingEleve = e;
        this.eleveController = new EleveController();
        this.isEditMode = (e != null);
        initComponents();
        if (isEditMode) populateFields();
    }

    private void initComponents() {
        setTitle(isEditMode ? "Modifier" : "Ajouter");
        setResizable(false);
        setUndecorated(true);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeUtil.BG_WHITE);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        // Barre titre
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(ThemeUtil.PRIMARY);
        titleBar.setBorder(BorderFactory.createEmptyBorder(16,24,16,24));

        JPanel tl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tl.setBackground(ThemeUtil.PRIMARY);
        tl.add(new JLabel(isEditMode ? IconProvider.editWhite() : IconProvider.addWhite()));
        JLabel title = new JLabel(isEditMode ? "Modifier un élève" : "Nouvel élève");
        title.setFont(ThemeUtil.FONT_HEADING);
        title.setForeground(ThemeUtil.TEXT_LIGHT);
        tl.add(title);
        titleBar.add(tl, BorderLayout.WEST);

        JButton cls = new JButton(IconProvider.closeWhite());
        cls.setContentAreaFilled(false); cls.setBorderPainted(false);
        cls.setFocusPainted(false);
        cls.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cls.addActionListener(e -> dispose());
        titleBar.add(cls, BorderLayout.EAST);
        root.add(titleBar, BorderLayout.NORTH);

        // Corps
        JPanel body = new JPanel(new BorderLayout(15,15));
        body.setBackground(ThemeUtil.BG_LIGHT);
        body.setBorder(BorderFactory.createEmptyBorder(24,24,20,24));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(ThemeUtil.BG_LIGHT);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,8,6,8); g.fill = GridBagConstraints.HORIZONTAL;
        int r = 0;
        addRow(form, g, r++, "Nom *",             nomF = field());
        addRow(form, g, r++, "Prénom *",          prenomF = field());
        addRow(form, g, r++, "Date (jj/mm/aaaa)*", dateF = field());
        addCombo(form, g, r++, "Sexe *",          sexeC = new JComboBox<>(new String[]{"M","F"}));
        addRow(form, g, r++, "Adresse",           adrF = field());
        addRow(form, g, r++, "Téléphone",         telF = field());
        addRow(form, g, r++, "Email",             emailF = field());
        addRow(form, g, r++, "Classe *",          classeF = field());

        JScrollPane sp = new JScrollPane(form);
        sp.setBorder(null);
        sp.getViewport().setBackground(ThemeUtil.BG_LIGHT);
        body.add(sp, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBackground(ThemeUtil.BG_LIGHT);
        JButton cancel = ThemeUtil.createOutlineButton("Annuler");
        cancel.addActionListener(e -> dispose());
        JButton save = ThemeUtil.createPrimaryButton(isEditMode ? "Modifier" : "Ajouter");
        save.addActionListener(e -> handleSave());
        footer.add(cancel); footer.add(save);
        body.add(footer, BorderLayout.SOUTH);
        root.add(body, BorderLayout.CENTER);

        setSize(520, 560);
        setLocationRelativeTo(parent);
        getRootPane().setDefaultButton(save);
    }

    private JTextField field() {
        JTextField f = new JTextField(22);
        f.setFont(ThemeUtil.FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                new ThemeUtil.RoundedBorder(6, ThemeUtil.BORDER),
                BorderFactory.createEmptyBorder(8,12,8,12)));
        return f;
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String l, JTextField f) {
        g.gridx=0; g.gridy=row;
        JLabel lbl = new JLabel(l);
        lbl.setFont(ThemeUtil.FONT_SMALL); lbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        p.add(lbl, g);
        g.gridx=1; p.add(f, g);
    }

    private void addCombo(JPanel p, GridBagConstraints g, int row, String l, JComboBox<String> c) {
        g.gridx=0; g.gridy=row;
        JLabel lbl = new JLabel(l);
        lbl.setFont(ThemeUtil.FONT_SMALL); lbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        p.add(lbl, g);
        g.gridx=1; c.setFont(ThemeUtil.FONT_BODY); p.add(c, g);
    }

    private void populateFields() {
        nomF.setText(existingEleve.getNom());
        prenomF.setText(existingEleve.getPrenom());
        if (existingEleve.getDateNaissance()!=null) dateF.setText(existingEleve.getDateNaissance().format(DF));
        sexeC.setSelectedItem(existingEleve.getSexe());
        adrF.setText(existingEleve.getAdresse()!=null?existingEleve.getAdresse():"");
        telF.setText(existingEleve.getTelephone()!=null?existingEleve.getTelephone():"");
        emailF.setText(existingEleve.getEmail()!=null?existingEleve.getEmail():"");
        classeF.setText(existingEleve.getClasse());
    }

    private void handleSave() {
        try {
            Eleve e = build();
            boolean ok;
            if (isEditMode) {
                e.setId(existingEleve.getId());
                ok = eleveController.updateEleve(e);
            } else {
                ok = eleveController.addEleve(e);
            }
            if (ok) {
                JOptionPane.showMessageDialog(this, isEditMode?"Modifié avec succès.":"Ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else JOptionPane.showMessageDialog(this, "Erreur d'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : "+ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Eleve build() {
        String nom=nomF.getText().trim(), prenom=prenomF.getText().trim(),
               dateStr=dateF.getText().trim(), sexe=(String)sexeC.getSelectedItem(),
               adr=adrF.getText().trim(), tel=telF.getText().trim(),
               email=emailF.getText().trim(), classe=classeF.getText().trim();
        if (nom.isEmpty()) throw new IllegalArgumentException("Nom obligatoire.");
        if (prenom.isEmpty()) throw new IllegalArgumentException("Prénom obligatoire.");
        if (classe.isEmpty()) throw new IllegalArgumentException("Classe obligatoire.");
        LocalDate d;
        try { d = LocalDate.parse(dateStr, DF); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Date invalide (jj/mm/aaaa)."); }
        return new Eleve(nom,prenom,d,sexe, adr.isEmpty()?null:adr, tel.isEmpty()?null:tel, email.isEmpty()?null:email, classe);
    }
}
