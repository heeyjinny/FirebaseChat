package com.heeyjinny.firebasechat.model

//메시지를 주고받기 위해 사용되는 클래스
//채팅방에서 채팅 목록을 보여주기 위한 코드
//아이디, 메시지, 유저이름, 전송시간
class Messages {
    var id: String = ""
    var msg: String = ""
    var userName: String = ""
    var time: Long = 0

    constructor()

    constructor(msg: String, userName: String){
        this.msg = msg
        this.userName = userName
        this.time = System.currentTimeMillis()
    }

}