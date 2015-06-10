package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    String jsonStr=null;
    Answers ans;

    int surveyId = 0;
    String uploadUrl = "https://survey-web-stgng.herokuapp.com/api/responses.json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_details);
        actionBar = getActionBar();
        completedResponseIds = new ArrayList<Integer>();
        lumsticApp = (LumsticApp) getApplication();

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

                //Toast.makeText(SurveyDetailsActivity.this, "hcbxsduhcbduhcd", Toast.LENGTH_LONG).show();
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


            completedResponseIds = dbAdapter.getCompleteResponsesIds(surveyId);
            for (int i = 0; i < completedResponseIds.size(); i++) {
                answerses = new ArrayList<Answers>();

                completedResponseIds.get(i);

                answerses = null;

                answerses = dbAdapter.getAnswerByResponseId(completedResponseIds.get(i));
                JSONArray jsonArray= new JSONArray() ;
                for(int j=0;j<answerses.size();j++){
                    JSONObject jsonObject= new JSONObject();
                    try {
                        jsonObject.put("question_id", answerses.get(j).getQuestion_id());
                        jsonObject.put("updated_at", answerses.get(j).getUpdated_at());
                        jsonObject.put("content", answerses.get(j).getContent());

//                        if(!answerses.get(j).getImage().equals(null)){
//
//
//                            String path ="Environment.getExternalStorageDirectory().toString() + \"/saved_images\"";
//                            Bitmap b = null;
//                            String fileName=answerses.get(j).getImage();
//                                try {
//                                    File f = new File(path, fileName);
//                                     b= BitmapFactory.decodeStream(new FileInputStream(f));
//
//
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//
//                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                            b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                            byte[] byteArray = byteArrayOutputStream .toByteArray();
//                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                            jsonObject.put("image", encoded);
//
//                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("answers_attributes", jsonArray);
                    obj.put("status","complete");
                    obj.put("survey_id",surveys.getId());
                    obj.put("updated_at",1433848151);
                    obj.put("longitude",73.8291466666657);
                    obj.put("latitude",18.54194666666656);
                    obj.put("user_id",158);
                    obj.put("organization_id",23);
                    obj.put("access_token",lumsticApp.getPreferences().getAccessToken());
                    obj.put("mobile_id","17fc567e-0fc7-444f-a0f9-201d966779b9");
                    JSONObject responseObj= new JSONObject();
                    obj.put("response",responseObj);
                    responseObj.put("answers_attributes", jsonArray);
                    responseObj.put("status", "complete");
                    responseObj.put("survey_id",surveys.getId());
                    responseObj.put("updated_at",1433848151);
                    responseObj.put("longitude",73.8291466666657);
                    responseObj.put("latitude",18.54194666666656);
                    responseObj.put("user_id",158);
                    responseObj.put("organization_id",23);

                    responseObj.put("mobile_id","17fc567e-0fc7-444f-a0f9-201d966779b9");


                     jsonStr = obj.toString();

                   Log.e("jsonString", jsonStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }



            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uploadUrl);
//            List nameValuePairs = new ArrayList();
//            nameValuePairs.add(new BasicNameValuePair("answer_attribute", jsonStr));
            List nameValuePairs = new ArrayList();
           nameValuePairs.add(new BasicNameValuePair("answer_attribute", ""));
            try {
                httppost.setHeader("Content-type", "application/json");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpclient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String jsonLoginString = EntityUtils.toString(httpEntity);
                Log.e("jsonsyncresponse",jsonLoginString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }







            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(SurveyDetailsActivity.this, completedResponseIds.get(0) + "", Toast.LENGTH_LONG).show();
        }
    }

}
