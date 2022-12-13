package com.heeyjinny.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.heeyjinny.firebasechat.databinding.ActivityChatRoomBinding
import com.heeyjinny.firebasechat.databinding.ItemMsgListBinding
import com.heeyjinny.firebasechat.model.Messages
import java.text.SimpleDateFormat

//채팅방 구현
//데이터베이스의 rooms의 아이디노드 아래 방 정보와 함께 저장되는
//메시지 목록 사용
//rooms.child("방아이디").child("메시지들") 단계로 참조됨
class ChatRoomActivity : AppCompatActivity() {

    //1
    //뷰바인딩
    val binding by lazy { ActivityChatRoomBinding.inflate(layoutInflater) }

    //2
    //파이어베이스 데이터베이스 연결
    val database = Firebase.database

    //3
    //메시지노드를 참조하는 프로퍼티 생성
    //메시지노드 참조 시 방 아이디가 필요하기 때문에
    //lateinit 으로 먼저 선언
    lateinit var msgRef: DatabaseReference

    //4
    //방 아이디, 방 제목 프로퍼티 미리 선언
    var roomId: String = ""
    var roomTitle: String = ""

    //5
    //메시지목록 프로퍼티 선언
    val msgList = mutableListOf<Messages>()

    //6
    //어댑터를 저장할 프로퍼티 선언 후 어댑터 생성
    lateinit var adapter: MsgListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //13
        //인텐트로 전달된 방 정보와 사용자 정보꺼내기
        roomId = intent.getStringExtra("roomId") ?: "none"
        roomTitle = intent.getStringExtra("roomTitle") ?: "없음"

        //14
        //메시지 노드 레퍼런스 연결
        //최상의노드 rooms안에 id에 들어가 messages생성 및 연결
        msgRef = database.getReference("rooms").child(roomId).child("messages")

        //15
        //어댑터 생성
        adapter = MsgListAdapter(msgList)

        with(binding){
            //16
            //리사이클러뷰와 어댑터 연결
            recyclerMsg.adapter = adapter
            recyclerMsg.layoutManager = LinearLayoutManager(baseContext)

            //20
            //방제목 입력
            textRoomName.text = roomTitle
            //21
            //뒤로가기버튼 방종료하기
            //전송버튼 메시지 전송
            btnBack.setOnClickListener { finish() }
            btnSend.setOnClickListener { sendMsg() }
        }

        //18
        //메시지 목록을 읽어와 갱신
        //loadMsg()호출
        loadMsg()

    }//onCreate

    //17
    //메시지 목록을 읽어오는 ValueEventListener 를 호출하는
    //loadMsg()메서드 생성
    fun loadMsg(){
        msgRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //17-1
                //메시지목록 삭제
                msgList.clear()
                for (item in snapshot.children){
                    //17-2
                    //메시지 목록에 추가
                    item.getValue(Messages::class.java)?.let {
                        msgList.add(it)
                    }
                }
                //17-3
                //어댑터 갱신
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }

        })
    }//loadMsg

    //19
    //메시지를 파이어베이스에 전송하는
    //메서드 sendMsg 작성
    fun sendMsg(){
        with(binding){
            //19-1
            //입력된 메시지가 있을때만 처리...
            if (editMsg.text.isNotEmpty()){
                val message = Messages(editMsg.text.toString(), ChatListActivity.userName)
                val msgId = msgRef.push().key!!
                message.id = msgId
                msgRef.child(msgId).setValue(message)
                //19-2
                //메시지 입력 후 입력필드 삭제
                editMsg.setText("")
            }
        }
    }//sendMsg

}//ChatRoomActivity

//7
//어댑터클래스 생성
class MsgListAdapter(val msgList:MutableList<Messages>): RecyclerView.Adapter<MsgListAdapter.Holder>(){

    //10
    //뷰생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemMsgListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return Holder(binding)
    }

    //11
    //현재 위치 바인딩 처리
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val msg = msgList.get(position)
        holder.setMsg(msg)
    }

    //9
    //목록의 개수
    override fun getItemCount(): Int {
        return msgList.size
    }

    //8
    //홀더생성
    inner class Holder(val binding: ItemMsgListBinding): RecyclerView.ViewHolder(binding.root){
        //12
        //바인딩처리
        fun setMsg(msg:Messages){
            with(binding){
                textName.setText(msg.userName)
                textMsg.setText(msg.msg)

                //24시간 HH...
                var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                textDate.setText("${sdf.format(msg.time)}")
            }
        }

    }

}//MsgListAdapter