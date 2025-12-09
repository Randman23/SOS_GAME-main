package Sprint5.ApplicationLogic;

import Sprint5.GUILogic.GameView;
import Sprint5.GameLogic.Game;
import Sprint5.GameLogic.SimpleGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Main application entry point.
 */
public class SOSApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        Game initialGame = new SimpleGame(5);
        GameView view = new GameView(initialGame);

        // Create controller and pass both view and game
        GameController controller = new GameController(view, initialGame);

        // Use a smaller initial window that can grow
        Scene scene = new Scene(view, 850, 650);
        primaryStage.setTitle("SOS Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}