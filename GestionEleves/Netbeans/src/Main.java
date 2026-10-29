import util.ThemeUtil;
import view.LoginView;

import javax.swing.*;

// Point d'entree de l'application GestionEleves.
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Impossible d'appliquer le Look & Feel : " + e.getMessage());
        }

        ThemeUtil.applyGlobalTheme();

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
