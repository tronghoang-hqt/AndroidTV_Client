package com.example.androidtv.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.androidtv.MainActivity
import com.example.androidtv.R
import com.example.androidtv.models.User
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Realm.init(this);
        val intentRegister = Intent(this, RegisterActivity::class.java);
        val intentMain = Intent(this, MainActivity::class.java);
        login.setOnClickListener {
            val passwordPattern =
                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$".toRegex();
            val usernamePattern =
                "^[a-zA-Z0-9]+([a-zA-Z0-9](_|-| )[a-zA-Z0-9])*[a-zA-Z0-9]*$".toRegex();
            if (username.text.length == 0) {
                username.requestFocus();
                username.setError("User name is required")
            } else
                if (password.text.length == 0) {
                    password.requestFocus();
                    password.setError("Password is required")
                } else
                    if (!password.text.matches(passwordPattern)) {
                        password.requestFocus();
                        password.setError("Password is minimum 8 characters, at least one letter, one number and one special character:")
                    } else
                        if (!username.text.matches(usernamePattern)) {
                            username.requestFocus();
                            username.setError("User name doesn't have space and special characters ")
                        } else {
                            login(intentMain);
                        }
        }
        register.setOnClickListener {
            startActivity(intentRegister);
        }
    }

    @Throws(IOException::class)
    fun login(main: Intent) {
        val MEDIA_TYPE = MediaType.parse("application/json")
        val url = "http://192.168.1.107:3000/users/login"
        val client = OkHttpClient()
        val postdata = JSONObject()
        try {
            postdata.put("username", username.text.toString())
            postdata.put("password", password.text.toString())
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
                //call.cancel();
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                val mMessage = response.body()!!.string()
                if(JSONObject(mMessage).getString("login")=="true"){
                    val realm = Realm.getDefaultInstance();
                    realm.executeTransaction { realm ->
                        realm.deleteAll()
                    }
                    val user = User();
                    user.setUserId(JSONObject(mMessage).getString("id"));
                    user.setUsername(JSONObject(mMessage).getString("username"));
                    user.setEmail(JSONObject(mMessage).getString("email"));
                    user.setToken(JSONObject(mMessage).getString("token"));
                    user.setAvatar(JSONObject(mMessage).getString("avatar_url"));
                    user.setRefreshToken(JSONObject(mMessage).getString("refreshToken"));
                    realm.beginTransaction();
                    val copyUser: User = realm.copyToRealm(user);
                    realm.commitTransaction();
//                    val results1: RealmResults<User> =
//                        realm.where(User::class.java).findAll()
//
//                    for (c in results1) {
//                        Log.d("username", c.getUsername())
//                    }
//                    startActivity(main);
                }
                else {
                    notification.setText("Username or password isn't correct")
                }
            }
        })
    }
}

