import android.app.AlarmManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hashicode.simpletodo.model.Task;
import com.hashicode.simpletodo.service.TaskDatabaseHelper;
import com.hashicode.simpletodo.service.TaskService;
import com.hashicode.simpletodo.model.TaskTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by takahashi on 11/10/15.
 */
@Ignore(value = "configure manifest")
@RunWith(RobolectricTestRunner.class)
public class TodoServiceTest{

    private TaskService taskService;
    private ShadowAlarmManager shadowAlarmManager;
    private AlarmManager alarmManager;
    private Context context;
    private SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;
        this.taskService = TaskService.getInstance(context);
        db = TaskDatabaseHelper.getInstance(
                context).getWritableDatabase();
        alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = Shadows.shadowOf(alarmManager);
        assertTrue(shadowAlarmManager.getNextScheduledAlarm()==null);
        assertEquals(0,shadowAlarmManager.getScheduledAlarms().size());
    }

    @Test
    public void testInsert(){
        Task task = buildTodo("Task", buildFutureDate());
        assertTrue(taskService.insert(task)!=-1);
        assertNotNull(shadowAlarmManager.getNextScheduledAlarm());

        Task taskNoDate = buildTodo("Task no date", null);
        assertTrue(taskService.insert(taskNoDate)!=-1);
        assertNull(shadowAlarmManager.getNextScheduledAlarm());


    }

    private Task buildTodo(String name, Date date) {
        Task task = new Task();
        task.setName(name);
        task.setRemind(date);
        return task;
    }

    private Date buildFutureDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return calendar.getTime();
    }

    @Test
    public void testUpdate(){
        Task task = buildTodo("Task", buildFutureDate());
        Long id1= taskService.insert(task);
//        assertNotNull(shadowAlarmManager.getNextScheduledAlarm());

        task.setId(id1);
        task.setName("New task");
        assertEquals(1, taskService.update(task));
        assertNotNull(shadowAlarmManager.getNextScheduledAlarm());


        task.setRemind(null);
        assertEquals(1, taskService.update(task));
        assertNull(shadowAlarmManager.getNextScheduledAlarm());

    }

    @Test
    public void testDelete(){
        Task task = buildTodo("Task", buildFutureDate());
        Long id1= taskService.insert(task);
        task.setId(id1);

        assertEquals(1, taskService.delete(id1));
        assertNull(shadowAlarmManager.getNextScheduledAlarm());

    }

    @After
    public void tearDown() throws Exception {
        TaskDatabaseHelper.getInstance(context).getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+ TaskTable.TABLE_NAME);
        db.close();
    }
}
