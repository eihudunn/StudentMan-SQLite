package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class AddFragment : Fragment() {

    private lateinit var studentName: EditText
    private lateinit var studentId: EditText
    private lateinit var addStudent: Button
    private val studentViewModel: StudentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studentName = view.findViewById(R.id.et_student_name)
        studentId = view.findViewById(R.id.et_student_id)
        addStudent = view.findViewById(R.id.btn_add_student)

        addStudent.setOnClickListener {
            val name = studentName.text.toString()
            val id = studentId.text.toString()

            if (name.isNotEmpty() && id.isNotEmpty()) {
                val success = studentViewModel.addStudent(StudentModel(name, id))
                if (success) {
                    Toast.makeText(context, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(context, "Thêm sinh viên thất bại", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}