package com.heeyjinny.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.heeyjinny.firebasechat.databinding.ActivityMainBinding
import com.heeyjinny.firebasechat.model.User

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
//Messages 클래스
//메시지를 주고받기 위해 사용되는 클래스
//채팅방에서 채팅 목록을 보여주기 위한 코드
//아이디, 메시지, 유저이름, 전송시간

//6
//Room 클래스
//채팅방생성, 생성된 채팅방 목록에 보여주는 클래스
//방 아이디, 방 이름

//7
//로그인/회원가입 구현하기
//MainActivity.kt

//12
//채팅방 목록/방 만들기 기능 구현하기
//ChatListActivity.kt

//13
//채팅방 구현
//ChatRoomActivity.kt

class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //7-1
    //데이터베이스 프로퍼티에 파이어베이스 생성
    val database = Firebase.database

    //7-2
    //현재 액티비티에서 사용할 users 노드 연결
    val userRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //11
        //로그인버튼 클릭시 signIn() 호출, 회원가입버튼 클릭 시 signUp() 호출
        binding.btnSignin.setOnClickListener { signIn() }
        binding.btnSignUp.setOnClickListener { signUp() }

    }//onCreate

    //8
    //회원가입 버튼클릭 시 호출되는 함수생성
    //signUp()
    fun signUp(){
        with(binding){
            //8-1
            //입력된 값 가져오기
            val id = editId.text.toString()
            val password = editPw.text.toString()
            val name = editName.text.toString()

            //8-2
            //모두 값이 입력되어 있는지 검사***
            if (id.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()){

                //8-3
                //화면에 모두 값이 입력되어 있다면...
                //데이터베이스에 이미 아이디가 존재하는지 검사
                userRef.child(id).get().addOnSuccessListener {

                    if (it.exists()){
                        Toast.makeText(baseContext, "아이디가 이미 존재합니다.", Toast.LENGTH_SHORT).show()
                    }else{
                        //8-4
                        //데이터베이스에 아이디가 없다면
                        //저장 후 자동으로 로그인
                        //유저데이터클래스에 id,pw,name 전달
                        val user = User(id, password, name)
                        //8-5
                        //데이터베이스 user노드안 id에 유저정보 추가...
                        userRef.child(id).setValue(user)
                        
                        //8-6
                        //회원가입 완료 알림 메시지 후
                        //로그인...진행
                        Toast.makeText(baseContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        signIn()
                    }

                }
            }else{
                //8-7
                //입력창이 하나라도 비어있으면 메시지 출력
                Toast.makeText(baseContext, "아이디, 비밀번호, 별명을 모두 입력해야 합니다.", Toast.LENGTH_SHORT).show()
            }

        }//with
    }//signUp

    //9
    //로그인 버튼 클릭 시 호출되는 함수생성
    //singIn()
    fun signIn(){
        with(binding){
            //9-1
            //화면에 입력된 아이디, 비밀번호 값 가져오기
            val id = editId.text.toString()
            val password = editPw.text.toString()

            //9-2
            //만약 아이디, 비밀번호 값이 모두 입력되었다면
            if (id.isNotEmpty() && password.isNotEmpty()){
                //9-3
                //입력된 아이디로 User데이터 가져오기...
                userRef.child(id).get().addOnSuccessListener {
                    //9-4
                    //id가 데이터 베이스에 있는지 확인
                    if (it.exists()){
                        //9-5
                        //비밀번호 비교
                        it.getValue(User::class.java)?.let {
                            //9-6
                            //비밀번호가 일치하다면
                            if (it.password == password){
                                //9-7
                                //유저 아이디와 별명을 전달하면서
                                //채팅방 목록 들어가기...(밑에 생성)
                                Toast.makeText(baseContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                                goChatroomList(it.id, it.name)
                            }else{
                                //9-6-1
                                //비밀번호 불일치
                                Toast.makeText(baseContext, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{
                        //9-4-1
                        //아이디 없음
                        Toast.makeText(baseContext, "아이디가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                //9-2-1
                //아이디, 비밀번호 입력이 안되었다면
                Toast.makeText(baseContext, "아이디, 비밀번호를 모두 입력해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }//signIn

    //10
    //로그인 성공 시 채팅방 목록으로 넘어가기
    //사용자 정보를 파라미터로 전달
    //아이디, 별명
    fun goChatroomList(userId: String, userName: String){
        //10-1
        //채팅방 목록으로 넘어가는 인텐트 생성...
        val intent = Intent(this, ChatListActivity::class.java)

        //10-2
        //방 생성 또는 입장시 사용
        //인텐트 넘겨주면서 액티비티 시작
        intent.putExtra("userId", userId)
        intent.putExtra("userName", userName)
        startActivity(intent)
    }//goChatroomList

}//MainActivity