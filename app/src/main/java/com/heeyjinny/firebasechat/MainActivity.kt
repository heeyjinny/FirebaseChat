package com.heeyjinny.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**  실시간 채팅 앱 만들기(파이어베이스-리얼타임 데이터베이스)  **/

//1
//3개 Activity 필요
//Main(로그인/회원가입)
//ChatList(채팅방 목록)
//ChatRoom(채팅방 화면)
//패키지명 우클릭 - New - Activity - 채티방목록 및 화면 생성

//2
//안드로이드 화면 만들기
//로그인/회원가입, 채팅방목록, 채팅방, 채팅방 메시지 화면
//activity_main.xml : 로그인/회원가입 화면
//activity_chat_list : 채팅방 목록 화면
//activity_chat_room : 채팅방 화면
//item_msg_list : 채팅방화면 목록의 메시지

//3



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}