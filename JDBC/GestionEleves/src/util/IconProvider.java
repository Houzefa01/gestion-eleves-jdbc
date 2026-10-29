package util;

import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// Fournisseur d'icônes depuis classpath ou fichiers
public class IconProvider {

    private static final String ICON_PATH = "/icons/";
    private static final String ICON_DIR = "resources/icons/";
    private static final Map<String, ImageIcon> cache = new HashMap<>();

    // ===== LOGO =====
    public static ImageIcon logoWhite()  { return load("logo_white", 48); }
    public static ImageIcon logoBlue()   { return load("logo_blue", 48); }

    // ===== BLANC (sur fond sombre) =====
    public static ImageIcon dashboardWhite()   { return load("dashboard_w", 20); }
    public static ImageIcon addWhite()         { return load("add_w", 20); }
    public static ImageIcon editWhite()        { return load("edit_w", 20); }
    public static ImageIcon refreshWhite()     { return load("refresh_w", 20); }
    public static ImageIcon searchWhite()      { return load("search_w", 20); }
    public static ImageIcon trashWhite()       { return load("trash_w", 20); }
    public static ImageIcon userWhite()        { return load("user_w", 20); }
    public static ImageIcon logoutWhite()      { return load("logout_w", 20); }
    public static ImageIcon closeWhite()       { return load("close_w", 20); }

    // ===== BLEU =====
    public static ImageIcon dashboardBlue()    { return load("dashboard_b", 20); }
    public static ImageIcon addBlue()          { return load("add_b", 20); }
    public static ImageIcon editBlue()         { return load("edit_b", 20); }
    public static ImageIcon refreshBlue()      { return load("refresh_b", 20); }
    public static ImageIcon searchBlue()       { return load("search_b", 20); }
    public static ImageIcon trashBlue()        { return load("trash_b", 20); }
    public static ImageIcon userBlue()         { return load("user_b", 20); }
    public static ImageIcon logoutBlue()       { return load("logout_b", 20); }
    public static ImageIcon closeBlue()        { return load("close_b", 20); }

    // ===== ROUGE =====
    public static ImageIcon trashRed()         { return load("trash_r", 20); }
    public static ImageIcon closeRed()         { return load("close_r", 20); }

    // ===== GRIS =====
    public static ImageIcon searchGray()       { return load("search_g", 20); }

    private static ImageIcon load(String name, int size) {
        String key = name + "_" + size;
        if (cache.containsKey(key)) return cache.get(key);

        String path = ICON_PATH + name + ".png";
        URL url = IconProvider.class.getResource(path);

        if (url == null) {
            // Fallback fichiers : resources/icons/
            File f = new File(ICON_DIR + name + ".png");
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                ImageIcon result = new ImageIcon(scaled);
                cache.put(key, result);
                return result;
            }
            System.err.println("Icone introuvable : " + path);
            return new ImageIcon();
        }

        ImageIcon icon = new ImageIcon(url);
        if (icon.getIconWidth() > size) {
            Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }
        cache.put(key, icon);
        return icon;
    }
}
