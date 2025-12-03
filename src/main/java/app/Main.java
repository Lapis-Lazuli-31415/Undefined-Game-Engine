package app;

import view.HomeView;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            HomeView view = new HomeView();
            view.setVisible(true);
        });
    }
}