package com.majorproject.heartout

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.google.common.collect.Lists
import java.util.*

class MainActivity<ChatAdapter> : AppCompatActivity() {


    var chatView: RecyclerView? = null
    var chatAdapter: ChatAdapter? = null
    var messageList: List<Message> = ArrayList<Message>()
    var editMessage: EditText? = null
    var btnSend: ImageButton? = null

    //dialogFlow
    private var sessionsClient: SessionsClient? = null
    private var sessionName: SessionName? = null
    private val uuid = UUID.randomUUID().toString()
    private val TAG = "mainactivity"


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatView = findViewById(R.id.chatView)
        editMessage = findViewById(R.id.editMessage)
        btnSend = findViewById(R.id.btnSend)

        val also = ChatAdapter(messageList, this).also {
            var it = null
            chatAdapter = it
        }
        chatView.setAdapter(chatAdapter)

        btnSend.setOnClickListener(View.OnClickListener {
            val message = editMessage.getText().toString()
            if (!message.isEmpty()) {
                messageList.add(Message(message, false))
                editMessage.setText("")
                sendMessageToBot(message)
                Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged()
                Objects.requireNonNull(chatView.getLayoutManager())
                        .scrollToPosition(messageList.size - 1)
            } else {
                Toast.makeText(this@MainActivity, "Please enter text!", Toast.LENGTH_SHORT).show()
            }
        })

        setUpBot()
    }

    private fun setUpBot() {
        try {
            val stream = this.resources.openRawResource(R.raw.credentials)
            val credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))
            val projectId = (credentials as ServiceAccountCredentials).projectId
            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build()

            sessionName = SessionName.of(projectId, uuid)
            Log.d(TAG, "projectId : $projectId")
        } catch (e: Exception) {
            Log.d(TAG, "setUpBot: " + e.message)
        }
    }

    private fun sendMessageToBot(message: String) {
        val input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build()
                .helpersSendMessageInBg(this, sessionName, sessionsClient, input).execute()
    }

    fun callback(returnResponse: DetectIntentResponse?) {
        if (returnResponse != null) {
            val botReply = returnResponse.queryResult.fulfillmentText
            if (!botReply.isEmpty()) {
                messageList.add(Message(botReply, true))
                chatAdapter.notifyDataSetChanged()
                Objects.requireNonNull(chatView!!.layoutManager)!!.scrollToPosition(messageList.size - 1)
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show()
        }
    }

}













