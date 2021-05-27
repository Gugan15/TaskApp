package com.example.taskapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.taskapp.databinding.YoutubePlayerBinding
import java.io.*
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class EncryptFragment: Fragment() {
    private lateinit var binding:YoutubePlayerBinding
    private val permissionCode=654
    private var rsaEncrypt:String?=null
    private lateinit var keyStore:KeyStore
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding=YoutubePlayerBinding.inflate(LayoutInflater.from(context), null, false)

        binding.floatingActionButton.setOnClickListener{
            permissionCheck()
        }
        keyStore= KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        binding.deleteFile.setOnClickListener{
            deleteFile()
        }
        binding.decryptButton.setOnClickListener{
            val pref=context?.getSharedPreferences("crypt_keys", Context.MODE_PRIVATE)
            val file=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), binding.fileNameEdit.text.toString() + ".txt")
            if(file.exists()) {
                val stringBuilder = StringBuilder()
                var string: String? = null
                val bufferedReader = BufferedReader(FileReader(file))
                while (true) {
                    try {
                        if (bufferedReader.readLine().also { string = it } == null) break
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    stringBuilder.append(string)
                }
                bufferedReader.close()
                val secret= pref?.getString(binding.fileNameEdit.text.toString()+"_aes","")?.let { it1 -> rsaDecrypt(it1) }
                val secretKey: SecretKeySpec? = secret?.size?.let { it1 -> SecretKeySpec(secret,0, it1,"AES") }
                val raw = secretKey?.encoded
                val secretKeySpec = SecretKeySpec(raw, "AES")
                val result= decryptMsg(stringBuilder.toString(), secretKeySpec)
                binding.decryptText.text = result
            }else
            {
                Toast.makeText(context, "File Not exists", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun deleteFile() {
        val file=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), binding.fileNameEdit.text.toString() + ".txt")
        if(file.exists()) {
            file.delete()
            deleteKeys()
        }
    }

    private fun deleteKeys() {
        keyStore.deleteEntry("aliasText.getText().toString()")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permissionCheck() {
        if (context?.applicationContext?.let {
                    ActivityCompat.checkSelfPermission(
                            it, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } !=
                PackageManager.PERMISSION_GRANTED && context?.let {
                    ActivityCompat.checkSelfPermission(
                            it, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), permissionCode)
            return
        } else {
            onSaveClick()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun onSaveClick() {
        createRSAKeyPair()
        if(binding.fileNameEdit.text.isNotEmpty()&&binding.valuesEdit.text.isNotEmpty()){
            if(Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()){
                return
            }
            val file=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), binding.fileNameEdit.text.toString() + ".txt")
            val outputStream: FileOutputStream?
            try {
                file.createNewFile()
                outputStream= FileOutputStream(file, true)
                rsaEncrypt= rsaEncrypt()
                val aesSecretKey=generateKey("abcdefghijklmnop")
                val encrypt=encryptMsg(binding.valuesEdit.text.toString(), aesSecretKey)

                outputStream.write(encrypt?.toByteArray())
                outputStream.flush()
                outputStream.close()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generateKey(str: String): SecretKey {

        return SecretKeySpec(str.toByteArray(), "AES")
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class, InvalidParameterSpecException::class, IllegalBlockSizeException::class, BadPaddingException::class, UnsupportedEncodingException::class)
    fun encryptMsg(message: String, secret: SecretKey?): String? {
        val aesCipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        aesCipher.init(Cipher.ENCRYPT_MODE, secret, IvParameterSpec(ByteArray(16)))
        val byteCipherText = aesCipher.doFinal(message.toByteArray())
        return Base64.encodeToString(byteCipherText, Base64.DEFAULT)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun createRSAKeyPair()
    {
        val alias = binding.fileNameEdit.text.toString()+"_alias"
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {

                val keyPairGenerator = KeyPairGenerator.getInstance(
                    "RSA", "AndroidKeyStore"
                )
                keyPairGenerator.initialize(
                    KeyGenParameterSpec.Builder(
                        alias,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()
                )
                val keyPair = keyPairGenerator.generateKeyPair()
                Log.d("PlayerFragment",keyPair.private.toString())
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception " + e.message + " occurred", Toast.LENGTH_LONG).show()
            Log.e("PlayerFragment", Log.getStackTraceString(e))
        }
    }
    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidParameterSpecException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class, UnsupportedEncodingException::class)
    fun decryptMsg(cipherText: String?, secret: SecretKey?): String? {
        val cipher: Cipher? = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher?.init(Cipher.DECRYPT_MODE, secret,IvParameterSpec(ByteArray(16)))
        val b=cipher?.doFinal(Base64.decode(cipherText,Base64.DEFAULT))
        return b?.let { String(it, charset("UTF-8")) }
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class, IllegalBlockSizeException::class, BadPaddingException::class)
    fun rsaEncrypt(): String? {

        val publicKeyEntry= if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            keyStore.getCertificate(binding.fileNameEdit.text.toString()+"_alias").publicKey
        }
        else{
            val privateKey = keyStore.getEntry(binding.fileNameEdit.text.toString()+"_alias", null) as KeyStore.PrivateKeyEntry
            privateKey.certificate.publicKey

        }

        val cipher:Cipher = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            Cipher.getInstance("RSA/ECB/PKCS1Padding","AndroidKeyStoreBCWorkaround")
        else
            Cipher.getInstance("RSA/ECB/PKCS1Padding","AndroidOpenSSL")

        cipher.init(Cipher.ENCRYPT_MODE, publicKeyEntry)
        val encryptedKey = cipher.doFinal(generateKey("abcdefghijklmnop").encoded)

        val aesKey = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
        context?.getSharedPreferences("crypt_keys", Context.MODE_PRIVATE)?.edit()
                ?.putString(binding.fileNameEdit.text.toString() + "_aes", aesKey)
                ?.apply()
        return aesKey
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class, IllegalBlockSizeException::class, BadPaddingException::class)
    fun rsaDecrypt(encryptedBytes: String): ByteArray? {
        val privateKeyEntry= if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            keyStore.getKey(binding.fileNameEdit.text.toString()+"_alias",null) as PrivateKey
        }
        else{
            val privateKey = keyStore.getEntry(binding.fileNameEdit.text.toString()+"_alias", null) as KeyStore.PrivateKeyEntry
            privateKey.privateKey

        }

        val cipher1:Cipher = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            Cipher.getInstance("RSA/ECB/PKCS1Padding","AndroidKeyStoreBCWorkaround")
        else
            Cipher.getInstance("RSA/ECB/PKCS1Padding","AndroidOpenSSL")
        cipher1.init(Cipher.DECRYPT_MODE, privateKeyEntry)
        return cipher1.doFinal(Base64.decode(encryptedBytes, Base64.DEFAULT))
    }

}