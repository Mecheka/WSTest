package com.example.wstest.screen.auth

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import com.example.wstest.R
import com.example.wstest.databinding.DialogProfileBinding
import com.example.wstest.databinding.FragmentLoginBinding
import com.example.wstest.screen.auth.model.UserAndImage
import com.example.wstest.screen.base.BaseFragment
import com.example.wstest.screen.map.MainActivity

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { act ->
            viewModel.loginSuccessLiveData.observe(act) {
                if (!it) {
                    loginFailDialog()
                }
            }

            viewModel.userAndImageLiveData.observe(act) {
                profileDialog(it)
            }
        }

        binding.buttonRegister.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.contentContainer, RegisterFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        binding.buttonLogin.setOnClickListener {
            if (!validate()) return@setOnClickListener

            viewModel.login(
                binding.editUsername.text.toString(),
                binding.editPassword.text.toString()
            )
        }
    }

    private fun validate(): Boolean {
        if (binding.editUsername.text.isEmpty()) {
            binding.editUsername.requestFocus()
            binding.editUsername.error = "Empty"
            return false
        }

        if (binding.editPassword.text.isEmpty()) {
            binding.editPassword.requestFocus()
            binding.editPassword.error = "Empty"
            return false
        }
        return true
    }

    private fun loginFailDialog() {
        activity?.let {
            val option = arrayOf<CharSequence>("สมัครสมาชิก", "ติดต่อผู้ดูแลระบบ")
            val builder = AlertDialog.Builder(it)
            with(builder) {
                setTitle("ไม่พบข้อมูลผู้ใช้ในระบบกรุณาสมัครสมาชิก และ ติดต่อผู้ดูแลระบบ")
                setItems(option) { dialog, which ->
                    dialog.dismiss()
                    if (option[which] == "สมัครสมาชิก") {
                        it.supportFragmentManager.commit {
                            replace(R.id.contentContainer, RegisterFragment())
                            setReorderingAllowed(true)
                            addToBackStack(null)
                        }
                    } else {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1176"))
                        startActivity(intent)
                    }


                }
                show()
            }
        }
    }

    private fun profileDialog(userAndImage: UserAndImage) {
        activity?.let {
            val dialogView = DialogProfileBinding.inflate(layoutInflater, null, false)
            val builder = AlertDialog.Builder(it)
            with(builder) {
                setView(dialogView.root)
                val b = Base64.decode(userAndImage.userImageEntity.image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                val dialog=create()
                dialogView.imageProfile.setImageBitmap(bitmap)
                dialogView.textUserName.text = userAndImage.userEntity.userName
                dialogView.textName.text = userAndImage.userEntity.name
                dialogView.textLastName.text = userAndImage.userEntity.lastName
                dialogView.textCardNo.text = userAndImage.userEntity.cardNo
                dialogView.textPhone.text = userAndImage.userEntity.phone

                dialogView.buttonToMain.setOnClickListener { _ ->
                    dialog.dismiss()
                    val intent = Intent(it, MainActivity::class.java)
                    startActivity(intent)
                }

                show()
            }
        }
    }
}