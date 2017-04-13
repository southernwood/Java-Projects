package com.cleo.todolist.datamodel;

import java.time.LocalDate;

/**
 * Created by sensen on 2017-04-13.
 */
public class TodoItem {
    private String shortDescription;
    private String detail;
    private LocalDate deadline;

    public TodoItem(String shortDescription, String detail, LocalDate deadline) {
        this.shortDescription = shortDescription;
        this.detail = detail;
        this.deadline = deadline;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return shortDescription;
    }
}
