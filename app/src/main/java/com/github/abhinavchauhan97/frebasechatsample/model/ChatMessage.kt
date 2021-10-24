package com.github.abhinavchauhan97.frebasechatsample.model

data class ChatMessage(
    val id:Long,
    val userId:String,
    val name:String,
    val message:String,
    val color:Int
){
    constructor() : this(0L,"0","","",0)
}