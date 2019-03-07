package com.example.firebase_login_database_kotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val hd = Handler()
        hd.postDelayed(splashhandler(), 2000) // 1초 후에 hd handler 실행  3000ms = 3초

    }

    private inner class splashhandler : Runnable {
        override fun run() {
            startActivity(Intent(application, SignInActivity::class.java)) //로딩이 끝난 후, ChoiceFunction 이동
            this@SplashActivity.finish() // 로딩페이지 Activity stack에서 제거
        }
    }

    override fun onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}
