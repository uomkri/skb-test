package ru.uomkri.skbtest.screens.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import ru.uomkri.skbtest.databinding.FragmentDetailsBinding

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val owner = args.owner
        val repoName = args.repoName

        viewModel.getRepo(owner, repoName)
        viewModel.clearError()

        val userPrefs = requireActivity().getSharedPreferences("userData", Context.MODE_PRIVATE)
        val uid = userPrefs.getString("uid", "")
        val isLoggedIn = args.isLoggedIn

        if (isLoggedIn) binding.favButton.visibility =
            View.VISIBLE else binding.favButton.visibility = View.GONE

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })

        viewModel.repo.observe(viewLifecycleOwner, Observer {
            if (it != null) {

            if (isLoggedIn) viewModel.isRepoInFavorites(it.id, uid!!)

            Log.e("id", it.id.toString())
            binding.repoName.text = it.name
            binding.ownerName.text = it.owner.login
            binding.createdOn.text = it.createdAt!!.take(10)
            binding.forkCount.text = "${it.forksCount} forks"
            binding.starCount.text = "${it.stargazersCount} stars"
            if (it.description != null) binding.description.text = it.description
            if (it.owner.avatarUrl != null) {
                Picasso.get()
                    .load(it.owner.avatarUrl)
                    .fit()
                    .into(binding.ownerAvatar)
                }
            }
        })

        viewModel.isInFavorites.observe(viewLifecycleOwner, Observer {
            Log.e("fav", it.toString())
            when (it) {
                true -> {
                    binding.favButton.apply {
                        text = "Remove from favorites"
                        setOnClickListener {
                            viewModel.deleteRepoFromFavorites(viewModel.repo.value!!.id, uid!!)
                            this.findNavController()
                                .navigate(DetailsFragmentDirections.actionDetailsFragmentToHomeFragment())
                        }
                    }

                }
                false -> {
                    binding.favButton.apply {
                        text = "Add to favorites"
                        setOnClickListener {
                            viewModel.insertRepoIntoFavorites(viewModel.repo.value!!, uid!!)
                            this.findNavController()
                                .navigate(DetailsFragmentDirections.actionDetailsFragmentToHomeFragment())
                        }
                    }
                }
            }
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearSelectedRepo()
    }
}
