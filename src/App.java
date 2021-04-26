import ui.MainWindow;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
