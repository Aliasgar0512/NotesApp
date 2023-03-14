package com.example.notes_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.databinding.FragmentMainBinding
import com.example.notes_app.models.NoteResponse
import com.example.notes_app.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NoteViewModel>()
    private lateinit var adapter: NoteAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteClicked) // passing function reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteObserver()
        viewModel.getNotes()

        binding.noteList.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.noteList.adapter = adapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    private fun noteObserver() {
        viewModel.noteResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onNoteClicked(noteResponse: NoteResponse) {
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))// serialisation
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}