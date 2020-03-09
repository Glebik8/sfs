package com.blackfish.a1pedal.dialog

import com.blackfish.a1pedal.data.Response

interface Moves {
    fun accept(response: Response)
    fun chat(response: Response)
    fun change(response: Response)
    fun deny(response: Response)
}