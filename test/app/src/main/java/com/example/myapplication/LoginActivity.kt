package com.example.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityLoginBinding


import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var  googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private companion object {
        private const val RC_SIGN_IN =100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()




        binding.SignButton.setOnClickListener{
            Log.d(LoginActivity.TAG,"onCreate : begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }


    }

    private fun checkUser() {
        val firrebaseUser = firebaseAuth.currentUser
        if(firrebaseUser != null) {
            startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == RC_SIGN_IN)
        {
            val accounTask  = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account  = accounTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e:Exception)
            {

            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {

        val credential =GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val firebasUser = firebaseAuth.currentUser
                val uid = firebasUser!!.uid
                val email = firebasUser.email


                if(authResult.additionalUserInfo!!.isNewUser)
                {
                    Toast.makeText(this,"Account Created",Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this,"Loggin Succes",Toast.LENGTH_LONG).show()
                }
                startActivity(Intent(this@LoginActivity,ProfileActivity::class.java))
                finish()

            }
            .addOnFailureListener { e->
                Toast.makeText(this,"Loggin Failed : ${e.message} ",Toast.LENGTH_LONG).show()

            }

    }
}