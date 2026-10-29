import controller.AuthController;
import util.DBConnection;
import util.ThemeUtil;
import view.LoginView;

import javax.swing.*;

// Point d'entrée de l'application GestionEleves.
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Impossible d'appliquer le Look & Feel : " + e.getMessage());
        }

        ThemeUtil.applyGlobalTheme();

        SwingUtilities.invokeLater(() -> {
            try {
                DBConnection.getInstance();

                AuthController authController = new AuthController();
                authController.initializeDefaultUsers();

                LoginView loginView = new LoginView(authController);
                loginView.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur critique lors du d\u00e9marrage :\n" + e.getMessage() +
                        "\n\nV\u00e9rifiez que MariaDB est d\u00e9marr\u00e9 et que le fichier\n" +
                        "database.properties est correctement configur\u00e9.",
                        "Erreur de d\u00e9marrage",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
