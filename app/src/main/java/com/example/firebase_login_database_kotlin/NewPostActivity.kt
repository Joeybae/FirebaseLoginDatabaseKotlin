package com.example.firebase_login_database_kotlin

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.firebase_login_database_kotlin.users.Post
import com.example.firebase_login_database_kotlin.users.User
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_new_post.*
import java.util.*

open class NewPostActivity : BaseActivity() {


    //image upload
    private val GALLERY_REQUEST = 999
    private var mImageUri: Uri? = null
    private var mSelectImage: ImageButton? = null
    private var mStorageRef: StorageReference? = null

    //button
    private var mSubmitButton: FloatingActionButton? = null

    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        // [START initialize_database_ref]
        database = FirebaseDatabase.getInstance().reference
        // [END initialize_database_ref]

        //Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //need for customization
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true) // make back button

        //calendar
        val mDateField = findViewById<TextView>(R.id.fieldDate)
        val selectDate = findViewById<Button>(R.id.btnDate)
        selectDate.setOnClickListener {
            var calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            var datePickerDialog = DatePickerDialog(this@NewPostActivity, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                //                                date.setText(day + "/" + (month + 1) + "/" + year);
                mDateField.text = year.toString() + "/" + (month + 1) + "/" + day
            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }

        //image upload
        mSubmitButton = findViewById<FloatingActionButton>(R.id.fabSubmitPost)
        bindViews()
        clickEvents()
    }

    //Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitPost() {
        val title = fieldTitle.text.toString()
        val date = fieldDate.text.toString()
        val body = fieldBody.text.toString()


        // Title is required
        if (TextUtils.isEmpty(title)) {
            fieldTitle.error = REQUIRED
            return
        }

        // date is required
        if (TextUtils.isEmpty(date)) {
            fieldDate.error = REQUIRED
            return
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            fieldBody.error = REQUIRED
            return
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false)
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show()

        // [START single_value_read]
        val filepath = mStorageRef!!.child("photos").child(Objects.requireNonNull(mImageUri!!.lastPathSegment))
        mImageUri?.let {
            filepath.putFile(it).addOnSuccessListener {
                val downloadUrl = filepath.downloadUrl
                downloadUrl.addOnCompleteListener { task ->
                    val img_url = task.result!!.toString()
                    writeNewPost(title, date, body, img_url)
                }
            }
        }
        // [END single_value_read]
    }

    private fun setEditingEnabled(enabled: Boolean) {
        fieldTitle.isEnabled = enabled
        fieldBody.isEnabled = enabled
        fieldDate.isEnabled = enabled
        if (enabled) {
            fabSubmitPost.show()
        } else {
            fabSubmitPost.hide()
        }
    }

    // [START write_fan_out]
    private fun writeNewPost(title: String,  date: String, body: String, IMAGE: String) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val newPost = database.child("posts").push()//cret uniquid
        val postId = newPost.getKey()
        database.child("posts").child(postId!!).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val userId = uid
                    database.child("users").child(userId).addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Get user value
                                val user = dataSnapshot.getValue(User::class.java)

                                val post = Post(userId, user!!.username.toString(), title, date, body, IMAGE)
                                val postValues = post.toMap()

                                val childUpdates = HashMap<String, Any>()
                                childUpdates["/posts/" + postId!!] = postValues
                                childUpdates["/user-posts/$userId/$postId"] = postValues

                                database.updateChildren(childUpdates)

                                // Finish this Activity, back to the stream
                                setEditingEnabled(true)
                                finish()
                                // [END_EXCLUDE]
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                                // [START_EXCLUDE]
                                setEditingEnabled(true)
                                // [END_EXCLUDE]
                            }
                        })

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                    // [START_EXCLUDE]
                    setEditingEnabled(true)
                    // [END_EXCLUDE]
                }
            }
        )
    }
    // [END write_fan_out]

    companion object {

        private const val TAG = "NewPostActivity"
        private const val REQUIRED = "Required"
    }

    // [Start upload image]
    private fun bindViews() {
        mStorageRef = FirebaseStorage.getInstance().reference
        mSelectImage = findViewById<ImageButton>(R.id.uploadimg)
    }

    private fun clickEvents() {
        mSelectImage!!.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_REQUEST)
        }
        mSubmitButton!!.setOnClickListener { submitPost() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data!!.data
            mSelectImage!!.setImageURI(mImageUri)
        }
    }
}
