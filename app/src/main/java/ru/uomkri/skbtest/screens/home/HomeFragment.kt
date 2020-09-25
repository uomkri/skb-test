package ru.uomkri.skbtest.screens.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.uomkri.skbtest.databinding.FragmentHomeBinding
import ru.uomkri.skbtest.utils.ListAdapter
import ru.uomkri.skbtest.utils.RecyclerItemClickListener


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val args: HomeFragmentArgs by navArgs()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.fetchRepos("")
        viewModel.clearError()

        val adapter = ListAdapter()

        Log.e("log", args.isLoggedIn.toString())

        val layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        val divider = DividerItemDecoration(context, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(divider)
        viewModel.getRepos().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                adapter.submitList(it)
            }
        })

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        viewModel.fetchRepos(newText)
                    }

                    return false
                }
            }
        )

        binding.recyclerView.addOnItemTouchListener(RecyclerItemClickListener(binding.recyclerView,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val item = viewModel._repoList.value!![position]
                    view.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                            owner = item.owner.login,
                            repoName = item.name,
                            isLoggedIn = args.isLoggedIn
                        )
                    )
                }
            }
        ))

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })

        return binding.root
    }
}