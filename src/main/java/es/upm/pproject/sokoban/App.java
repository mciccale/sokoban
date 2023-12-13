package es.upm.pproject.sokoban;

import es.upm.pproject.sokoban.controller.GameController;
import es.upm.pproject.sokoban.interfaces.Controller;
import es.upm.pproject.sokoban.view.GameFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
	public static void main(String[] args) {
        try {
            Controller game = new GameController();
            game.parse(1);
            SwingUtilities.invokeLater(() ->
                new GameFrame().initialize(game)
            );
        } catch (Exception e) {
            logger.error("Something went wrong: {}", e.getMessage());
        }
    }
}
