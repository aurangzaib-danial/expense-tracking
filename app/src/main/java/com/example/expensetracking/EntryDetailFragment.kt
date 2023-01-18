package com.example.expensetracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.expensetracking.data.Entry
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracking.databinding.FragmentEntryDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * [EntryDetailFragment] displays the details of the selected entry.
 */
class EntryDetailFragment : Fragment() {
    private val navigationArgs: EntryDetailFragmentArgs by navArgs()
    lateinit var entry: Entry

    private val viewModel: AppViewModel by activityViewModels {
        AppViewModelFactory(
            (activity?.application as ExpenseTrackingApplication).database.entryDao()
        )
    }

    private var _binding: FragmentEntryDetailBinding? = null
    private val binding get() = _binding!!

    /**
     * Binds views with the passed in entry data.
     */
    private fun bind(entry: Entry) {
        binding.apply {
            entryName.text = entry.entryName
            entryCategory.text = entry.entryCategory
            entryDate.text = entry.entryDate
            entryAmount.text = entry.entryAmount.toString()
            entryNote.text = entry.entryNote
            editEntry.setOnClickListener { editEntry() }
            deleteEntry.setOnClickListener { showConfirmationDialog() }
        }
    }

    /**
     * Navigate to the Edit item screen.
     */
    private fun editEntry() {
        val action = EntryDetailFragmentDirections.actionEntryDetailFragmentToAddEntryFragment(
            "Edit Entry",
            entry.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the entry.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Are you sure you want to delete")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteEntry()
            }
            .show()
    }

    /**
     * Deletes the current entry and navigates to the list fragment.
     */
    private fun deleteEntry() {
        viewModel.deleteEntry(entry)
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.entryId
        // Retrieve the entry details using the entryId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveEntry(id).observe(this.viewLifecycleOwner) { selectedEntry ->
            entry = selectedEntry
            bind(entry)
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}