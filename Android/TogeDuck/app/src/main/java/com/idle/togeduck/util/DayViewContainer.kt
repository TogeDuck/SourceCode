package com.idle.togeduck.util

import android.view.View
import android.widget.TextView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
     val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}