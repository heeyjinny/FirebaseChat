package com.heeyjinny.firebasechat.model

//회원가입과 로그인 화면에서
//사용자 아이디, 비밀번호, 별명 입력받는 코드
class User {
    var id: String = ""
    var password: String = ""
    var name: String = ""

    constructor()

    constructor(id: String, password: String, name: String){
        this.id = id
        this.password = password
        this.name = name
    }
}