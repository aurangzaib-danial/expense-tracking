package com.example.expensetracking

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracking.data.Entry
import com.example.expensetracking.databinding.FragmentAddEntryBinding

/**
 * Fragment to add or update an entry in the database.
 */
class AddEntryFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: AppViewModel by activityViewModels {
        AppViewModelFactory(
            (activity?.application as ExpenseTrackingApplication).database
                .entryDao()
        )
    }

    private val navigationArgs: EntryDetailFragmentArgs by navArgs()

    lateinit var entry: Entry


    // Binding object instance corresponding to the fragment_add_entry.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.entryName.text.toString(),
            binding.entryCategory.text.toString(),
            binding.entryDate.text.toString(),
            binding.entryAmount.text.toString()
        )
    }

    /**
     * Binds views with the passed in [Entry] information.
     */
    private fun bind(entry: Entry) {
        binding.apply {
            entryName.setText(entry.entryName)
            entryCategory.setText(entry.entryCategory)
            entryDate.setText(entry.entryDate)
            entryAmount.setText(entry.entryAmount.toString())
            entryNote.setText(entry.entryNote)
            saveAction.setOnClickListener { updateEntry() }
        }
    }

    /**
     * Inserts the new Entry into database and navigates up to list fragment.
     */
    private fun addNewEntry() {
        if (isEntryValid()) {
            viewModel.addNewEntry(
                binding.entryName.text.toString(),
                binding.entryCategory.text.toString(),
                binding.entryDate.text.toString(),
                binding.entryAmount.text.toString(),
                binding.entryNote.text.toString()
            )
            val action = AddEntryFragmentDirections.actionAddEntryFragmentToEntryListFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * Updates an existing Entry in the database and navigates up to list fragment.
     */
    private fun updateEntry() {
        if (isEntryValid()) {
            viewModel.updateEntry(
                this.navigationArgs.entryId,
                this.binding.entryName.text.toString(),
                this.binding.entryCategory.text.toString(),
                this.binding.entryDate.text.toString(),
                this.binding.entryAmount.text.toString(),
                this.binding.entryNote.text.toString()
            )
            val action = AddEntryFragmentDirections.actionAddEntryFragmentToEntryListFragment()
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.entryId
        if (id > 0) {
            viewModel.retrieveEntry(id).observe(this.viewLifecycleOwner) { selectedEntry ->
                entry = selectedEntry
                bind(entry)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewEntry()
            }
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}