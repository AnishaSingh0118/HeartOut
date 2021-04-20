package helpers

import android.util.Log
import com.google.cloud.dialogflow.v2.*
import com.majorproject.heartout.MainActivity
import interfaces.BotReply

class SendMessageInBg(mainActivity: MainActivity<Any?>, sessionName: SessionName?, sessionsClient: SessionsClient?, input: QueryInput?) {

    private var session: SessionName? = null
    private var sessionsClient: SessionsClient? = null
    private var queryInput: QueryInput? = null
    private val TAG = "async"
    private var botReply: BotReply? = null

    fun SendMessageInBg(botReply: BotReply?, session: SessionName?, sessionsClient: SessionsClient?,
                        queryInput: QueryInput?) {
        this.botReply = botReply
        this.session = session
        this.sessionsClient = sessionsClient
        this.queryInput = queryInput
    }

    protected fun doInBackground(vararg voids: Void?): DetectIntentResponse? {
        try {
            val detectIntentRequest = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build()
            return sessionsClient!!.detectIntent(detectIntentRequest)
        } catch (e: Exception) {
            Log.d(TAG, "doInBackground: " + e.message)
            e.printStackTrace()
        }
        return null
    }

    protected fun onPostExecute(response: DetectIntentResponse?) {
        //handle return response here
        botReply?.callback(response)
    }

}
