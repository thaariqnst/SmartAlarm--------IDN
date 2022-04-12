package com.thaariq.smartalarm

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment: DialogFragment(),DatePickerDialog.OnDateSetListener {

    private var dialogListener : DateDialogListener? = null

    //== 2 function dibawah untuk memastikan tidak ada fragment yang sedang terbuka agar tidak bertumpuk ==
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DateDialogListener
    }
    override fun onDetach() {
        super.onDetach()
        if (dialogListener != null)dialogListener = null
    }
    // ==============================================================


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOFMonth = calendar.get(Calendar.DATE)
        return DatePickerDialog(activity as Context, this, year, month, dayOFMonth)
    }

    // untuk menyimpan data yang dipilih ke dalam listener
    override fun onDateSet(p0: DatePicker?, year: Int,month: Int, dayOfMonth: Int) {
        dialogListener?.onDialogDateSet(tag, year, month, dayOfMonth)
        Log.i(tag, "onDateSet: $year, $month, $dayOfMonth")
    }

    //untuk dipanggil di activity, untuk menampilkan nilai input
    interface DateDialogListener{
        fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int)
    }
}