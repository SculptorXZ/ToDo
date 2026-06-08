package com.example.todolistjadwal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistjadwal.data.Schedule
import com.example.todolistjadwal.databinding.ItemDayHeaderBinding
import com.example.todolistjadwal.databinding.ItemScheduleBinding

sealed class ScheduleListItem {
    data class Header(val dayName: String) : ScheduleListItem()
    data class ScheduleItem(val schedule: Schedule) : ScheduleListItem()
}

class ScheduleAdapter(
    private val onDeleteClick: (Schedule) -> Unit
) : ListAdapter<ScheduleListItem, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ScheduleListItem.Header -> TYPE_HEADER
            is ScheduleListItem.ScheduleItem -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(
                ItemDayHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ScheduleViewHolder(
                ItemScheduleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is HeaderViewHolder && item is ScheduleListItem.Header) {
            holder.bind(item.dayName)
        } else if (holder is ScheduleViewHolder && item is ScheduleListItem.ScheduleItem) {
            holder.bind(item.schedule)
        }
    }

    inner class HeaderViewHolder(private val binding: ItemDayHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dayName: String) {
            binding.tvDayHeader.text = dayName
        }
    }

    inner class ScheduleViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: Schedule) {
            binding.tvSubjectName.text = schedule.subjectName
            binding.tvScheduleTime.text = "${schedule.startTime} - ${schedule.endTime}"
            binding.tvScheduleRoom.text = "Ruang: ${schedule.room}"

            binding.ivDeleteSchedule.setOnClickListener {
                onDeleteClick(schedule)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<ScheduleListItem>() {
        override fun areItemsTheSame(oldItem: ScheduleListItem, newItem: ScheduleListItem): Boolean {
            return if (oldItem is ScheduleListItem.Header && newItem is ScheduleListItem.Header) {
                oldItem.dayName == newItem.dayName
            } else if (oldItem is ScheduleListItem.ScheduleItem && newItem is ScheduleListItem.ScheduleItem) {
                oldItem.schedule.id == newItem.schedule.id
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: ScheduleListItem, newItem: ScheduleListItem): Boolean {
            return oldItem == newItem
        }
    }
}
