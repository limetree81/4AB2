
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.a4ab2.Plan
import com.example.a4ab2.Plan.Companion.getPlansByDayInWeek
import java.text.SimpleDateFormat
import java.util.*

class Task {
    var taskId: Long = 0
    var plan: Plan = Plan()
    var date: String = "20240101"
    var done: Boolean = false

    fun insert(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(COLUMN_PLAN_ID, plan.planId)
            put(COLUMN_DATE, date)
            put(COLUMN_DONE, done)
        }

        taskId = db.insert(TABLE_NAME_TASKS, null, values)
    }

    fun update(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(COLUMN_PLAN_ID, plan.planId)
            put(COLUMN_DATE, date)
            put(COLUMN_DONE, done)
        }

        db.update(TABLE_NAME_TASKS, values, "$COLUMN_ID=?", arrayOf(taskId.toString()))
    }

    fun delete(db: SQLiteDatabase) {
        db.delete(TABLE_NAME_TASKS, "$COLUMN_ID=?", arrayOf(taskId.toString()))
    }

    companion object {
        const val TABLE_NAME_TASKS = "tasks"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_PLAN_ID = "plan_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_DONE = "done"

        fun createTable(db: SQLiteDatabase) {
            val createTableQuery = """
                CREATE TABLE $TABLE_NAME_TASKS (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_PLAN_ID INTEGER,
                    $COLUMN_DATE TEXT,
                    $COLUMN_DONE INTEGER
                )
            """.trimIndent()

            db.execSQL(createTableQuery)
        }

        fun getAll(db: SQLiteDatabase): List<Task> {
            val tasks = mutableListOf<Task>()
            val projection = arrayOf(
                COLUMN_ID,
                COLUMN_PLAN_ID,
                COLUMN_DATE,
                COLUMN_DONE
            )

            val cursor = db.query(
                TABLE_NAME_TASKS,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            with(cursor) {
                while (moveToNext()) {
                    val task = Task()
                    task.taskId = getLong(getColumnIndexOrThrow(COLUMN_ID))
                    task.plan.planId = getLong(getColumnIndexOrThrow(COLUMN_PLAN_ID))
                    task.date = getString(getColumnIndexOrThrow(COLUMN_DATE))
                    task.done = getInt(getColumnIndexOrThrow(COLUMN_DONE)) != 0

                    tasks.add(task)
                }
            }

            cursor.close()
            return tasks
        }

        fun getByDate(db: SQLiteDatabase, today: String): List<Task> {
            val tasks = mutableListOf<Task>()
            val projection = arrayOf(
                COLUMN_ID,
                COLUMN_PLAN_ID,
                COLUMN_DATE,
                COLUMN_DONE
            )

            val cursor = db.query(
                TABLE_NAME_TASKS,
                projection,
                "date = ?",
                arrayOf(today),
                null,
                null,
                null
            )

            with(cursor) {
                while (moveToNext()) {
                    val task = Task()
                    task.taskId = getLong(getColumnIndexOrThrow(COLUMN_ID))
                    task.plan.planId = getLong(getColumnIndexOrThrow(COLUMN_PLAN_ID))
                    task.date = getString(getColumnIndexOrThrow(COLUMN_DATE))
                    task.done = getInt(getColumnIndexOrThrow(COLUMN_DONE)) != 0

                    tasks.add(task)
                }
            }

            cursor.close()
            return tasks
        }

        fun getFromPlan(db: SQLiteDatabase, today: String): List<Task>{
            val dayOfWeek = getTodayDayOfWeek(today)
            var plans = getPlansByDayInWeek(db, dayOfWeek)
            var tasks = mutableListOf<Task>()
            for(i in plans){
                tasks.add(Task().apply{plan = i;date = today})
            }
            return tasks
        }
        fun getTodayDayOfWeek(today: String): Int {
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("yyyyMMdd").parse(today)!!
            return calendar.get(Calendar.DAY_OF_WEEK) -1
        }
    }
}
