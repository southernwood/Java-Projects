package com.cleo.todolist;

import com.cleo.todolist.datamodel.TodoData;
import com.cleo.todolist.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Created by sensen on 2017-04-13.
 */
public class DialogController {
    @FXML
    private TextField shortDecription;
    @FXML
    private TextArea details;
    @FXML
    private DatePicker deadline;

    public TodoItem processResult() {
        String shortDis = shortDecription.getText().trim();
        String detail = details.getText().trim();
        LocalDate date = deadline.getValue();
        TodoItem item = new TodoItem(shortDis, detail, date);
        TodoData.getInstance().addTodoItem(item);
        return item;
    }
}
