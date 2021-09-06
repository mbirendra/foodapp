package com.example.foodbook.ui.saved

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.foodbook.R
import com.example.foodbook.notification.NotificationSender
import com.example.foodbook.repository.FoodRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
class SaveddFragment : Fragment(),View.OnClickListener {

    private lateinit var etFoodTitle : EditText
    private lateinit var FoodImg : ImageView
    private lateinit var etFoodDesc : EditText
    private lateinit var etPrepTime : EditText
    private lateinit var rgFoodCategory : RadioGroup
    private lateinit var btnAdd : Button
    var category = "Veg"
    private lateinit var constraintLayout: ConstraintLayout
    var GALLERY_REQUEST_CODE = 1
    var CAMERA_REQUEST_CODE = 0
    var image_url = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_savedd, container, false)

        etFoodTitle = root.findViewById(R.id.etFoodTitle)
        FoodImg = root.findViewById(R.id.FoodImg)
        etFoodDesc = root.findViewById(R.id.etFoodDesc)
        rgFoodCategory = root.findViewById(R.id.rgFoodCategory)
        etPrepTime = root.findViewById(R.id.etPrepTime)
        btnAdd = root.findViewById(R.id.btnAdd)

        radioGroupListener()

        btnAdd.setOnClickListener{
            if(validation())
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = FoodRepository()
                        val response = repo.insertFood(etFoodTitle.text.toString(),etFoodDesc.text.toString(),category,"__",etPrepTime.text.toString())
                         println(response)
                        if(response.success == true)
                        {
                                uploadImage(response.saved!!._id)
                            NotificationSender(requireContext(),"New Food inserted","").createHighPriority()

                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                Toast.makeText(requireContext(), "Error while adding Food1", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    catch (ex:Exception)
                    {
                        println(ex.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            Toast.makeText(requireContext(), "Error while adding Food 2", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }


        FoodImg.setOnClickListener{
            showPopUp()
        }

        return root
    }


    private fun radioGroupListener() {

            rgFoodCategory.setOnCheckedChangeListener { group, checkId ->
                when (checkId) {
                    R.id.rbVeg -> {
                        category = "Veg"
                    }
                    R.id.rbNonVeg -> {
                        category = "Non-veg"
                    }

                }
            }
        }

    private fun showPopUp()
    {
        var popUp = PopupMenu(requireContext(),FoodImg)
        popUp.menuInflater.inflate(R.menu.camera_gallery,popUp.menu)
        popUp.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.camera ->{
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent,CAMERA_REQUEST_CODE)
                }

                R.id.gallery->{
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent,GALLERY_REQUEST_CODE)
                }
            }
            true
        }

        popUp.show()
    }

    private fun uploadImage(id:String)
    {
        if(image_url != "")
        {
            println(image_url)
            var file = File(image_url)
            var extension = MimeTypeMap.getFileExtensionFromUrl(image_url)
            var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            val req_body = RequestBody.create(MediaType.parse(mimeType),file)
            val body = MultipartBody.Part.createFormData("Foodimg",file.name,req_body)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = FoodRepository()
                    val response = repo.uploadFood(id,body)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            etFoodTitle.text.clear()
                            etFoodDesc.text.clear()
                            category = "Veg"
                            etPrepTime.text.clear()
                            Toast.makeText(requireContext(), "Food Added", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                catch (ex:Exception)
                {
                    println(ex.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(), "Error while adding Food", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else
        {
            Toast.makeText(requireContext(), "Please add image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etFoodTitle.text))
        {
            etFoodTitle.error = "Insert Food Title"
            etFoodTitle.requestFocus()
            return false
        }

        else if(TextUtils.isEmpty(etFoodDesc.text))
        {
            etFoodDesc.error = "Insert Food Description"
            etFoodDesc.requestFocus()
            return false
        }

        else if(TextUtils.isEmpty(etPrepTime.text))
        {
            etPrepTime.error = "Insert Preparation Time"
            etPrepTime.requestFocus()
            return false
        }

        else
        {
            return true
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                //overall location of selected image
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = requireActivity().contentResolver
//locator and identifier
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()//moveTONext // movetolast
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                image_url = cursor.getString(columnIndex)
                //image preview
                FoodImg.setImageBitmap(BitmapFactory.decodeFile(image_url))
                cursor.close()
            }else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
            val imageBitmap = data.extras?.get("data") as Bitmap
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
            image_url = file!!.absolutePath
            FoodImg.setImageBitmap(BitmapFactory.decodeFile(image_url))
        }
        }
    }

    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {

        var file: File? = null
        return try {

            file = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        }catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }





    override fun onClick(v: View?) {
       when(v!!.id)
       {
           R.id.FoodImage -> {
               println("Hellon")
               showPopUp()
           }
       }
    }


}