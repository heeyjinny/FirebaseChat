package com.heeyjinny.firebasechat.model

//Room 클래스
//채팅방생성, 생성된 채팅방 목록에 보여주는 클래스
//방 아이디, 방 이름
class Room {
    var id: String = ""
    var title: String = ""
    //users프로퍼티에는 하나의 이름만 입력되는데
    //프로젝트가 끝난 후 여러개의 이름이 입력될 수 있도록 설계
    var users: String = ""

    constructor()

    constructor(title: String, creatorName: String){
        this.title = title
        users = creatorName
    }
}