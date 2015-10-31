package com.hashicode.simpletodo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a Task from the database
 *
 * Created by takahashi on 02/11/15.
 */
public class Task implements Parcelable {

    private Long id;
    private String name;
    private Date remind;

    public Task() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        this.remind = calendar.getTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRemind() {
        return remind;
    }

    public void setRemind(Date remind) {
        this.remind = remind;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeLong(remind != null ? remind.getTime() : -1);
    }

    protected Task(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        long tmpDeadLine = in.readLong();
        this.remind = tmpDeadLine == -1 ? null : new Date(tmpDeadLine);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (name != null ? !name.equals(task.name) : task.name != null)
            return false;
        return !(remind != null ? !remind.equals(task.remind) : task.remind != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (remind != null ? remind.hashCode() : 0);
        return result;
    }
}
