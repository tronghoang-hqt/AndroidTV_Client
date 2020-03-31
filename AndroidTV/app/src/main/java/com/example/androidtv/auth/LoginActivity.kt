package com.example.androidtv.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.androidtv.R
import com.example.androidtv.app.media.VideoConsumptionExampleFragment.TAG
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login.setOnClickListener{


        }
    }

    @Throws(IOException::class)
    fun postRequest() {
        val MEDIA_TYPE = MediaType.parse("application/json")
        val url = "http://118.71.224.167:3000/users/login"
        val client = OkHttpClient()
        val postdata = JSONObject()
        try {
            postdata.put("username", "aneh")
            postdata.put("password", "12345")
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
                Log.e(TAG, mMessage)
            }
        })
    }
}

