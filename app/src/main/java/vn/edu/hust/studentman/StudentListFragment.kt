package vn.edu.hust.studentman

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle

class StudentListFragment : Fragment() {
    private val studentViewModel: StudentViewModel by activityViewModels()
    private lateinit var studentAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.list_view_students)
        studentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = studentAdapter

        registerForContextMenu(listView)

        studentViewModel.students.observe(viewLifecycleOwner) { students ->
            studentAdapter.clear()
            studentAdapter.addAll(students.map { "${it.studentName} - ${it.studentId}" })
            studentAdapter.notifyDataSetChanged()
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_add_new -> {
                        findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val student = studentViewModel.students.value?.get(info.position)

        return when (item.itemId) {
            R.id.context_edit -> {
                student?.let {
                    val action = StudentListFragmentDirections
                        .actionStudentListFragmentToEditStudentFragment(it.studentName, it.studentId)
                    findNavController().navigate(action)
                }
                true
            }
            R.id.context_remove -> {
                student?.let { confirmDeleteStudent(it) }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun confirmDeleteStudent(student: StudentModel) {
        AlertDialog.Builder(requireContext())
            .setMessage("Bạn chắc chắn muốn xóa sinh viên này chứ?")
            .setPositiveButton("Có") { _, _ ->
                val success = studentViewModel.deleteStudent(student)
                if (success) {
                    Toast.makeText(requireContext(), "Đã xóa ${student.studentName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Không", null)
            .show()
    }
}