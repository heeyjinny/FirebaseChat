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
//데이터 클래스 생성
//데이터 클래스를 생성할 model패키지 추가
//패키지명 우클릭 - New - Package - model
//model패키지에 메시지, 룸, 유저 클래스 추가

//4
//User 클래스
//회원가입과 로그인 화면에서
//사용자 아이디, 비밀번호, 별명 입력받는 코드

//5
//Message 클래스
//메시지를 주고받기 위해 사용되는 클래스
//채팅방에서 채팅 목록을 보여주기 위한 코드
//아이디, 메시지, 유저이름, 전송시간

//6
//Room 클래스
//채팅방생성, 생성된 채팅방 목록에 보여주는 클래스
//방 아이디, 방 이름

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}