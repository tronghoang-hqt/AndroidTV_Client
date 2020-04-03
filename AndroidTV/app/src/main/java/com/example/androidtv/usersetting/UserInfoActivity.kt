package com.example.androidtv.usersetting

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import com.example.androidtv.MainActivity
import com.example.androidtv.R
import com.example.androidtv.models.User
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_user_infor.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL


class UserInfoActivity : Activity() {
    val PICK_IMAGE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_infor)
        Realm.init(this)
        val realm = Realm.getDefaultInstance()
        val results1 =
            realm.where(
                User::class.java
            ).findAll()
        if (results1.size != 0) {
            txtUsername.setText(results1[0]!!.username)
            txtEmail.setText(results1[0]!!.email)
            println(results1[0]!!.token)
            avatar_user.setImageDrawable(
                MainActivity.LoadImageFromWebOperations(
                    results1[0]!!.avatar
                )
            )
        }
        btn_change_avatar.setAllCaps(false);
        btn_edit_password.setOnClickListener{
            new_password.setVisibility(View.VISIBLE);
            confirm_password.setVisibility(View.VISIBLE);
            current_password.setVisibility(View.VISIBLE);
            save_password_btn.setVisibility(View.VISIBLE);
            btn_cls_edit_password.setVisibility(View.VISIBLE);
        }
        btn_cls_edit_password.setOnClickListener{
            new_password.setVisibility(View.INVISIBLE);
            confirm_password.setVisibility(View.INVISIBLE);
            current_password.setVisibility(View.INVISIBLE);
            save_password_btn.setVisibility(View.INVISIBLE);
            btn_cls_edit_password.setVisibility(View.INVISIBLE);
        }
        save_password_btn.setOnClickListener{
            changepassword();
        }
        btn_change_avatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }
    }
    fun LoadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val `is` = URL(url).content as InputStream
            Drawable.createFromStream(`is`, "avatar_img")
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
        }
    }

    @Throws(IOException::class)
    fun changepassword() {
        var token:String="";
        val realm = Realm.getDefaultInstance()
        val results1 =
            realm.where(
                User::class.java
            ).findAll()
        if (results1.size != 0) {
            token=results1[0]!!.token
            println(token)
        }
        val MEDIA_TYPE = MediaType.parse("application/json")
        val url = "http://192.168.1.107:3000/users/changepassword"
        val client = OkHttpClient()
        val postdata = JSONObject()
        try {
            postdata.put("newpassword",new_password.getText())
            postdata.put("currentpassword", current_password.getText())
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        val body = RequestBody.create(MEDIA_TYPE, postdata.toString())
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json").header("token",token)
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
                Log.d("data",mMessage);
                if(JSONObject(mMessage).getString("changePassword")=="true"){
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                else {
                    current_password.requestFocus();
                    notification.setText("Current password isn't correct")
                }
            }
        })
    }
}
