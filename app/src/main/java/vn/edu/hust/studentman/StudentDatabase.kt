package vn.edu.hust.studentman

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "students.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addStudent(student: StudentModel): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, student.studentId)
            put(COLUMN_NAME, student.studentName)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun getAllStudents(): List<StudentModel> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val students = mutableListOf<StudentModel>()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            students.add(StudentModel(name, id))
        }
        cursor.close()
        db.close()
        return students
    }

    fun updateStudent(oldId: String, student: StudentModel): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, student.studentId)
            put(COLUMN_NAME, student.studentName)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(oldId))
        db.close()
        return result > 0
    }

    fun deleteStudent(studentId: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(studentId))
        db.close()
        return result > 0
    }
}
