package com.example.quyet.podomoro.databases;

import android.util.Log;
import android.widget.Toast;

import com.example.quyet.podomoro.activities.TaskActivity;
import com.example.quyet.podomoro.databases.models.Task;
import com.example.quyet.podomoro.databases.models.TempTask;
import com.example.quyet.podomoro.networks.NetContext;
import com.example.quyet.podomoro.networks.jsonmodel.DeleteResponseJSon;
import com.example.quyet.podomoro.networks.jsonmodel.TaskResponseJson;
import com.example.quyet.podomoro.networks.services.TaskService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by quyetdv on 2/21/2017.
 */

public class TaskManager {
    public static final String TAG = "TaskManager";
    private TaskService taskService = NetContext.instance.create(TaskService.class);

    public interface GetTasksListener {
        void onGetAllTask(boolean ok);
    }

    public interface EditTaskListener {
        void onEditTask(boolean ok);
    }


    private EditTaskListener editTaskListener;

    public void setEditTaskListener(EditTaskListener editTaskListener) {
        this.editTaskListener = editTaskListener;
    }

    public static final TaskManager instance = new TaskManager();
    private GetTasksListener getTasksListener;

    public void setGetTasksListener(GetTasksListener getTasksListener) {
        this.getTasksListener = getTasksListener;
    }

    private TaskManager() {
    }

    public boolean getTaskFromServer() {
        // // TODO: 2/23/2017 new Solution (use static header )
//        TaskService taskService = NetContext.instance.create(TaskService.class);
        taskService.getTasks().enqueue(new Callback<List<TaskResponseJson>>() {
            @Override
            public void onResponse(Call<List<TaskResponseJson>> call, Response<List<TaskResponseJson>> response) {
                Log.d(TAG, "onResponse: get All Task" + response.code());
                if (response.body() != null) {
                    for (TaskResponseJson t :
                            response.body()) {
//                        if (t.getName()!= null)
                        DBContext.instance.addOrUpdate(new Task(
                                t.getName(),
                                t.getColor(),
                                t.getPayment_per_hour(),
                                t.isDone(),
                                t.getId(),
                                t.getLocal_id(),
                                t.getDue_date()
                        ));
                    }
//                    compareData(DBContext.instance.getTasks());
                    getTasksListener.onGetAllTask(true);
                }
            }
            @Override
            public void onFailure(Call<List<TaskResponseJson>> call, Throwable t) {
                Log.d(TAG, String.format("onFailure: get all task %s", t.getCause()));
                getTasksListener.onGetAllTask(false);
            }
        });
        return true;
    }

    public void addNewTask(final Task newTask) {
        TaskResponseJson newTaskResponseJson = new TaskResponseJson(
                newTask.getName(),
                newTask.getColor(),
                newTask.getPayment_per_hour(),
                newTask.isDone(),
                newTask.getId(),
                newTask.getLocal_id(),
                newTask.getDue_date()
        );
        Log.d(TAG, "addNewTask: " + newTask.isDone());
        // // TODO: 2/23/2017 new Solution (use static header )
        taskService.addNewTask(newTaskResponseJson).enqueue(new Callback<TaskResponseJson>() {
            @Override
            public void onResponse(Call<TaskResponseJson> call, Response<TaskResponseJson> response) {
                Log.d(TAG, String.format("addNewTask: %s %s", response.code(), response.body().toString()));
            }

            @Override
            public void onFailure(Call<TaskResponseJson> call, Throwable t) {
                Log.d(TAG, String.format("onFailure: add new task%s", t.getCause().toString()));
//                DBContext.instance.addOrUpdate(new TempTask(newTask.getLocal_id(), true));
            }
        });

    }

    public void editTask(final Task editedTask) {
        String localId = editedTask.getLocal_id();

        TaskResponseJson editedTaskResponse = new TaskResponseJson(
                editedTask.getName(),
                editedTask.getColor(),
                editedTask.getPayment_per_hour(),
                editedTask.isDone(),
                editedTask.getId(),
                editedTask.getDue_date()
        );
        taskService.editTask(localId, editedTaskResponse).enqueue(new Callback<TaskResponseJson>() {
            @Override
            public void onResponse(Call<TaskResponseJson> call, Response<TaskResponseJson> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse : Edited task code" + response.code());
                    editTaskListener.onEditTask(true);
                } else {
                    Log.d(TAG, "onResponse: edit fail ; code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TaskResponseJson> call, Throwable t) {
                Log.d(TAG, "edit fail ; " + t.getCause());
                editTaskListener.onEditTask(false);
//                DBContext.instance.addOrUpdate(new TempTask(editedTask.getLocal_id(), true, false, false));
            }
        });
    }
    public void deleteTask(final Task taskDelete) {
        String localID = taskDelete.getLocal_id();
        Log.d(TAG, "deleteTask: in function");
        taskService.deleteTask(localID).enqueue(new Callback<DeleteResponseJSon>() {
            @Override
            public void onResponse(Call<DeleteResponseJSon> call, Response<DeleteResponseJSon> response) {
                Log.d(TAG, "onResponse: DeleteTask code " + response.code());
            }
            @Override
            public void onFailure(Call<DeleteResponseJSon> call, Throwable t) {
                Log.d(TAG, String.format("onFailure: delete task%s", t.getCause()));
//                DBContext.instance.addOrUpdate(new TempTask(taskDelete.getLocal_id(), false, true, false));
            }
        });
    }
    public void compareData(List<Task> taskList){
        List<TempTask> temps = DBContext.instance.getTemps();
        for (TempTask temp:
             temps) {
            deleteTask(new Task(temp.getLocal_id()));
            DBContext.instance.deleteTask(new Task(temp.getLocal_id()));
            DBContext.instance.deleteTempTask(temp);
        }
        for (Task task:
             taskList) {
            for (TempTask temp :
                    temps) {
                if (task.getLocal_id().equals(temp.getLocal_id())){

                }
            }
        }
    }
}
