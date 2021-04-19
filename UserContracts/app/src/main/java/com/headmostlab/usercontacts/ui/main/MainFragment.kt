package com.headmostlab.usercontacts.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.headmostlab.usercontacts.R
import com.headmostlab.usercontacts.data.ContactRepository
import com.headmostlab.usercontacts.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val REQUEST_CODE = 1000
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ContactAdapter

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactRepository = ContactRepository(requireContext().contentResolver)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(contactRepository)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ContactAdapter()
        binding.recyclerView.adapter = adapter
        checkContactPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkContactPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED ->
                    getContract()
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Для отображения списка контактов необходимо предоставить к ним доступ")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("Не надо") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> requestPermission()
            }
        }
    }

    private fun getContract() {
        viewModel.getContacts().observe(viewLifecycleOwner, { render(it) })
    }

    private fun render(state: AppState) {
        when (state) {
            is AppState.Loaded -> {
                adapter.submitList(state.contacts)
                binding.loadingProgress.visibility = View.GONE
            }
            is AppState.Error -> {
                binding.main.showSnackbar(R.string.error_message, R.string.button_reload) {
                    viewModel.getContacts()
                }
                binding.loadingProgress.visibility = View.GONE
            }
            AppState.Loading -> {
                binding.loadingProgress.visibility = View.VISIBLE
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getContract()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("Список останется пустым, т.к. доступ к контактам не был предоставлен")
                            .create()
                            .show()
                    }
                }
            }
        }
    }
}
