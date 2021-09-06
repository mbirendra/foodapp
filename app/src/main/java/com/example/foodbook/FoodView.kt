package com.example.foodbook

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.entity.Food
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

class FoodView : AppCompatActivity(),View.OnClickListener {
    private lateinit var FoodImage : ImageView
    private lateinit var FoodTitle : TextView
    private lateinit var FoodCategory : TextView
    private lateinit var PreparationTime : TextView
    private lateinit var FoodDescription : TextView
    private lateinit var Updatebtn : Button
    private lateinit var Deletebtn : Button
    private lateinit var FoodInfo : Food
    private lateinit var etTitle:EditText
    private lateinit var btnChange : Button
    private lateinit var etDesc:EditText
    private lateinit var rgCategory:RadioGroup
    private lateinit var etTime:EditText
    private lateinit var btnCancel : Button

    var image_url = ""
    var CAMERA_REQUEST_CODE = 1
    var GALLERY_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_view2)

        binding()

        FoodInfo = intent.getParcelableExtra("Food")!!

        addValues()

        Deletebtn.setOnClickListener(this)
        Updatebtn.setOnClickListener(this)

        if(ServiceBuilder.toggle == true)
        {
            Updatebtn.visibility = View.VISIBLE
            Deletebtn.visibility = View.VISIBLE
        }
        else
        {

                Updatebtn.visibility = View.GONE
                Deletebtn.visibility = View.GONE

        }

        btnChange.setOnClickListener {
            uploadImage(FoodInfo._id)
        }

    }

    private fun addValues()
    {
        FoodTitle.text = FoodInfo.Foodtitle
        FoodCategory.text = FoodInfo.foodcategory
        FoodDescription.text = FoodInfo.Fooddesc
        PreparationTime.text = FoodInfo.preptime

        FoodImage.setOnClickListener {
            showPopUp()
        }

        var imgPath = ServiceBuilder.loadImagePath() + FoodInfo.foodimg!!.replace("\\","/")
        Glide.with(this).load(imgPath).into(FoodImage)
    }

    private fun binding() {
        FoodImage = findViewById(R.id.imgView)
        FoodTitle = findViewById(R.id.titleView)
        FoodCategory= findViewById(R.id.categoryView)
        PreparationTime= findViewById(R.id.timeView)
        FoodDescription= findViewById(R.id.descView)
        Updatebtn = findViewById(R.id.Updatebtn)
        Deletebtn = findViewById(R.id.Deletebtn)
        btnChange = findViewById(R.id.btnChange)
    }

    private fun showPopUp()
    {
        var popUp = PopupMenu(this,FoodImage)
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

                            Toast.makeText(this@FoodView, "Food Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                catch (ex:Exception)
                {
                    println(ex.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(this@FoodView, "Error while adding Food", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else
        {
            Toast.makeText(this@FoodView, "Please add image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etTitle.text))
        {
            etTitle.error = "Insert Food Title"
            etTitle.requestFocus()
            return false
        }

        else if(TextUtils.isEmpty(etDesc.text))
        {
            etDesc.error = "Insert Food Description"
            etDesc.requestFocus()
            return false
        }

        else if(TextUtils.isEmpty(etTime.text))
        {
            etTime.error = "Insert Preparation Time"
            etTime.requestFocus()
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
                val contentResolver = contentResolver
//locator and identifier
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()//moveTONext // movetolast
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                image_url = cursor.getString(columnIndex)
                //image preview
                FoodImage.setImageBitmap(BitmapFactory.decodeFile(image_url))
                cursor.close()
            }else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                image_url = file!!.absolutePath
                FoodImage.setImageBitmap(BitmapFactory.decodeFile(image_url))
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
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
            R.id.Updatebtn->{
                        var dialog = Dialog(this)
        dialog.setContentView(R.layout.update_food)//make this and insert from layout
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

         etTitle = dialog.findViewById(R.id.etTitle)

         etDesc= dialog.findViewById(R.id.etDesc)
         rgCategory = dialog.findViewById(R.id.rgCategory)
         etTime= dialog.findViewById(R.id.etTime)
                btnCancel = dialog.findViewById(R.id.btnCancel)
         var btnUpdate:Button = dialog.findViewById(R.id.btnUpdate)
         var foodCategory = FoodInfo.foodcategory

                var imgPath = ServiceBuilder.loadImagePath() + FoodInfo.foodimg!!.replace("\\","/")
                Glide.with(this).load(imgPath).into(FoodImage)

         etTitle.setText(FoodInfo.Foodtitle)
         etDesc.setText(FoodInfo.Fooddesc)
         etTime.setText(FoodInfo.preptime)

         btnUpdate.setOnClickListener {
             if(validation())
             {
                 CoroutineScope(Dispatchers.IO).launch {
                     try {
                         val repo = FoodRepository()
                         val response = repo.updateFoodDetails(etTitle.text.toString(),etDesc.text.toString(),foodCategory!!,etTime.text.toString(),FoodInfo._id)
                         if(response.success == true)
                         {
                             withContext(Dispatchers.Main)
                             {
                                 etTime.snackbar("Details Updated")
                                 FoodTitle.text = etTitle.text.toString()
                                 FoodDescription.text = etDesc.text.toString()
                                 PreparationTime.text = etTime.text.toString()
                                 FoodCategory.text = foodCategory
                                 FoodInfo.foodcategory = foodCategory
                                 dialog.dismiss()
                             }
                         }
                         else
                         {
                             withContext(Dispatchers.Main)
                             {
                                 etTime.snackbar("There is error")
                             }
                         }
                     }
                     catch (ex:Exception)
                     {

                         withContext(Dispatchers.Main)
                         {
                            etTime.snackbar("${ex.toString()}")
                         }
                     }
                 }
             }
         }

         FoodImage.setOnClickListener {
             showPopUp()
         }


         rgCategory.setOnCheckedChangeListener { group, checkedId ->
             when(checkedId)
             {
                 R.id.rbnonveg ->{
                     foodCategory = "Non-Veg"

                 }

                 R.id.rbveg ->{
                     foodCategory = "Veg"

                 }
             }
         }

         when(FoodInfo.foodcategory)
         {
             "Veg"->{
                 rgCategory.check(R.id.rbveg)
             }
             "Non-Veg"->{
                 rgCategory.check(R.id.rbnonveg)
             }
         }

         btnCancel.setOnClickListener {
             dialog.dismiss()
         }

        dialog.show()

            }

            R.id.Deletebtn -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Do you really want to delete your Food?")
                alert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = FoodRepository()
                            val response = repo.deleteFood(FoodInfo._id)
                            if (response.success == true) {
                                withContext(Dispatchers.Main)
                                {
                                    Toast.makeText(this@FoodView, "${response.message}", Toast.LENGTH_SHORT).show()
                                    var intent = Intent(this@FoodView,DashboardActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        } catch (ex: Exception) {
                            withContext(Dispatchers.Main)
                            {
                                Toast.makeText(this@FoodView,
                                    "${ex.toString()}",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                alert.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->

                }

                val alertt = alert.create()
                alertt.setCancelable(false)
                alertt.show()

            }
        }
    }
}