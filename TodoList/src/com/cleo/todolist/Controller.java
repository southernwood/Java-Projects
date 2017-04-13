package com.cleo.todolist;

import com.cleo.todolist.datamodel.TodoData;
import com.cleo.todolist.datamodel.TodoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea itemDetailsArea;
    @FXML
    private Label dealLineLabel;
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private ToggleButton filterToggleButton;

    private FilteredList<TodoItem> filteredList;
    private Predicate<TodoItem> wantAllItems;
    private Predicate<TodoItem> wantTodayItems;

    public void initialize(){

        //associate the deleteMenu
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        listContextMenu.getItems().addAll(deleteMenuItem);


        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if(newValue != null) {
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailsArea.setText(item.getDetail());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    dealLineLabel.setText(df.format(item.getDeadline()));
                }
            }
        });
        // configure the filtered list
        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return true;
            }
        };
        wantTodayItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                if (item.getDeadline().equals(LocalDate.now())){
                    return true; // true item will be in the result
                }else {
                    return false;
                }
            }
        };
        filteredList = new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(), wantAllItems);

        // order the list according to this date
        SortedList<TodoItem> sortList = new SortedList<TodoItem>(filteredList, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });
        todoListView.setItems(sortList); // binding the data
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();
        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>(){
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        }else{
                            setText(item.getShortDescription());
                            if (item.getDeadline().isBefore(LocalDate.now().plusDays(1))){
                                setTextFill(Color.RED);
                            }else if (item.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BROWN);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) ->{
                            if(isNowEmpty){
                                cell.setContextMenu(null);
                            }else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        });
    }
    // setup the dialog plane
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New todo Item");
        dialog.setHeaderText("Use this dialog to create a new item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try{
            //Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            DialogController controller = fxmlLoader.getController();
            TodoItem item = controller.processResult();
          //  todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
            todoListView.getSelectionModel().select(item);
            System.out.println("Ok pressed");
        }else {
            System.out.println("Cancel pressed");
        }

    }
    @FXML
    public void handleClickListView(){
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        System.out.println("selected" + item);

//        StringBuilder sb = new StringBuilder(item.getDetail());
//        sb.append("\n\n\n\n"); // append  4 empty lines
//        sb.append("Due: ");
//        sb.append(item.getDeadline().toString());
        itemDetailsArea.setText(item.toString());
        dealLineLabel.setText(item.getDeadline().toString());
    }

    public void handleFilterButton() {
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (filterToggleButton.isSelected()){
            filteredList.setPredicate(wantTodayItems);
            if (filteredList.isEmpty()){
                itemDetailsArea.clear();
                dealLineLabel.setText("");
            }else if(filteredList.contains(selectedItem)){
                todoListView.getSelectionModel().select(selectedItem);
            }else{
                todoListView.getSelectionModel().selectFirst();
            }
        }else{
            filteredList.setPredicate(wantAllItems);
            todoListView.getSelectionModel().select(selectedItem);
        }
    }

    @FXML
    public void handleKeyPressed (KeyEvent keyEvent){
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        if (item != null ) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteItem(item);
            }
        }
    }
    public void deleteItem(TodoItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: "+item.getShortDescription());
        alert.setContentText("Are you sure? Press Ok to confirm, or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoData.getInstance().deleteTodoItem(item);
        }
    }
    public void handleExit() {
        Platform.exit();
    }
}
