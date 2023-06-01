package com.jay.weatherapp.common.ui.viewmodel

import java.util.*

/**
 * representing one-off viewmodel event. can be used to send error to ui, show a dialog etc.
 * _id is used to make every event instance unique (data class has a structural equality) so
 * that StateFlow will not skip state emission.
 */
data class Event<out T>(private val content: T?, private val _id: UUID = UUID.randomUUID()) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
