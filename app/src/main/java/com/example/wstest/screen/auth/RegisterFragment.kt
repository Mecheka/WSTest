package com.example.wstest.screen.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import com.example.wstest.database.entiry.UserEntity
import com.example.wstest.database.entiry.UserImageEntity
import com.example.wstest.databinding.FragmentRegisterBinding
import com.example.wstest.screen.base.BaseFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream

class RegisterFragment : BaseFragment() {

    companion object {
        const val CAMERA_CODE = 1001
        const val GALLEY_CODE = 1002
    }

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { act ->

            viewModel.registerSuccessLiveData.observe(act) {
                successDialog(it) {
                    act.supportFragmentManager.popBackStack()
                }
            }
        }

        binding.imageProfile.setOnClickListener {
            selectImage()
        }

        binding.buttonSave.setOnClickListener {

            if (!validate()) return@setOnClickListener

            val userEntity = UserEntity(
                binding.editUserName.text.toString(),
                binding.editPassword.text.toString(),
                binding.editName.text.toString(),
                binding.editLastName.text.toString(),
                binding.editCardNo.text.toString(),
                binding.editPhone.text.toString()
            )

            val userImage = convertImageToString()
            val userImageEntity =
                UserImageEntity(userName = binding.editUserName.text.toString(), image = userImage)

            viewModel.register(userEntity, userImageEntity)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                binding.imageProfile.setImageBitmap(data?.extras?.get("data") as Bitmap)
            } else {
                binding.imageProfile.setImageURI(data?.data)
            }
        }
    }

    private fun selectImage() {
        context?.let {
            val option = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(it)
            with(builder) {
                setTitle("Choose your profile picture")
                setItems(option) { dialog, which ->
                    when {
                        option[which] == "Take Photo" -> {
                            Dexter.withContext(it).withPermission(Manifest.permission.CAMERA)
                                .withListener(object : PermissionListener {
                                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        startActivityForResult(intent, CAMERA_CODE)
                                    }

                                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                                        Log.DEBUG
                                    }

                                    override fun onPermissionRationaleShouldBeShown(
                                        p0: PermissionRequest?,
                                        p1: PermissionToken?
                                    ) {
                                        p1?.continuePermissionRequest()
                                    }
                                }).check()
                        }
                        option[which] == "Choose from Gallery" -> {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            startActivityForResult(intent, GALLEY_CODE)
                        }
                        else -> {
                            dialog.dismiss()
                        }
                    }
                }

                show()
            }
        }

    }

    private fun convertImageToString(): String {
        val boas = ByteArrayOutputStream()
        binding.imageProfile.drawToBitmap().compress(Bitmap.CompressFormat.PNG, 100, boas)
        val b = boas.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun validate(): Boolean {
        if (binding.editUserName.text.isEmpty()) {
            binding.editUserName.requestFocus()
            binding.editUserName.error = "Empty"
            return false
        }

        if (binding.editPassword.text.isEmpty()) {
            binding.editPassword.requestFocus()
            binding.editPassword.error = "Empty"
            return false
        }

        if (binding.editName.text.isEmpty()) {
            binding.editName.requestFocus()
            binding.editName.error = "Empty"
            return false
        }

        if (binding.editLastName.text.isEmpty()) {
            binding.editLastName.requestFocus()
            binding.editLastName.error = "Empty"
            return false
        }

        if (binding.editCardNo.text.isEmpty()) {
            binding.editCardNo.requestFocus()
            binding.editCardNo.error = "Empty"
            return false
        }

        if (binding.editPhone.text.isEmpty()) {
            binding.editPhone.requestFocus()
            binding.editPhone.error = "Empty"
            return false
        }

        return true
    }
}