package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



public class Controller {
    @FXML
    private TextField nameField;
    @FXML
    private Button helloButton;
    @FXML
    private Button byeButton;

    @FXML
    private CheckBox ClearCheckBox;

    @FXML
    private Label ourLabel;

    @FXML
    public void initialize(){
        helloButton.setDisable(true);
        byeButton.setDisable(true);
    }
    public void onButtonClicked(ActionEvent e) {

        if (e.getSource().equals(helloButton)){
            System.out.println("hello, " + nameField.getText());
        }else{System.out.println("bye, " + nameField.getText());
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    String s = Platform.isFxApplicationThread()? "UI Thread ": "Background Thread";
                    Thread.sleep(10000);
                    System.out.println("I'm going to sleep on the : " + s);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String s = Platform.isFxApplicationThread()? "UI Thread ": "Background Thread";
                            System.out.println("I'm going to update on the : " + s);
                            ourLabel.setText("we did something");
                        }
                    });
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        };
        new Thread(task).start();
        if (ClearCheckBox.isSelected()){
            nameField.clear();
            helloButton.setDisable(true);
            byeButton.setDisable(true);
            System.out.println("clear box is selected");
        }
    }
    @FXML
    public void handleKeyReleased() {
        String text = nameField.getText();
        boolean disableButton = text.isEmpty() || text.trim().isEmpty();
        helloButton.setDisable(disableButton);
        byeButton.setDisable(disableButton);
    }

    public void handleChange() {
        System.out.println("checked box is selected");
    }
}
