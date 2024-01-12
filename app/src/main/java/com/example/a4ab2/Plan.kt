package com.example.a4ab2

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class Plan {
    var planId: Long = 0L
    var name: String = ""
    var firstDate: String = "20240101"
    var repeatSunday: Boolean = false
    var repeatMonday: Boolean = false
    var repeatTuesday: Boolean = false
    var repeatWednesday: Boolean = false
    var repeatThursday: Boolean = false
    var repeatFriday: Boolean = false
    var repeatSaturday: Boolean = false

    fun insert(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_FIRST_DATE, firstDate)
            put(COLUMN_REPEAT_SUNDAY, repeatSunday)
            put(COLUMN_REPEAT_MONDAY, repeatMonday)
            put(COLUMN_REPEAT_TUESDAY, repeatTuesday)
            put(COLUMN_REPEAT_WEDNESDAY, repeatWednesday)
            put(COLUMN_REPEAT_THURSDAY, repeatThursday)
            put(COLUMN_REPEAT_FRIDAY, repeatFriday)
            put(COLUMN_REPEAT_SATURDAY, repeatSaturday)
        }

        planId = db.insert(TABLE_NAME, null, values)
    }

    fun update(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_FIRST_DATE, firstDate)
            put(COLUMN_REPEAT_SUNDAY, repeatSunday)
            put(COLUMN_REPEAT_MONDAY, repeatMonday)
            put(COLUMN_REPEAT_TUESDAY, repeatTuesday)
            put(COLUMN_REPEAT_WEDNESDAY, repeatWednesday)
            put(COLUMN_REPEAT_THURSDAY, repeatThursday)
            put(COLUMN_REPEAT_FRIDAY, repeatFriday)
            put(COLUMN_REPEAT_SATURDAY, repeatSaturday)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(planId.toString()))
    }

    fun delete(db: SQLiteDatabase) {
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(planId.toString()))
    }

    fun getFormattedFirstDate(): String {
        val part1 = firstDate.substring(0, 4)
        val part2 = firstDate.substring(4, 6)
        val part3 = firstDate.substring(6)

        return "$part1 - $part2 - $part3"
    }

    fun getSelectedDays(): String {
        val selectedDays = StringBuilder()

        if (repeatSunday) selectedDays.append("일")
        if (repeatMonday) selectedDays.append("월")
        if (repeatTuesday) selectedDays.append("화")
        if (repeatWednesday) selectedDays.append("수")
        if (repeatThursday) selectedDays.append("목")
        if (repeatFriday) selectedDays.append("금")
        if (repeatSaturday) selectedDays.append("토")

        return selectedDays.toString()
    }

    companion object {
        const val TABLE_NAME = "plans"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_FIRST_DATE = "first_date"
        const val COLUMN_REPEAT_SUNDAY = "repeat_sunday"
        const val COLUMN_REPEAT_MONDAY = "repeat_monday"
        const val COLUMN_REPEAT_TUESDAY = "repeat_tuesday"
        const val COLUMN_REPEAT_WEDNESDAY = "repeat_wednesday"
        const val COLUMN_REPEAT_THURSDAY = "repeat_thursday"
        const val COLUMN_REPEAT_FRIDAY = "repeat_friday"
        const val COLUMN_REPEAT_SATURDAY = "repeat_saturday"

        fun createTable(db: SQLiteDatabase) {
            val createTableQuery = """
                CREATE TABLE $TABLE_NAME (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_NAME TEXT,
                    $COLUMN_FIRST_DATE TEXT,
                    $COLUMN_REPEAT_SUNDAY INTEGER,
                    $COLUMN_REPEAT_MONDAY INTEGER,
                    $COLUMN_REPEAT_TUESDAY INTEGER,
                    $COLUMN_REPEAT_WEDNESDAY INTEGER,
                    $COLUMN_REPEAT_THURSDAY INTEGER,
                    $COLUMN_REPEAT_FRIDAY INTEGER,
                    $COLUMN_REPEAT_SATURDAY INTEGER
                )
            """.trimIndent()

            db.execSQL(createTableQuery)
        }

        fun getAll(db: SQLiteDatabase): List<Plan> {
            val plans = mutableListOf<Plan>()
            val projection = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_FIRST_DATE,
                COLUMN_REPEAT_SUNDAY,
                COLUMN_REPEAT_MONDAY,
                COLUMN_REPEAT_TUESDAY,
                COLUMN_REPEAT_WEDNESDAY,
                COLUMN_REPEAT_THURSDAY,
                COLUMN_REPEAT_FRIDAY,
                COLUMN_REPEAT_SATURDAY
            )

            val cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            with(cursor) {
                while (moveToNext()) {
                    val plan = Plan()
                    plan.planId = getLong(getColumnIndexOrThrow(COLUMN_ID))
                    plan.name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                    plan.firstDate = getString(getColumnIndexOrThrow(COLUMN_FIRST_DATE))
                    plan.repeatSunday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_SUNDAY)) != 0
                    plan.repeatMonday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_MONDAY)) != 0
                    plan.repeatTuesday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_TUESDAY)) != 0
                    plan.repeatWednesday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_WEDNESDAY)) != 0
                    plan.repeatThursday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_THURSDAY)) != 0
                    plan.repeatFriday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_FRIDAY)) != 0
                    plan.repeatSaturday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_SATURDAY)) != 0

                    plans.add(plan)
                }
            }

            cursor.close()
            return plans
        }

        fun getPlansByDayInWeek(db: SQLiteDatabase, dayInWeek: Int): List<Plan> {
            val repeatColumnName = getRepeatColumnNameForDay(dayInWeek)

            val plans = mutableListOf<Plan>()
            val projection = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_FIRST_DATE,
                COLUMN_REPEAT_SUNDAY,
                COLUMN_REPEAT_MONDAY,
                COLUMN_REPEAT_TUESDAY,
                COLUMN_REPEAT_WEDNESDAY,
                COLUMN_REPEAT_THURSDAY,
                COLUMN_REPEAT_FRIDAY,
                COLUMN_REPEAT_SATURDAY
            )

            val selection = "$repeatColumnName = 1"
            val cursor: Cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
            )

            with(cursor) {
                while (moveToNext()) {
                    val plan = Plan()
                    plan.planId = getLong(getColumnIndexOrThrow(COLUMN_ID))
                    plan.name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                    plan.firstDate = getString(getColumnIndexOrThrow(COLUMN_FIRST_DATE))
                    plan.repeatSunday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_SUNDAY)) != 0
                    plan.repeatMonday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_MONDAY)) != 0
                    plan.repeatTuesday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_TUESDAY)) != 0
                    plan.repeatWednesday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_WEDNESDAY)) != 0
                    plan.repeatThursday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_THURSDAY)) != 0
                    plan.repeatFriday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_FRIDAY)) != 0
                    plan.repeatSaturday = getInt(getColumnIndexOrThrow(COLUMN_REPEAT_SATURDAY)) != 0

                    plans.add(plan)
                }
            }

            cursor.close()
            return plans
        }
        private fun getRepeatColumnNameForDay(dayOfWeek: Int): String {
            return when (dayOfWeek) {
                0 -> COLUMN_REPEAT_SUNDAY
                1 -> COLUMN_REPEAT_MONDAY
                2 -> COLUMN_REPEAT_TUESDAY
                3 -> COLUMN_REPEAT_WEDNESDAY
                4 -> COLUMN_REPEAT_THURSDAY
                5 -> COLUMN_REPEAT_FRIDAY
                6 -> COLUMN_REPEAT_SATURDAY
                else -> throw IllegalArgumentException("Invalid day of the week: $dayOfWeek")
            }
        }
    }
}
