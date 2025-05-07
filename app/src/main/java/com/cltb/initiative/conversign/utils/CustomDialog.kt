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
        positiveText: String = binding.positiveButton.text.toString(),
        negativeText: String = binding.negativeButton.text.toString(),
        positiveAction: () -> Unit,
        negativeAction: (() -> Unit)? = null,
    ) = with(dialog) {
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        // Texts
        binding.titleTextView.text = title
        binding.messageTextView.text = message
        binding.positiveButton.text = positiveText
        binding.negativeButton.text = negativeText
        // Buttons
        binding.positiveButton.setOnClickListener {
            positiveAction.invoke()
        }
        binding.negativeButton.setOnClickListener {
            negativeAction?.invoke() ?: dismiss()
        }

        show()

    }

    fun dismiss() {
        dialog.dismiss()
    }
}