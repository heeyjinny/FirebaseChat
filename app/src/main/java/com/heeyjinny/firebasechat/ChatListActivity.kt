package com.heeyjinny.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.heeyjinny.firebasechat.databinding.ActivityChatListBinding
import com.heeyjinny.firebasechat.model.Room

//채팅방 목록/방 만들기 기능 구현
class ChatListActivity : AppCompatActivity() {

    //1
    //뷰바인딩
    val binding by lazy { ActivityChatListBinding.inflate(layoutInflater) }

    //2
    //데이터베이스와 연결하기
    val database = Firebase.database

    //3
    //최상위 노드 rooms 연결
    val roomsRef = database.getReference("rooms")

    //4
    //사용자 아이디와 이름을 저장하는 프로퍼티 선언
    //채팅목록뿐만아니라 다른 액티비티에서 조회가 가능하도록
    //compain object로 선언!
    companion object{
        var userId: String = ""
        var userName: String = ""
    }

    //9
    //방 목록을 저장한 roomList 프로퍼티 선언
    val roomList = mutableListOf<Room>()

    //10
    //어댑터를 저장한 프로퍼티 선언
    lateinit var adapter: ChatRoomListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //4-1
        //위 선언한 프로퍼티에
        //인텐트로 전달된 값 저장
        //만약 값이 없을 때 엘비스 연산자 사용하여 기본 값 설정
        userId = intent.getStringExtra("userId") ?: "none"
        userName = intent.getStringExtra("userName") ?: "Anonymous"

        //7
        //방만들기 버튼 클릭 시
        //만들기 다이얼로그 생성
        binding.btnCreate.setOnClickListener { openCreateRoom() }

        //11
        //화면의 리사이클러뷰와 어탭터 연결
        adapter = ChatRoomListAdapter(roomList)
        binding.recyclerRooms.adapter = adapter
        binding.recyclerRooms.layoutManager = LinearLayoutManager(baseContext)

        //13
        //목록을 갱신하는 메서드 호출
        loadRooms()

    }//onCreate

    //5
    //방 만들기 버튼 클릭 시
    //AlertDialog를 사용해
    //방 정보 입력 창 띄우기
    fun openCreateRoom(){
        //5-1
        //방 이름을 입력할 EditText를 코드로 생성
        val editText = EditText(this)
        //5-2
        //다이얼로그 생성
        val dialog = AlertDialog.Builder(this)
            .setTitle("방 이름")
            .setView(editText).setNegativeButton("취소",null)
            .setPositiveButton("만들기"){ dlg, id ->
                //5-3
                //방이름 입력 여부 체크...
                if (editText.text.isNotEmpty()){
                    createRoom(editText.text.toString())
                }else{
                    Toast.makeText(baseContext, "방 이름을 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                }
        }
        //5-4
        //5-3의 속성을 가지고 있는 다이얼로그 띄우기
        dialog.show()
    }//openCreateRoom

    //6
    //실제 방을 만드는 creatRoom메서드 생성
    //다이얼로그의 만들기버튼 클릭 시 호출
    fun createRoom(title: String){
        //6-1
        //방 데이터 생성 Room클래스 데이터 저장
        val room = Room(title, userName)
        //6-2
        //방 아이디 만들어서 입력
        //.push().key!!를 이용해 데이터베이스에 자동아이디 생성
        val roomId = roomsRef.push().key!!
        room.id = roomId

        //6-3
        //파이어베이스에 전송
        roomsRef.child(roomId).setValue(room)

    }//createRoom

    //12
    //목록을 갱신하는 메서드 생성
    //addValueEventListener사용...
    //필수메서드가 2개이므로 object로 받아서 생성
    fun loadRooms(){
        roomsRef.addValueEventListener(object: ValueEventListener{
            //12-1
            //데이터가 정상적으로 변경되었으면
            //파이어베이스 리얼타임 데이터베이스에서 rooms목록을 불러온 후
            //roomList에 저장하고 목록갱신함
            override fun onDataChange(snapshot: DataSnapshot) {
                //12-1
                //기존 방목록 삭제하고 방목록에 추가
                roomList.clear()
                for (item in snapshot.children){
                    item.getValue(Room::class.java)?.let {
                        roomList.add(it)
                    }
                }
                //12-2
                //어댑터 갱신
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }

        })
    }//loadRooms

}//ChatListActivity

//8
//채팅방을 새로 만들면
//데이터베이스에 rooms 노드가 추가됨
//그 rooms의 목록을 리사이클러뷰에 보여주기
//리사이클러뷰 어댑터를 만들고 뷰와 연결하기
//새로 생성하지 않고 액티비티 밑에 클래스 생성...
class ChatRoomListAdapter(val roomList: MutableList<Room>):
    RecyclerView.Adapter<ChatRoomListAdapter.Holder>() {//ChatRoomListAdapter

    //8-3
    //레이아웃을 생성하고 Holder에 담은 후 리턴...
    //텍스트뷰가 1개인 간단한 목록이어서 아이템 레이아웃을 따로 생성하지 않음
    //안드로이드 기본제공하는 아이템 simple_list_item_1 사용...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1,parent,false)

        return Holder(view)
    }

    //8-4
    //현재 포지션의 데이터를 꺼낸 후 홀더에 전달
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val room = roomList.get(position)
        holder.setRoom(room)
    }

    //8-2
    //아이템 목록 리턴
    override fun getItemCount(): Int {
        return roomList.size
    }

    //8-1
    //아이템 레이아웃을 따로 만들지 않았기 때문에 뷰바인딩 처리는 하지 않음...
    //기본 아이템 뷰 사용예정이므로 바인딩말고
    //View를 import함
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        //13
        //목록 아이템 클릭 시 채팅방으로 이동
        lateinit var mRoom: Room
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChatRoomActivity::class.java)
                intent.putExtra("roomId", mRoom.id)
                intent.putExtra("roomTitle", mRoom.title)
                itemView.context.startActivity(intent)
            }
        }

        //8-5
        //setRoom()메서드 생성
        //바인딩을 사용하지 않기 때문에 findViewById사용하여 위젯접근
        //기본제공 아이템레이아웃의 텍스트뷰의 아이디는 text1 임...
        fun setRoom(room: Room){
            //14
            //클릭 리스너를 init에서 구현하기때문에
            //setRoom메서드에서 채팅방을 저장하여 init에서 사용...
            this.mRoom = room

            //8-5
            itemView.findViewById<TextView>(android.R.id.text1).setText(room.title)
        }
    }

}//ChatRoomListAdapte
