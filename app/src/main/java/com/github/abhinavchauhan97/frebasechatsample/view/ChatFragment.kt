package com.github.abhinavchauhan97.frebasechatsample.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.abhinavchauhan97.frebasechatsample.R
import com.github.abhinavchauhan97.frebasechatsample.model.ChatMessage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import kotlin.random.Random

class ChatFragment : Fragment(R.layout.fragment_chat){

    lateinit var user:FirebaseUser
    var initialLoad = true
    lateinit var color:Color
    lateinit var messageNode:DatabaseReference
    lateinit var colorNode:DatabaseReference
    lateinit var chatRecyclerAdapter: ChatRecyclerAdapter
    val oldMessages = LinkedList<ChatMessage>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser!!
        color =  Color.valueOf(Random.nextInt()/256f, Random.nextInt()/256f, Random.nextInt()/256f)
        FirebaseDatabase.getInstance().getReference("color").child(user.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value == null) {
                            snapshot.ref.setValue(color.toArgb())
                        } else {
                            val color = snapshot.getValue(Int::class.java)
                            setUI(color!!)
                            setMessages()
                        }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun setUI(color : Int){
        chatRecyclerAdapter = ChatRecyclerAdapter()
        requireView().findViewById<RecyclerView>(R.id.chat_list_recyclerView).apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)
            adapter = chatRecyclerAdapter
        }
        val inputBox = requireView().findViewById<EditText>(R.id.input_edittext).apply {
            backgroundTintList = ColorStateList.valueOf(Color.valueOf(Random.nextInt()/256f, Random.nextInt()/256f, Random.nextInt()/256f).toArgb())
        }
        requireView().findViewById<FloatingActionButton>(R.id.send_button).apply {
            setOnClickListener {
                if(inputBox.text.isNotBlank()){
                    val id = System.currentTimeMillis()
                    val message = ChatMessage(id,user.uid,user.displayName!!,inputBox.text.toString(),color)
                    messageNode.child(id.toString()).setValue(message)
                    if(!initialLoad){
                        oldMessages.add(0,message)
                        chatRecyclerAdapter.submitList(null)
                        chatRecyclerAdapter.submitList(oldMessages)
                    }
                    inputBox.setText("")
                }
            }
        }
    }
    private fun setMessages(){
        messageNode = FirebaseDatabase.getInstance().getReference("chat")
        messageNode.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEachIndexed { index , message ->
                        val m = message.getValue(ChatMessage::class.java)!!
                        if(initialLoad) {
                            oldMessages.add(0,m)
                        }else {
                            if(index == snapshot.childrenCount.toInt() - 1) {
                                if(m.userId != user.uid) {
                                    oldMessages.add(0,m)
                                }
                            }
                        }
                }
                initialLoad = false
                chatRecyclerAdapter.submitList(null)
                chatRecyclerAdapter.submitList(oldMessages)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}