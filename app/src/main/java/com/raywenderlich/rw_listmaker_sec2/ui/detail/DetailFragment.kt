package com.raywenderlich.rw_listmaker_sec2.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.rw_listmaker_sec2.databinding.DetailFragmentBinding
import com.raywenderlich.rw_listmaker_sec2.model.TaskList
import com.raywenderlich.rw_listmaker_sec2.ui.main.MainActivity
import com.raywenderlich.rw_listmaker_sec2.ui.main.MainViewModel
import com.raywenderlich.rw_listmaker_sec2.ui.main.MainViewModelFactory

class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = DetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.detailFragmentRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity()))
        ).get(MainViewModel::class.java)

        val list: TaskList? = arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)
        list?.let {
            viewModel.list = list
            requireActivity().title = list.name
        }

        val recyclerViewAdapter = DettailItemRecyclerViewAdapter(viewModel.list)
        binding.detailFragmentRecyclerview.adapter = recyclerViewAdapter
        viewModel.onTaskAdded = {
            recyclerViewAdapter.notifyDataSetChanged()
        }
        recyclerViewAdapter.onItemClick = { task ->
            Toast.makeText(requireActivity(), task, Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

}