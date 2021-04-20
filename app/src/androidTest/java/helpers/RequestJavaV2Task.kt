package helpers

import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import com.google.cloud.dialogflow.v2.DetectIntentRequest
import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.majorproject.heartout.MainActivity
import interfaces.BotReply

class RequestJavaV2Task(mainActivity: MainActivity<Any?>, sessionName: SessionName?, sessionsClient: SessionsClient?, input: QueryInput?){


        private val mInterface: BotReply,
         private val session: SessionName,
                        private val sessionsClient: SessionsClient,
                        private val queryInput: QueryInput) : AsyncTask<Void?, Void?, com.google.cloud.dialogflow.v2.DetectIntentResponse?>()
    {
        protected override fun doInBackground(vararg voids: Void): com.google.cloud.dialogflow.v2.DetectIntentResponse? {
            try {
                val detectIntentRequest = DetectIntentRequest.newBuilder()
                        .setSession(session.toString())
                        .setQueryInput(queryInput)
                        .build()
                return sessionsClient.detectIntent(detectIntentRequest)
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "doInBackground: " + e.message)
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(response: com.google.cloud.dialogflow.v2.DetectIntentResponse?) {
            mInterface.callback(response)
        }

