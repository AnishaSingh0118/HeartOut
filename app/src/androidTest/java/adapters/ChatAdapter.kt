package adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.majorproject.heartout.MainActivity
import com.majorproject.heartout.R
import models.Message

public class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MyViewHolder>(){

    private var messageList: List<Message>? = null
    private var activity: Activity? = null

    fun ChatAdapter(messageList: List<Message>?, activity: Activity?) {
        this.messageList = messageList
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(activity).inflate(R.layout.adapter_message_one, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message: String = messageList!![position].message
        val isReceived: Boolean = messageList!![position].isReceived
        if (isReceived) {
            holder.messageReceive.visibility = View.VISIBLE
            holder.messageSend.visibility = View.GONE
            holder.messageReceive.text = message
        } else {
            holder.messageSend.visibility = View.VISIBLE
            holder.messageReceive.visibility = View.GONE
            holder.messageSend.text = message
        }
    }

    override fun getItemCount(): Int {
        return messageList!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageSend: TextView
        var messageReceive: TextView

        init {
            messageSend = itemView.findViewById(R.id.message_send)
            messageReceive = itemView.findViewById(R.id.message_receive)
        }
    }
}
