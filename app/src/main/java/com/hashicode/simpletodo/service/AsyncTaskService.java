package com.hashicode.simpletodo.service;

import android.content.Context;
import android.os.AsyncTask;

import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.model.Task;

import java.util.Set;

/**
 * An {@link AsyncTask} used to call a {@link TaskService} to make operations.
 *
 * Created by takahashi on 12/15/15.
 */
public class AsyncTaskService extends AsyncTask<AsyncTaskService.Operation, Void, Void> {

    private TaskService service;
    private AsyncTaskServiceCallback callback;

    public AsyncTaskService(Context context, AsyncTaskServiceCallback callback){
        this.service = TaskService.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Operation... params) {
        Operation param = params[0];
        String operation = params[0].getOperation();
        switch (operation){
            case Constants.INSERT :
                service.insert(param.getTask());
                break;
            case Constants.UPDATE :
                service.update(param.getTask());
                break;
            case Constants.DELETE :
                service.delete(param.getTodoId());
                break;
            case Constants.DONE :
                if(param.getIds()!=null){
                    service.bulkDone(param.getIds());
                }
                else{
                    service.done(param.getTodoId());
                }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onPostExecute(aVoid);
    }

    public static class Operation{

        private final Task task;
        private final Long todoId;
        private final String operation;
        private final Set<String> ids;

        private Operation(Task task, String operation) {
            this.task = task;
            this.operation = operation;
            this.todoId=null;
            this.ids =null;
        }

        private Operation(Long todoId, String operation) {
            this.operation = operation;
            this.todoId = todoId;
            this.task =null;
            this.ids=null;
        }

        public Operation(Set<String> ids, String operation) {
            this.ids = ids;
            this.operation = operation;
            this.todoId = null;
            this.task =null;
        }

        public static Operation buildInsertOperation(Task task){
            return new Operation(task, Constants.INSERT);
        }

        public static Operation buildUpdateOperation(Task task){
            return new Operation(task, Constants.UPDATE);
        }

        public static Operation buildDeleteOperation(Long todoId){
            return new Operation(todoId, Constants.DELETE);
        }

        public static Operation buildBulkDoneOperation(Set<String> ids){
            return new Operation(ids, Constants.DONE);
        }

        public Task getTask() {
            return task;
        }

        public String getOperation() {
            return operation;
        }

        public Long getTodoId() {
            return todoId;
        }

        public Set<String> getIds() {
            return ids;
        }
    }

}
