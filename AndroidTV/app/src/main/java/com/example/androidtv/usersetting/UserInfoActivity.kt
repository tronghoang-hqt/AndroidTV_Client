package com.example.androidtv.usersetting

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import com.example.androidtv.MainActivity
import com.example.androidtv.R
import com.example.androidtv.models.User
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_user_infor.*
import java.io.InputStream
import java.net.URL


class UserInfoActivity : Activity() {

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
            println(results1[0]!!.avatar)
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

        }
        btn_change_avatar.setOnClickListener {
            
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

}
