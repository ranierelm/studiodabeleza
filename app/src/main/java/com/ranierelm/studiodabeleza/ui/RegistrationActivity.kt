package com.ranierelm.studiodabeleza.ui


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ranierelm.studiodabeleza.R
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    //Elementos interface do usuário

    private lateinit var editName: EditText
    private lateinit var editLastname: EditText
    private lateinit var editPhone: EditText
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var btnRegister: Button
    private lateinit var mProgressBar: ProgressDialog

    //Referência ao banco de dados
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth

    private val TAG = "RegistrationActivity"

    //Variáveis globais
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var password: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

       initialize()

    }
    private fun initialize(){
        editName = findViewById<EditText>(R.id.editName)
        editLastname = findViewById<EditText>(R.id.editLastname)
        editPhone = findViewById<EditText>(R.id.editPhone)
        emailRegister = findViewById<EditText>(R.id.emailRegister)
        passwordRegister = findViewById<EditText>(R.id.passwordRegister)
        btnRegister = findViewById<Button>(R.id.btnRegister)
        mProgressBar = ProgressDialog(this)


        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener { createNewAccount() }
    }


    private fun createNewAccount(){

        firstName = editName.text.toString()
        lastName = editLastname.text.toString()
        email = emailRegister.text.toString()
        password = passwordRegister.text.toString()

        if(!isEmpty(firstName) && !isEmpty(lastName) && !isEmpty(email) && !isEmpty(password)){
            Toast.makeText(this, "Informações preenchidas corretamente.",
                    Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this, "Complete os campos obrigatórios!",
                    Toast.LENGTH_SHORT).show()
        }

        mProgressBar.setMessage("Registrando Usuário...")
        mProgressBar.show()

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            mProgressBar.hide()

            if(task.isSuccessful){
                Log.d(TAG, "CreateUserWithEmail:Success")

                val userId = mAuth.currentUser.uid

                //Checar se o usuário verificou o email
                verifyEmail()

                val currentUserDb = mDatabaseReference.child(userId)
                currentUserDb.child("firstName").setValue(firstName)
                currentUserDb.child("firstName").setValue(lastName)

                //Atualizar informações no banco de dados
                updateUserinfoandUi()

            } else {
                Log.w(TAG, "CreateUserWithEmail:Failed", task.exception)
                Toast.makeText(this@RegistrationActivity, "Authentication failed!",
                        Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun updateUserinfoandUi(){
        //Iniciar a nova atividade
        val intent = Intent(this@RegistrationActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verifyEmail(){
        val mUser = mAuth.currentUser
        mUser.sendEmailVerification().addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                Toast.makeText(this@RegistrationActivity,
                        "Verification Email sent to"+
                                mUser.getEmail(), Toast.LENGTH_SHORT).show()
            } else{
                Log.e(TAG, "SendEmailVerification", task.exception)
                Toast.makeText(this@RegistrationActivity,
                        "Failed to send Verification Email", Toast.LENGTH_SHORT).show()
            }
        }
    }

}