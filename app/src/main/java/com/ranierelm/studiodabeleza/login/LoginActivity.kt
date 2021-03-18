package com.ranierelm.studiodabeleza.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ranierelm.studiodabeleza.R
import com.ranierelm.studiodabeleza.ui.RecoveryPasswordActivity
import com.ranierelm.studiodabeleza.ui.RegistrationActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private EditText edit_text_credentials


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        txt_create_account.setOnClickListener(){
            registrationScreen()
        }

        txt_forgot_password.setOnClickListener(){
            recoveryScreen()
        }


    }

    private fun registrationScreen(){

        val goRegister = Intent(this, RegistrationActivity::class.java )
        startActivity(goRegister)
    }

    private fun recoveryScreen(){
        val recoveryPassword = Intent(this, RecoveryPasswordActivity::class.java)
        startActivity(recoveryPassword)
    }

}