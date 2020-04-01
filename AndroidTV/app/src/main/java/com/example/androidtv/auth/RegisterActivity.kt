package com.example.androidtv.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.androidtv.MainActivity
import com.example.androidtv.R
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmConfiguration.Builder
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val intentLogin = Intent(this, LoginActivity::class.java);
        register.setOnClickListener {
            val passwordPattern =
                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$".toRegex();
            val usernamePattern =
                "^[a-zA-Z0-9]+([a-zA-Z0-9](_|-| )[a-zA-Z0-9])*[a-zA-Z0-9]*$".toRegex();
            val emailPattern =
               "^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$".toRegex();
            if (username.text.length == 0) {
                username.requestFocus();
                username.setError("User name is required");
            } else
                if (password.text.length == 0) {
                    password.requestFocus();
                    password.setError("Password is required")
                } else
                    if (email.text.length == 0) {
                        email.requestFocus();
                        email.setError("Email is required")
                    } else
                        if (!email.text.matches(emailPattern)) {
                            email.requestFocus();
                            email.setError("Email isn't valid")
                        } else
                            if (!password.text.matches(passwordPattern)) {
                                password.requestFocus();
                                password.setError("Password is minimum 8 characters, at least one letter, one number and one special character:")
                            } else
                                if (!username.text.matches(usernamePattern)) {
                                    username.requestFocus();
                                    username.setError("User name doesn't have space and special characters ")
                                } else if (password.text.toString() != confirmpassword.text.toString()) {
                                    confirmpassword.requestFocus();
                                    confirmpassword.setError("Confirm password isn't same password");
                                } else{
                                    register(intentLogin);
                                }

        }
    }

    @Throws(IOException::class)
    fun register(login:Intent) {
        val MEDIA_TYPE = MediaType.parse("application/json")
        val url = "http://192.168.1.107:3000/users/signup"
        val client = OkHttpClient()
        val postdata = JSONObject()
        try {
            postdata.put("username", username.text)
            postdata.put("password", password.text)
            postdata.put("email", email.text)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        val body = RequestBody.create(MEDIA_TYPE, postdata.toString())
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                val mMessage = response.body().toString();
                Log.e("Tag", mMessage)
                startActivity(login);
            }
        })
    }
}
