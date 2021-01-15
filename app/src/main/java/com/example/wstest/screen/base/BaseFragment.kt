package com.example.wstest.screen.base

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    protected fun successDialog(success: Boolean, action:() -> Unit = {}) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            with(builder) {
                if (success) {
                    setMessage("Success")
                } else {
                    setMessage("Fail")
                }
                setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    if (success) action()
                }
                show()
            }
        }
    }
}