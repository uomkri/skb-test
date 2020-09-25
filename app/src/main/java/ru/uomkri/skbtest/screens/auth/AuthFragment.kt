package ru.uomkri.skbtest.screens.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import ru.uomkri.skbtest.R
import ru.uomkri.skbtest.databinding.FragmentAuthBinding
import ru.uomkri.skbtest.screens.MainActivity
import ru.uomkri.skbtest.screens.auth.AuthFragmentDirections
import javax.inject.Inject

//TODO: Insert DI where needed
@AndroidEntryPoint
class AuthFragment() : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    @Inject
    lateinit var oAuthProvider: OAuthProvider.Builder

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var googleAuth: GoogleSignInOptions.Builder

    private val RC_SIGN_IN = 4

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        googleSignInClient = GoogleSignIn.getClient(
            requireContext(),
            googleAuth.requestIdToken(resources.getString(R.string.client_id)).build()
        )

        binding = FragmentAuthBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.buttonNoauth.setOnClickListener {
            it.findNavController()
                .navigate(AuthFragmentDirections.actionAuthMainFragmentToHomeFragment(false))
        }

        binding.buttonGithub.setOnClickListener {
            signInWithGithub()
        }

        binding.buttonGoogle.setOnClickListener {
            signInWithGoogle()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.e("tkn", account.idToken)

                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Snackbar.make(requireView(), e.message!!, Snackbar.LENGTH_SHORT).show()
                Log.e("err", e.message)
                e.printStackTrace()
            }
        }
    }

    private fun signInWithGithub() {
        firebaseAuth.startActivityForSignInWithProvider(requireActivity(), oAuthProvider.build())
            .addOnSuccessListener {
                Log.e("uid", it.user!!.uid)
                startMainActivity(it.user!!.uid, it.additionalUserInfo?.username!!)
            }
            .addOnFailureListener {
                Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
                Log.e("err", it.message)
                it.printStackTrace()
            }
    }

    private fun signInWithGoogle() {
        val intent = googleSignInClient.signInIntent

        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                startMainActivity(it.user!!.uid, it.user!!.displayName!!)
            }
            .addOnFailureListener {
                Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
                Log.e("err", it.message)
                it.printStackTrace()
            }
    }

    private fun startMainActivity(uid: String, username: String) {
        val bundle = Bundle()
        bundle.apply {
            putString("uid", uid)
            putString("username", username)
        }

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        requireActivity().finish()
    }


}