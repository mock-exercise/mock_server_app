package com.example.serverapp.utils

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.connectorlibrary.enitity.Gender
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("set_birthday")
fun setBirthday(textView: TextView, date: Long) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val netDate = Date(date)
    textView.text = sdf.format(netDate)
}

@BindingAdapter(value = ["gender_id", "list_gender"], requireAll = true )
fun setGender(textView: TextView, gender_id: Int, listGender: List<Gender>?) {
    Log.e("TAG", "setGender: $listGender", )
    Log.e("TAG", "setGender: $gender_id", )
    val gender = listGender?.find {
        it.gender_id == gender_id
    }
    textView.text = gender?.gender_name
}

@BindingAdapter("active")
fun setActive(textView: TextView, isActive: Boolean) {
        textView.text = if (isActive) "Hoạt động" else "Đã khóa"
}