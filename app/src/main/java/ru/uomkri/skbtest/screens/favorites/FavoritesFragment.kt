package ru.uomkri.skbtest.screens.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ru.uomkri.skbtest.databinding.FragmentFavoritesBinding
import ru.uomkri.skbtest.screens.home.HomeFragmentDirections
import ru.uomkri.skbtest.utils.FavListAdapter
import ru.uomkri.skbtest.utils.ListAdapter
import ru.uomkri.skbtest.utils.RecyclerItemClickListener
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavListAdapter
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentFavoritesBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val userPrefs = requireActivity().getSharedPreferences("userData", Context.MODE_PRIVATE)
        val uid = userPrefs.getString("uid", "")

        viewModel.getFavRepos(uid!!)
        viewModel.clearError()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        val divider = DividerItemDecoration(context, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(divider)

        viewModel.favRepos.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter = FavListAdapter(it)
                binding.recyclerView.adapter = adapter
            }
        })

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    when {
                        newText != null && newText.isEmpty() -> adapter.refreshList(viewModel.favRepos.value!!)
                        newText != null && newText.isNotEmpty() -> adapter.refreshList(
                            viewModel.filterByName(
                                newText
                            )
                        )
                    }

                    return false
                }
            }
        )

        binding.recyclerView.addOnItemTouchListener(RecyclerItemClickListener(binding.recyclerView,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val item = viewModel.favRepos.value!![position]
                    view.findNavController().navigate(
                        FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(
                            owner = item.owner.login,
                            repoName = item.name,
                            isLoggedIn = true
                        )
                    )
                }
            }
        ))

        binding.buttonClear.setOnClickListener {
            viewModel.clearFavorites(uid)
            requireView().findNavController()
                .navigate(FavoritesFragmentDirections.actionFavoritesFragment2ToHomeFragment2())
        }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })

        return binding.root
    }
}