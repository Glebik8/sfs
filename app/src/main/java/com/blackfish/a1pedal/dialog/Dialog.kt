package com.blackfish.a1pedal.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.blackfish.a1pedal.ProfileInfo.User
import com.blackfish.a1pedal.data.Response


class Dialog(private val event: Response, private val moves: Moves): DialogFragment() {

    private val names = arrayOf("Принять", "Чат", "Изменить", "Отменить")
    private val actions =
            arrayOf("Принять", "Чат", "Изменить", "Отменить") zip arrayOf(moves::accept, moves::chat, moves::change, moves::deny)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            if ((!User.getInstance().isDriver
                    || event.status in listOf("waitingD"))
                    && !(User.getInstance().isDriver && event.status == "rejected")) {
                builder.setTitle(event.name)
                        .setItems(names) { _, which ->
                            actions[which].second.invoke(event)
                        }
            } else {
                builder.setTitle(event.name)
                        .setItems(arrayOf("Чат", "Изменить")) { _, which ->
                            when(which) {
                                0 -> moves.chat(event)
                                else -> moves.change(event)
                            }
                        }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun createDialog() = if (User.getInstance().isDriver) {
        1
    } else {
        1
    }
}