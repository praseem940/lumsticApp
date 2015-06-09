package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Adapters.DBAdapter;
import lumstic.example.com.lumstic.Models.Answers;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Responses;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;
import lumstic.example.com.lumstic.Utils.LumsticApp;

public class SurveyDetailsActivity extends Activity {

    LinearLayout completeResponsesLinearLayout;
    LinearLayout incompleteResponsesLinearLayout;
    Button addResponsesButton;
    ActionBar actionBar;
    Surveys surveys;
    ImageView uploadButton;
    TextView surveyTitleText, surveyDescriptionText, endDateText;
    List<Questions> questionsList;
    Responses responses;
    List<Answers> answerses;
    int completeCount = 0, incompleteCount = 0;
    DBAdapter dbAdapter;
    TextView completeTv, incompleteTv;
    List<Integer> completedResponseIds;
    LumsticApp lumsticApp;
    Answers ans;

    int surveyId = 0;
    String uploadUrl = "http://survey-web-stgng.herokuapp.com/api/response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_details);
        actionBar = getActionBar();
        completedResponseIds = new ArrayList<Integer>();
        lumsticApp= (LumsticApp) getApplication();

        if (getIntent().hasExtra(IntentConstants.SURVEY)) {
            surveys = new Surveys();
            surveys = (Surveys) getIntent().getExtras().getSerializable(IntentConstants.SURVEY);
            actionBar.setTitle(surveys.getName());
            questionsList = new ArrayList<Questions>();
            questionsList = surveys.getQuestions();
        } else
            actionBar.setTitle("Survey Detail");


        surveyId = surveys.getId();

        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_ic_back);
        actionBar.setDisplayShowTitleEnabled(true);

        uploadButton = (ImageView) findViewById(R.id.upload_button);
        surveyTitleText = (TextView) findViewById(R.id.survey_title_text);
        surveyDescriptionText = (TextView) findViewById(R.id.survey_description_text);
        endDateText = (TextView) findViewById(R.id.end_date_text);
        completeTv = (TextView) findViewById(R.id.complete_response);
        incompleteTv = (TextView) findViewById(R.id.incomplete_response);


        responses = new Responses();
        dbAdapter = new DBAdapter(SurveyDetailsActivity.this);
        if (getIntent().hasExtra(IntentConstants.SURVEY)) {

            surveyTitleText.setText(surveys.getName());
            surveyDescriptionText.setText(surveys.getDescription());
            endDateText.setText(surveys.getExpiryDate());
        }
        addResponsesButton = (Button) findViewById(R.id.add_responses_button);
        incompleteResponsesLinearLayout = (LinearLayout) findViewById(R.id.incomplete_response_container);
        completeResponsesLinearLayout = (LinearLayout) findViewById(R.id.complete_response_container);
        incompleteResponsesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SurveyDetailsActivity.this, IncompleteResponseActivity.class);
                intent.putExtra(IntentConstants.SURVEY, surveys);
                startActivity(intent);
            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(SurveyDetailsActivity.this,"hcbxsduhcbduhcd",Toast.LENGTH_LONG).show();
                new uploadResponse().execute();

            }
        });


        completeResponsesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SurveyDetailsActivity.this, CompleteResponsesActivity.class);
                i.putExtra(IntentConstants.SURVEY, surveys);
                startActivity(i);
            }
        });

        incompleteCount = dbAdapter.getIncompleteResponse(surveys.getId());
        completeCount = dbAdapter.getCompleteResponse(surveys.getId());


        incompleteTv.setText(incompleteCount + "");
        completeTv.setText(completeCount + "");

        addResponsesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getIntent().hasExtra(IntentConstants.SURVEY)) {
                    responses.setSurveyId(surveys.getId());
                    responses.setStatus("incomplete");
                    Intent intent = new Intent(SurveyDetailsActivity.this, NewResponseActivity.class);
                    intent.putExtra(IntentConstants.SURVEY, (java.io.Serializable) surveys);
                    startActivity(intent);
                }
                Toast.makeText(SurveyDetailsActivity.this, dbAdapter.insertDataResponsesTable(responses) + "", Toast.LENGTH_SHORT).show();


            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.survey_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class uploadResponse extends AsyncTask<Void, Void, String> {


        protected String doInBackground(Void... voids) {


            answerses = new ArrayList<Answers>();
            completedResponseIds = dbAdapter.getCompleteResponsesIds(surveyId);
            for (int i = 0; i < completedResponseIds.size(); i++) {


                completedResponseIds.get(i);

                 ans = dbAdapter.getAnswerByResponseId(completedResponseIds.get(i));
                answerses.add(ans);

            }

            JSONObject  answers_attributes= new JSONObject();
            for(int i=0;i<answerses.size();i++){
                JSONObject obj= new JSONObject();

                try {
                    obj.put("question_id",answerses.get(i).getQuestion_id());
                    obj.put("updated_at",answerses.get(i).getUpdated_at());
                    obj.put("content",answerses.get(i).getContent());
                    obj.put("image",answerses.get(i).getImage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
try {

    answers_attributes.put("status", "complete");
    answers_attributes.put("survey_id", 757);
    answers_attributes.put("updated_at", 898989);
    answers_attributes.put("longitude", 73);
    answers_attributes.put("latitude", 18);
    answers_attributes.put("user_id", 344);
    answers_attributes.put("organisation_id", 2323);
    answers_attributes.put("access_token", lumsticApp.getPreferences().getAccessToken());
    answers_attributes.put("mobile_id", "");


}
catch (JSONException e){
    e.printStackTrace();
}



            JSONObject  response= new JSONObject();
            response.put(answers_attributes);












                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(uploadUrl);
                HttpResponse response = null;

                StringEntity se = null;
                try {
                    se = new StringEntity( answers_attributes.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);
                try {
                    response = httpclient.execute(httppost);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(response!=null){
                    try {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(SurveyDetailsActivity.this, completedResponseIds.get(0) + "", Toast.LENGTH_LONG).show();
        }
    }

}
