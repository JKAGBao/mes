package project.bridgetek.com.applib.main.bean;

import java.util.List;

/**
 * Created by bridge on 18-6-12.
 */

public class Task {
    String account;
    int tasks_num;
    List<TaskInfo> tasks;
    String errorMessage;

    public Task(String account, int tasks_num, List<TaskInfo> tasks, String errorMessage) {
        this.account = account;
        this.tasks_num = tasks_num;
        this.tasks = tasks;
        this.errorMessage = errorMessage;
    }

    public Task() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getTasks_num() {
        return tasks_num;
    }

    public void setTasks_num(int tasks_num) {
        this.tasks_num = tasks_num;
    }

    public List<TaskInfo> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskInfo> tasks) {
        this.tasks = tasks;
    }
}
