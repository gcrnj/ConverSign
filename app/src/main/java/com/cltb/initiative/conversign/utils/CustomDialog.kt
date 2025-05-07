package com.cltb.initiative.conversign.utils

import android.app.Dialog
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.cltb.initiative.conversign.databinding.CustomDialogBinding

class CustomDialog(private val activity: AppCompatActivity) {

    val dialog by lazy {
        Dialog(activity)
    }

    val binding:CustomDialogBinding by lazy {
        CustomDialogBinding.inflate(LayoutInflater.from(activity))
    }

    fun showDialog(
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        positiveAction: () -> Unit,
        negativeAction: (() -> Unit)? = null,
    ) {
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        binding.titleTextView.text = title
//        binding.messageTextView.text = message
//        binding.positiveButton.text = positiveText
//        binding.negativeButton.text = negativeText

        dialog.show()

    }

    fun dismiss() {
        dialog.dismiss()
    }
}