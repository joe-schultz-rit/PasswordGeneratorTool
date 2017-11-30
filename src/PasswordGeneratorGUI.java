
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Joseph Schultz
 */
public class PasswordGeneratorGUI extends Application implements Observer {

    /** Password Generator for this GUI */
    private PasswordGenerator pwdGen;
    private File charFile = null;
    private File writeFile = null;
    private int startInt = -1;
    private int endInt = -1;

    public void init() {
        this.pwdGen = new PasswordGenerator();
    }

    public void start(Stage primaryStage){
        //Title Bar
//        Label title = new Label("Password Generator v1.0");
//        title.setPadding(new Insets(20, 20, 20, 30));

        //First Section
        Label pathToDictionary = new Label("Choose Character File: ");
        pathToDictionary.setPadding(new Insets(20,50,0,50));
        TextField filePath = new TextField("");
        filePath.setMinWidth(250);
        filePath.setEditable(false);
        Button characterListFile = new Button("Open...");
        characterListFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            charFile = fileChooser.showOpenDialog(primaryStage);
            if(charFile != null){
                filePath.setText(charFile.toString());
            }
        });
        HBox hbox = new HBox();
        hbox.getChildren().addAll(characterListFile, filePath);
        hbox.setPadding(new Insets(0, 20, 5, 50));

        //Second Section
        Label pathToWrite = new Label("Choose File To Write To: ");
        pathToWrite.setPadding(new Insets(20, 20, 0, 50));
        TextField filePath2 = new TextField("");
        filePath2.setMinWidth(250);
        filePath2.setEditable(false);
        Button fileToWrite = new Button("Open...");
        fileToWrite.setOnAction(event -> {
            FileChooser fileChooser2 = new FileChooser();
            fileChooser2.setTitle("Open Resource File");
            writeFile = fileChooser2.showOpenDialog(primaryStage);
            if(writeFile != null){
                filePath2.setText(writeFile.toString());
            }
        });
        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(fileToWrite, filePath2);
        hbox2.setPadding(new Insets(0, 20, 40, 50));

        //Third Section
        Label startLabel = new Label("Starting Length:");
        TextField start = new TextField("");
        start.setEditable(true);
        HBox hbox3 = new HBox();
        hbox3.getChildren().addAll(startLabel, start);
        hbox3.setPadding(new Insets(0, 0, 10, 50));


        //Fourth Section
        Label endLabel = new Label("Ending Length:");
        TextField end = new TextField("");
        end.setEditable(true);
        HBox hbox4 = new HBox();
        hbox4.getChildren().addAll(endLabel, end);
        hbox4.setPadding(new Insets(0, 0, 10, 50));


        //Fifth Section
        Button startButton = new Button("Start");
        startButton.setMinWidth(150);
        startButton.setMinHeight(50);
        HBox hbox5 = new HBox(startButton);
        hbox5.setPadding(new Insets(10, 0, 0, 50));
        startButton.setOnAction(event -> {
            if(charFile == null || !validateFile(charFile)){
                System.out.println("Invalid CharFile");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File Error");
                alert.setHeaderText("Invalid Character File!");
                alert.setContentText("Please choose a *.txt file to retrieve");
                alert.showAndWait();
            } else {
                if(writeFile == null || !validateFile(writeFile)){
                    System.out.println("Invalid WriteFile");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("File Error");
                    alert.setHeaderText("Invalid Write File!");
                    alert.setContentText("Please choose a *.txt file to write to");
                    alert.showAndWait();
                } else {
                    boolean checked = false;
                    if (!start.getText().equals("") && !end.getText().equals("")) {
                        startInt = Integer.parseInt(start.getText());
                        endInt = Integer.parseInt(end.getText());
                    } else {
                        checked = true;
                        System.out.println("Invalid Range");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Range Error");
                        alert.setHeaderText("Invalid Range Of Length!");
                        alert.setContentText("Please choose a valid range.  Start and end lengths must not be less than or equal to " +
                                "zero, and start length can not be greater than end length");
                        alert.showAndWait();
                    } if (!checked && (start.getText().equals("") || end.getText().equals("") || startInt > endInt || startInt < 1 || endInt < 1)) {
                        System.out.println("Invalid Range");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Range Error");
                        alert.setHeaderText("Invalid Range Of Length!");
                        alert.setContentText("Please choose a valid range.  Start and end lengths must not be less than or equal to " +
                                "zero, and start length can not be greater than end length");
                        alert.showAndWait();
                    } else {
                        primaryStage.close();
                        pwdGen.init(writeFile, charFile, startInt, endInt);
                        pwdGen.start();
                        }
                }
            }

        });
        VBox vbox = new VBox();
        vbox.getChildren().addAll( pathToDictionary, hbox, pathToWrite, hbox2, hbox3, hbox4, hbox5);
        Scene scene = new Scene(vbox, 450, 325);
        primaryStage.setScene(scene);
        //Causes Error
        //primaryStage.getIcons().add(new Image(PasswordGeneratorGUI.class.getResourceAsStream("/Resources/Icon.png")));
        primaryStage.setTitle("Password Generator");
        primaryStage.show();
    }

    public boolean validateFile(File f){
        String file = f.toString();
        String[] fileList = file.split("\\.");
        return fileList[fileList.length - 1].equals("txt") || fileList[fileList.length - 1].equals("TXT");
    }

    public void update(Observable o, Object arg ) {
    }

    public void stop(){

    }
}
