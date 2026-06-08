package com.example.todolistjadwal.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistjadwal.data.Schedule
import com.example.todolistjadwal.databinding.FragmentScheduleBinding
import com.example.todolistjadwal.databinding.DialogAddScheduleBinding
import java.util.*

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
    private var startTime: String = "08:00"
    private var endTime: String = "10:00"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ScheduleAdapter { schedule ->
            viewModel.deleteSchedule(schedule)
        }
        binding.rvSchedule.adapter = adapter
        binding.rvSchedule.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allSchedules.observe(viewLifecycleOwner) { schedules ->
            val sortedSchedules = schedules.sortedWith(compareBy({
                if (it.dayOfWeek == 1) 7 else it.dayOfWeek - 1 
            }, { it.startTime }))

            val listItems = mutableListOf<ScheduleListItem>()
            var currentDay = -1
            val days = arrayOf("", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

            for (schedule in sortedSchedules) {
                if (schedule.dayOfWeek != currentDay) {
                    currentDay = schedule.dayOfWeek
                    listItems.add(ScheduleListItem.Header(days[currentDay]))
                }
                listItems.add(ScheduleListItem.ScheduleItem(schedule))
            }
            adapter.submitList(listItems)
        }

        binding.fabAddSchedule.setOnClickListener {
            showAddScheduleDialog()
        }
    }

    private fun showAddScheduleDialog() {
        val dialogBinding = DialogAddScheduleBinding.inflate(layoutInflater)
        
        val days = arrayOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerDay.adapter = spinnerAdapter

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Jadwal Kuliah")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { _, _ ->
                val subject = dialogBinding.etSubjectName.text.toString()
                val room = dialogBinding.etRoom.text.toString()
                val dayIndex = dialogBinding.spinnerDay.selectedItemPosition + 2 // 2 = Senin in Calendar.MONDAY
                val dayOfWeek = if (dayIndex > 7) 1 else dayIndex // Wrap for Sunday

                if (subject.isNotEmpty()) {
                    val schedule = Schedule(
                        subjectName = subject,
                        dayOfWeek = dayOfWeek,
                        startTime = startTime,
                        endTime = endTime,
                        room = room
                    )
                    viewModel.insertSchedule(schedule)
                } else {
                    Toast.makeText(requireContext(), "Nama Matkul tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)

        dialogBinding.btnPickStartTime.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                startTime = String.format("%02d:%02d", hour, minute)
                dialogBinding.btnPickStartTime.text = "Mulai: $startTime"
            }, 8, 0, true).show()
        }

        dialogBinding.btnPickEndTime.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                endTime = String.format("%02d:%02d", hour, minute)
                dialogBinding.btnPickEndTime.text = "Selesai: $endTime"
            }, 10, 0, true).show()
        }

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
