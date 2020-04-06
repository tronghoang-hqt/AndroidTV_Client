package com.example.androidtv.usersetting

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.androidtv.ButtonFocusListener
import com.example.androidtv.MainActivity
import com.example.androidtv.R
import com.example.androidtv.models.User
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_user_infor.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.regex.Pattern


class UserInfoActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_infor)
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(Array<String>(1){android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }
        val passwordPattern =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$".toRegex();
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
            if(LoadImageFromWebOperations(
                    results1[0]!!.avatar
                )!=null){
            avatar_user.setImageDrawable(
                LoadImageFromWebOperations(
                    results1[0]!!.avatar
                )
            )}
        }
        val focusListener = ButtonFocusListener();
        btn_change_avatar.setOnFocusChangeListener(focusListener);
        btn_cls_edit_password.setOnFocusChangeListener(focusListener);
        btn_edit_password.setOnFocusChangeListener(focusListener);
        save_password_btn.setOnFocusChangeListener(focusListener);
        close.setOnFocusChangeListener(focusListener)
        btn_change_avatar.setAllCaps(false);
        btn_edit_password.setOnClickListener{
            new_password.setVisibility(View.VISIBLE);
            confirm_password.setVisibility(View.VISIBLE);
            current_password.setVisibility(View.VISIBLE);
            save_password_btn.setVisibility(View.VISIBLE);
            btn_cls_edit_password.setVisibility(View.VISIBLE);
        }
        btn_cls_edit_password.setOnClickListener{
            val imm: InputMethodManager =
                this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus?.getWindowToken(), 0);
            new_password.setVisibility(View.INVISIBLE);
            confirm_password.setVisibility(View.INVISIBLE);
            current_password.setVisibility(View.INVISIBLE);
            save_password_btn.setVisibility(View.INVISIBLE);
            btn_cls_edit_password.setVisibility(View.INVISIBLE);
        }
        save_password_btn.setOnClickListener{
            if(!new_password.getText().matches(passwordPattern)){
                new_password.requestFocus();
                new_password.setError("Password is minimum 8 characters, at least one letter, one number and one special character")
            }else if (new_password.text.toString()!=confirm_password.text.toString()){
                confirm_password.requestFocus();
                confirm_password.setError("Confirm password isn't same new password");
            }else {
                changepassword();
            }
        }
        btn_change_avatar.setOnClickListener {
            MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1000)
                .withFilter(Pattern.compile(".*\\.(png|jpg|gif|bmp)"))
                .withHiddenFiles(true) // Show hidden files and folders
                .start()
        }
        close.setOnClickListener {
            val intentMain=Intent(this,MainActivity::class.java);
            startActivity(intentMain);
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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            val filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            uploadImg(filePath);
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
                    runOnUiThread(Thread(Runnable {
                        val text = "Change password succesfully!"
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show();
                    }))
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                else {

                    runOnUiThread(Thread(Runnable {
                        current_password.requestFocus();
                        notification.setText("Current password isn't correct")
                    }))
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1001){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @Throws(IOException::class)
    fun uploadImg(filePath:String) {
        val  MEDIA_TYPE_PNG = MediaType.parse("image/*");
        val filename: String = filePath.substring(filePath.lastIndexOf("/") + 1)
        val file=File(filePath);
        var token:String="";
        val realm = Realm.getDefaultInstance()
        val results1 =
            realm.where(
                User::class.java
            ).findAll()
        if (results1.size != 0) {
            token=results1[0]!!.token
        }
        val url = "http://192.168.1.107:3000/users/avatar"
        val client = OkHttpClient()

        val req: RequestBody =
            MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("avatar", filename, RequestBody.create(MEDIA_TYPE_PNG, file)).build()
        val request: Request = Request.Builder()
            .url(url)
            .post(req)
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
                val avatar_url=JSONObject(mMessage).getString("avatar_url");
                val realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                val results1 =
                    realm.where(
                        User::class.java
                    ).findAll();
                results1[0]?.setAvatar(avatar_url);
                realm.commitTransaction();
                runOnUiThread(Thread(Runnable {
                    val text = "Change avatar succesfully!"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show();
                }))
                val intent = intent
                finish()
                startActivity(intent)
            }
        })
    }

}
