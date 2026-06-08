package com.example.todolistjadwal.ui

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistjadwal.data.Task
import com.example.todolistjadwal.databinding.FragmentTasksBinding
import com.example.todolistjadwal.databinding.DialogAddTaskBinding
import com.example.todolistjadwal.util.NotificationReceiver
import java.util.*

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
    private var selectedDeadline: Long = System.currentTimeMillis()
    private var selectedHour: Int = 8
    private var selectedMinute: Int = 0

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Izin Notifikasi Diberikan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Izin Notifikasi Ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter(
            onCheckedChange = { task ->
                viewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
            },
            onDeleteClick = { task ->
                viewModel.deleteTask(task)
            }
        )

        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }

        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Tugas/Ujian")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { _, _ ->
                val title = dialogBinding.etTaskTitle.text.toString()
                val desc = dialogBinding.etTaskDesc.text.toString()
                val type = if (dialogBinding.rbTask.isChecked) "TASK" else "EXAM"
                
                if (title.isNotEmpty()) {
                    val task = Task(title = title, description = desc, deadline = selectedDeadline, type = type)
                    viewModel.insertTask(task)
                    
                    if (dialogBinding.cbSetNotification.isChecked) {
                        checkPermissionAndScheduleNotifications(title, desc, selectedDeadline)
                    }
                } else {
                    Toast.makeText(requireContext(), "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)

        dialogBinding.btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val selectedCal = Calendar.getInstance()
                selectedCal.timeInMillis = selectedDeadline
                selectedCal.set(year, month, dayOfMonth)
                selectedDeadline = selectedCal.timeInMillis
                dialogBinding.btnPickDate.text = "$dayOfMonth/${month + 1}/$year"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        dialogBinding.btnPickTime.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                val selectedCal = Calendar.getInstance()
                selectedCal.timeInMillis = selectedDeadline
                selectedCal.set(Calendar.HOUR_OF_DAY, hour)
                selectedCal.set(Calendar.MINUTE, minute)
                selectedCal.set(Calendar.SECOND, 0)
                selectedDeadline = selectedCal.timeInMillis
                dialogBinding.btnPickTime.text = String.format("%02d:%02d", hour, minute)
            }, selectedHour, selectedMinute, true).show()
        }

        builder.show()
    }

    private fun checkPermissionAndScheduleNotifications(title: String, desc: String, time: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                scheduleMultipleNotifications(title, desc, time)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            scheduleMultipleNotifications(title, desc, time)
        }
    }

    private fun scheduleMultipleNotifications(title: String, desc: String, time: Long) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intervals = listOf(30, 25, 20, 15, 10, 5, 0)
        val baseId = System.currentTimeMillis().toInt()

        for (minutesBefore in intervals) {
            val notifyTime = time - (minutesBefore * 60 * 1000)
            
            if (notifyTime > System.currentTimeMillis()) {
                val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
                    putExtra("title", "Pengingat Tugas")
                    val message = if (minutesBefore == 0) {
                        "Deadline : $title"
                    } else {
                        "Deadline dalam $minutesBefore menit: $title"
                    }
                    putExtra("message", message)
                    putExtra("id", baseId + minutesBefore)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    baseId + minutesBefore,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                try {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        notifyTime,
                        pendingIntent
                    )
                } catch (e: SecurityException) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        notifyTime,
                        pendingIntent
                    )
                }
            }
        }
        Toast.makeText(requireContext(), "Notifikasi pengingat dijadwalkan", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
