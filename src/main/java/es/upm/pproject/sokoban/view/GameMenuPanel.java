package es.upm.pproject.sokoban.view;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class GameMenuPanel extends JPanel {

    public GameMenuPanel(JPanel contentPane, Clip levelClip, Clip congratulationsClip) {

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Agrega un espacio interno alrededor del panel
        setBackground(new Color(31, 31, 31)); // Establece el color de fondo marrón

        // Panel with the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Utiliza un GridLayout para organizar los botones
        buttonPanel.setOpaque(false);

        // Font of the buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 70);
        Color buttonBackgroundColor = new Color(182, 172, 132); // Amarillo
        Color buttonTextColor = Color.BLACK;

        // button "Start"
        JButton startButton = new JButton("Start");
        startButton.setFont(buttonFont);
        startButton.setBackground(buttonBackgroundColor);
        startButton.setFocusPainted(false); // Quita el recuadro azul alrededor del botón
        startButton.setForeground(buttonTextColor);
        startButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.next(contentPane);
            levelClip.loop(Clip.LOOP_CONTINUOUSLY);
        });
        buttonPanel.add(startButton);

        // button "Exit"
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(buttonBackgroundColor);
        exitButton.setForeground(buttonTextColor);
        exitButton.addActionListener(e ->{
                levelClip.close();
                congratulationsClip.close();
                System.exit(0);
        });
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }
}
