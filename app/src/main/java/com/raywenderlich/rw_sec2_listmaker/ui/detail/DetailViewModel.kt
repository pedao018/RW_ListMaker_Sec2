package com.raywenderlich.rw_sec2_listmaker.ui.detail

import androidx.lifecycle.ViewModel
import com.raywenderlich.rw_sec2_listmaker.model.TaskList

class DetailViewModel : ViewModel() {
    lateinit var onTaskAdded: (() -> Unit)
    lateinit var list: TaskList
    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }
}