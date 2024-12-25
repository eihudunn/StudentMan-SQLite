package vn.edu.hust.studentman

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val database = StudentDatabase(application)
    private val _students = MutableLiveData<List<StudentModel>>()

    val students: LiveData<List<StudentModel>> get() = _students

    init {
        loadStudents()
    }

    private fun loadStudents() {
        _students.value = database.getAllStudents()
    }

    fun getStudentById(studentId: String): StudentModel? {
        return database.getAllStudents().find { it.studentId == studentId }
    }

    fun addStudent(student: StudentModel): Boolean {
        val result = database.addStudent(student)
        if (result) loadStudents()
        return result
    }

    fun updateStudent(oldId: String, student: StudentModel): Boolean {
        val result = database.updateStudent(oldId, student)
        if (result) loadStudents()
        return result
    }

    fun deleteStudent(student: StudentModel): Boolean {
        val result = database.deleteStudent(student.studentId)
        if (result) loadStudents()
        return result
    }
}
