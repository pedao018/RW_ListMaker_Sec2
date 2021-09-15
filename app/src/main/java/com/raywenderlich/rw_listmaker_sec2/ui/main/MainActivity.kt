package com.raywenderlich.rw_listmaker_sec2.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.rw_listmaker_sec2.R
import com.raywenderlich.rw_listmaker_sec2.databinding.MainActivityBinding
import com.raywenderlich.rw_listmaker_sec2.model.TaskList

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
            val fragmentContainerViewId: Int = R.id.main_activity_container
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
            val mainFragment: MainFragment =
                supportFragmentManager.findFragmentById(R.id.main_activity_container) as MainFragment
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
        }

        // 4
        builder.create().show()
    }

    override fun onItemListsClick(list: TaskList) {
        Toast.makeText(this, "Click Item " + list.name, Toast.LENGTH_SHORT).show()
    }
}