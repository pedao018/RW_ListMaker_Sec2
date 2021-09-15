package com.raywenderlich.rw_listmaker_sec2.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.raywenderlich.rw_listmaker_sec2.model.TaskList

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    lateinit var onListAdded: (() -> Unit)
    lateinit var list: TaskList
    lateinit var onTaskAdded: (() -> Unit)
    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    //Chỉ gọi hàm này khi biến lists null hay sao ấy (by lazy). Khi xoay màn hình thì cũng ko gọi lại hàm này
    private fun retrieveLists(): MutableList<TaskList> {
        val sharedPreferencesContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        //Lấy ra từng dòng [key,value] trong SharedPreferences
        for (item in sharedPreferencesContents) {
            val itemHashSet = ArrayList(item.value as HashSet<String>)
            val list = TaskList(item.key, itemHashSet)
            taskLists.add(list)
        }
        return taskLists
    }

    fun saveList(list: TaskList) {
        sharedPreferences.edit().putStringSet(
            list.name,
            list.tasks.toHashSet()
        ).apply()
        lists.add(list)
        onListAdded.invoke()
    }

    fun updateList(list: TaskList) {
        sharedPreferences.edit().putStringSet(
            list.name,
            list.tasks.toHashSet()
        ).apply()
        lists.add(list)
    }

    fun refreshList() {
        lists.clear()
        lists.addAll(retrieveLists())
    }

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }
}