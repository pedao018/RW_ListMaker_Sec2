package com.raywenderlich.rw_listmaker_sec2.ui.detail

import androidx.lifecycle.ViewModel
import com.raywenderlich.rw_listmaker_sec2.model.TaskList

class DetailViewModel : ViewModel() {
    lateinit var onTaskAdded: (() -> Unit)
    lateinit var list: TaskList
    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }
}