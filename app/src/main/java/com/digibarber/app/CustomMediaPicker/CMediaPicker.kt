package com.digibarber.app.CustomMediaPicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digibarber.app.R


class CMediaPicker : AppCompatActivity() {

    lateinit var cancelBtn: TextView
    lateinit var nextBtn: TextView
    lateinit var librayName: TextView
    lateinit var changeLib: LinearLayout
    lateinit var topImage: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var albumList: RecyclerView
    lateinit var albumView: RelativeLayout
    lateinit var libBtn: Button
    lateinit var photoBtn: Button

    val GET_STORAGE_REQ_CODE = 12

    var allAlbums = ArrayList<Album>()
    var currentIndex = 2

    var maxImages = 5
    var selectedImages = 0

    var selectedPhotos = ArrayList<String>()

    lateinit var photosAdapter: PhotosAdapter
    lateinit var albumAdapter: AlbumAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cmediapicker)

        cancelBtn = findViewById(R.id.cancelBtn)
        nextBtn = findViewById(R.id.nextBtn)
        librayName = findViewById(R.id.albumName)
        changeLib = findViewById(R.id.changeLib)
        topImage = findViewById(R.id.topImage)
        recyclerView = findViewById(R.id.recycler)
        libBtn = findViewById(R.id.libBtn)
        photoBtn = findViewById(R.id.photoBtn)
        albumList = findViewById(R.id.albumList)
        albumView = findViewById(R.id.albumView)

        albumView.visibility = View.INVISIBLE

        maxImages = intent.getIntExtra("max", 1)

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        GET_STORAGE_REQ_CODE)
            } else getAllbumns()
        } else getAllbumns()
        changeLib.setOnClickListener {
            if (albumView.visibility == View.INVISIBLE) {
                albumView.visibility = View.VISIBLE
            } else {
                albumView.visibility = View.INVISIBLE
            }
        }
        topImage.setOnClickListener {
            albumView.visibility = View.INVISIBLE
        }
        cancelBtn.setOnClickListener {
            finish()
        }
        nextBtn.setOnClickListener {
            if (selectedPhotos.size > 0) {
                val returnIntent = Intent()
                returnIntent.putExtra("photos", selectedPhotos)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            GET_STORAGE_REQ_CODE -> {
                if (
                        grantResults.isNotEmpty() &&
                        permissions.isNotEmpty() &&
                        permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    getAllbumns()
                } else finish()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun setupView() {
        photosAdapter = PhotosAdapter(this)
        recyclerView.layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
        recyclerView.adapter = photosAdapter
        albumAdapter = AlbumAdapter(this)
        albumList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        albumList.adapter = albumAdapter
    }

    private fun getAllbumns() {

        allAlbums.clear()

        val cursor: Cursor?
        val column_index_data: Int
        val column_index_folder_name: Int
        var absolutePathOfImage: String
        var int_position = 0
        var boolean_folder = false

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        cursor = applicationContext.contentResolver.query(uri, projection, null, null, "$orderBy DESC")
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {

            absolutePathOfImage = cursor.getString(column_index_data)

            for (i in allAlbums.indices) {
                if (allAlbums.get(i).name.equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true
                    int_position = i
                    break
                } else {
                    boolean_folder = false
                }
            }
            if (boolean_folder) {
                val al_path = java.util.ArrayList<String>()
                al_path.addAll(allAlbums.get(int_position).images)
                al_path.add("file://" + absolutePathOfImage)
                allAlbums.get(int_position).images = al_path
            } else {
                val al_path = java.util.ArrayList<String>()
                al_path.add("file://" + absolutePathOfImage)
                val obj_model = Album()
                obj_model.name = cursor.getString(column_index_folder_name)
                obj_model.images = al_path
                allAlbums.add(obj_model)
            }
        }
        val emptyList: ArrayList<Int> = ArrayList()
        for (i in allAlbums.indices) {
            if (allAlbums.get(i).images.isEmpty()) {
                emptyList.add(i)
            }
        }
        for (i in emptyList.indices) {
            allAlbums.removeAt(i)
        }
        setupView()
    }
}

class Album {

    var name: String = ""
    var images = ArrayList<String>()
    var selected = ArrayList<String>()
}