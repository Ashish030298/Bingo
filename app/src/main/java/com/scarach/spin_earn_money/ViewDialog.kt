package com.scarach.spin_earn_money

import android.R
import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView


class ViewDialog {
    fun showDialog(activity: Activity?, msg: String?) {
        val dialog = activity?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(com.scarach.spin_earn_money.R.layout.view_dialog)
        val text = dialog?.findViewById<TextView>(com.scarach.spin_earn_money.R.id.text_dialog)
        text?.text = msg
        val dialogButton: Button = dialog?.findViewById(com.scarach.spin_earn_money.R.id.btn_dialog) as Button
        dialogButton.setOnClickListener {
            activity.finish()
            dialog.dismiss()
        }
        dialog.show()
    }
}