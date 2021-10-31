package com.raywenderlich.rw_sec2_listmaker.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.rw_sec2_listmaker.R
import com.raywenderlich.rw_sec2_listmaker.databinding.MainActivityBinding
import com.raywenderlich.rw_sec2_listmaker.model.TaskList
import com.raywenderlich.rw_sec2_listmaker.ui.detail.DetailActivity
import com.raywenderlich.rw_sec2_listmaker.ui.detail.DetailFragment

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentListener {
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val PREFERENCE_NAME = "PREFERENCE_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreference = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(
            this,
            //MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
            MainViewModelFactory(sharedPreference)
        ).get(MainViewModel::class.java)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance(this)
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.main_activity_container
            } else {
                R.id.main_fragment_container
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment, MainFragment.MAIN_FRAGMENT_TAG)
            }
            /*supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment, MainFragment.MAIN_FRAGMENT_TAG)
                .commitNow()*/
        } else {
            /*val mainFragment =
                supportFragmentManager.findFragmentByTag(MainFragment.MAIN_FRAGMENT_TAG) as MainFragment
            mainFragment.setClickListener(this)*/
            val mainFragment: MainFragment = if (binding.mainFragmentContainer == null) {
                supportFragmentManager.findFragmentById(R.id.main_activity_container) as MainFragment
            } else {
                supportFragmentManager.findFragmentById(R.id.main_fragment_container) as MainFragment
            }

            mainFragment.setClickListener(this)
        }
        binding.floatingActionButton.setOnClickListener { showCreateListDialog() }

        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    // Handle the Intent
                    intent?.let {
                        viewModel.updateList(intent.getParcelableExtra(INTENT_LIST_KEY)!!)
                        viewModel.refreshList()
                        if (binding.mainFragmentContainer == null) {
                            val mainFragment =
                                supportFragmentManager.findFragmentById(R.id.main_activity_container) as MainFragment
                            mainFragment.refreshRecycleview()
                        }

                    }
                }
            }

        val clickListener = { view: View ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)
        }
        binding.animBtn.setOnClickListener { view -> clickListener(view) }
    }

    private fun showCreateListDialog() {
        // 1
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        // 2
        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        // 3
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            val taskList = TaskList(listTitleEditText.text.toString())
            viewModel.saveList(taskList)
            showListDetail(taskList)
        }

        // 4
        builder.create().show()
    }

    override fun onItemListsClick(list: TaskList) {
        showListDetail(list)
    }

    private fun showListDetail(list: TaskList) {
        if (binding.mainFragmentContainer == null) {
            val listDetailIntent = Intent(this, DetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            //registerForActivityResult(listDetailIntent,LIST_DETAIL_REQUEST_CODE)
            startForResult.launch(listDetailIntent)
        } else {
            val bundle = bundleOf(INTENT_LIST_KEY to list)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.detail_fragment_container,
                    DetailFragment::class.java, bundle, null
                )
            }
            binding.floatingActionButton.setOnClickListener { showCreateTaskDialog() }
        }
    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onBackPressed() {
        val listDetailFragment =
            supportFragmentManager.findFragmentById(R.id.detail_fragment_container)
        if (listDetailFragment == null)
            super.onBackPressed()
        else {
            title = resources.getString(R.string.app_name)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listDetailFragment)
            }
            binding.floatingActionButton.setOnClickListener {
                showCreateListDialog()
            }
        }
    }
}