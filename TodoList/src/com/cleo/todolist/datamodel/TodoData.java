package com.cleo.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sensen on 2017-04-13.
 */
//singleton class
public class TodoData {
    private static TodoData instance = new TodoData();
    private static String fielname = "TodoListsItem.text";
    private ObservableList<TodoItem> todoItems;
    private DateTimeFormatter formatter;
    // classical singleton method
    public static TodoData getInstance(){
        return instance;
    }
    // prevent outside access
    private TodoData(){
        formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }


    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void addTodoItem(TodoItem item) {
        todoItems.add(item);
    }

    //read from file
    public void loadTodoItems() throws IOException {
        System.out.println("trying to load data from " +fielname);
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(fielname);
        BufferedReader br = Files.newBufferedReader(path);
        String input;
        try{
            while ((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t");
                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dealLine = itemPieces[2];
                LocalDate date = LocalDate.parse(dealLine, formatter);
                TodoItem item = new TodoItem(shortDescription, details, date);
                todoItems.add(item);
            }
        }finally {
            if (br != null) {
                br.close();
            }
        }
    }
    // write into file
    public void storeTodoItems() throws IOException {
        Path path = Paths.get(fielname);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<TodoItem> iter = todoItems.iterator();
            while (iter.hasNext()) {
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",  //real application may store data in xml format or in database
                        item.getShortDescription(), item.getDetail(),item.getDeadline().format(formatter)));
                bw.newLine();
            }
        }finally {
            if (bw != null){
                bw.close();
            }
        }

    }
    public void deleteTodoItem(TodoItem item) {
        todoItems.remove(item);
    }
}
