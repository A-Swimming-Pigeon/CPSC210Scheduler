package controller;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Status;
import model.Task;
import ui.*;
import utility.JsonFileIO;
import utility.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

// Controller class for Todobar UI
public class TodobarController implements Initializable {
    private static final String todoOptionsPopUpFXML = "resources/fxml/TodoOptionsPopUp.fxml";
    private static final String todoActionsPopUpFXML = "resources/fxml/TodoActionsPopUp.fxml";
    private File todoOptionsPopUpFxmlFile = new File(todoOptionsPopUpFXML);
    private File todoActionsPopUpFxmlFile = new File(todoActionsPopUpFXML);

    @FXML
    private Label descriptionLabel;
    @FXML
    private JFXHamburger todoActionsPopUpBurger;
    @FXML
    private StackPane todoActionsPopUpContainer;
    @FXML
    private JFXRippler todoOptionsPopUpRippler;
    @FXML
    private StackPane todoOptionsPopUpBurger;

    private JFXPopup optionsPopUp;
    private JFXPopup actionsPopUp;

    private Task task;

    // REQUIRES: task != null
    // MODIFIES: this
    // EFFECTS: sets the task in this Todobar
    //          updates the Todobar UI label to task's description
    public void setTask(Task task) {
        this.task = task;
        descriptionLabel.setText(task.getDescription());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTodoBarPopUp();
        loadTodobarOptionsPopUpActionListener();
        loadActionsPopup();
        loadActionsPopUpActionListener();
    }

    // EFFECTS: load todobar pop up
    private void loadTodoBarPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoOptionsPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new OptionsPopUpController());
            optionsPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // EFFECTS: load todobar options pop up listener
    private void loadTodobarOptionsPopUpActionListener() {
        todoOptionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                optionsPopUp.show(todoOptionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        -120,
                        15);
            }
        });
    }

    // EFFECTS: load todoactions pop up
    private void loadActionsPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoActionsPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new ActionsPopUpController());
            actionsPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // EFFECTS: load todoactions pop up listener
    private void loadActionsPopUpActionListener() {
        todoActionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                actionsPopUp.show(todoActionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        12,
                        15);
            }
        });
    }

    //Inner class ActionsPopUpController
    class ActionsPopUpController {
        @FXML
        private JFXListView<?> actionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = actionPopUpList.getSelectionModel().getSelectedIndex();
            checkIndex(selectedIndex);
            actionsPopUp.hide();
        }

        //EFFECTS: logs and sets status based off of user selection on a Task
        private void checkIndex(int index) {
            if (index > 2 && index < 5) {
                checkHigherIndex(index);
            } else if (index == 0) {
                Logger.log("TodobarOptionsPopUpController", "Setting task \"" + task.getDescription() + "\" to Todo");
                task.setStatus(Status.TODO);
            } else if (index == 1) {
                Logger.log("TodobarOptionsPopUpController", "Setting task \"" + task.getDescription()
                        + "\" to Up Next");
                task.setStatus(Status.UP_NEXT);
            } else if (index == 2) {
                Logger.log("TodobarOptionsPopUpController", "Setting task \"" + task.getDescription()
                        + "\" to In Progress");
                task.setStatus(Status.IN_PROGRESS);
            } else {
                Logger.log("TodobarOptionsPopUpController", "Selection not implemented");
            }
        }

        private void checkHigherIndex(int index) {
            if (index == 3) {
                Logger.log("TodobarOptionsPopUpController", "Setting task \"" + task.getDescription() + "\" to Done");
                task.setStatus(Status.DONE);
            } else if (index == 4) {
                Logger.log("TodobarOptionsPopUpController", "Pomodoro! is not available.");
            }
        }

    }

    //Inner class OptionsPopUpController
    private class OptionsPopUpController {
        @FXML
        private JFXListView<?> optionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = optionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    PomoTodoApp.setScene(new EditTask(task));
                    Logger.log("TodobarOptionsPopUpController", "Editing task : " + task);
                    break;
                case 1:
                    PomoTodoApp.getTasks().remove(task);
                    Logger.log("TodobarOptionsPopUpController", "Deleting task : " + task);
                    PomoTodoApp.setScene(new ListView(PomoTodoApp.getTasks()));
                    break;
                default:
                    Logger.log("TodobarOptionsPopUpController", "No action is implemented for the selected option");
            }
            optionsPopUp.hide();
        }
    }



}
