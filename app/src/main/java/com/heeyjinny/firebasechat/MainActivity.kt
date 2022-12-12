package com.heeyjinny.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**  실시간 채팅 앱 만들기(파이어베이스-리얼타임 데이터베이스)  **/

//1
//안드로이드 화면 만들기화면
//로그인/회원가입, 채팅방목록, 채팅방 3개 화면 필요
//activity_main.xml - 로그인/회원가입

//2



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}