package com.example.expensetracking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracking.databinding.EntryListFragmentBinding

/**
 * Main fragment displaying details for all entries in the database.
 */
class EntryListFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels {
        AppViewModelFactory(
            (activity?.application as ExpenseTrackingApplication).database.entryDao()
        )
    }

    private var _binding: EntryListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EntryListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EntryListAdapter {
            val action =
                EntryListFragmentDirections.actionEntryListFragmentToEntryDetailFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        // Attach an observer on the allEntries list to update the UI automatically when the data
        // changes.
        viewModel.allEntries.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = EntryListFragmentDirections.actionEntryListFragmentToAddEntryFragment(
                "Add Entry"
            )
            this.findNavController().navigate(action)
        }
    }
}