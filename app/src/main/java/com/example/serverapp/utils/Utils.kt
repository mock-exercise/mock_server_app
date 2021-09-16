package com.example.serverapp.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.connectorlibrary.enitity.Active
import com.example.connectorlibrary.enitity.Gender
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("set_birthday")
fun setBirthday(textView: TextView, date: Long) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val netDate = Date(date)
    textView.text = sdf.format(netDate)
}

@BindingAdapter("gender_id", "list_gender")
fun setGender(textView: TextView, gender_id: Int, listGender: List<Gender>) {
    val gender = listGender.find {
        it.gender_id == gender_id
    }
    textView.text = gender?.gender_name
}

@BindingAdapter("active_id", "list_active")
fun setActive(textView: TextView, active_id: Int, listActive: List<Active>) {
    val active = listActive.find {
        it.active_id == active_id
    }
    active?.active_name?.let {
        textView.text = if (it) "Hoạt động" else "Đã khóa"
    }
}