package com.idle.togeduck.util

import android.view.View
import android.widget.TextView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View) : ViewContainer(view) {
     val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
     lateinit var day: CalendarDay
     private var selectedDate: LocalDate? = null

     init {
          view.setOnClickListener {
               // Use the CalendarDay associated with this container.
          }
     }
}