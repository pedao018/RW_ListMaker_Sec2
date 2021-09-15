package com.raywenderlich.rw_listmaker_sec2.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.rw_listmaker_sec2.R
import com.raywenderlich.rw_listmaker_sec2.databinding.MainFragmentBinding
import com.raywenderlich.rw_listmaker_sec2.model.TaskList

class MainFragment : Fragment(),
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {
    private lateinit var clickListener: MainFragmentListener
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    companion object {
        const val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"

        fun newInstance(clickListener: MainFragmentListener): MainFragment {
            val mainFragment = MainFragment()
            mainFragment.clickListener = clickListener
            return mainFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.mainFragmentListsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity()))
        ).get(MainViewModel::class.java)

        val recyclerViewAdapter = ListSelectionRecyclerViewAdapter(viewModel.lists, this)
        binding.mainFragmentListsRecyclerview.adapter = recyclerViewAdapter
        viewModel.onListAdded = {
            recyclerViewAdapter.listsUpdated()
        }
        return binding.root
    }

    override fun listItemClicked(list: TaskList) {
        clickListener.onItemListsClick(list)
    }

    fun setClickListener(clickListener: MainFragmentListener) {
        this.clickListener = clickListener
    }

    fun refreshRecycleview() {
        binding.mainFragmentListsRecyclerview.adapter?.notifyDataSetChanged()
    }

    interface MainFragmentListener {
        fun onItemListsClick(list: TaskList)
    }

}