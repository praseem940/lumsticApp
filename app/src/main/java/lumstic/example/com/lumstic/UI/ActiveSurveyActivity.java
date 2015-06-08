package lumstic.example.com.lumstic.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Adapters.DBAdapter;
import lumstic.example.com.lumstic.Adapters.DashBoardAdapter;
import lumstic.example.com.lumstic.Models.Categories;
import lumstic.example.com.lumstic.Models.Options;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;
import lumstic.example.com.lumstic.Utils.JsonHelper;
import lumstic.example.com.lumstic.Utils.LumsticApp;


public class ActiveSurveyActivity extends Activity {
    ListView listView;
    LumsticApp lumsticApp;
    List<Surveys> surveysList;
    JsonHelper jsonHelper;
    boolean surveyFromServer = false;
    ProgressDialog progressDialog;
    DashBoardAdapter dashBoardAdapter;
    DBAdapter dbAdapter;
    String fetchUrl = "http://survey-web-stgng.herokuapp.com/api/deep_surveys?access_token=";
    String jsonFetchString = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_active_survey);
        getActionBar().setTitle("DashBoard");

        lumsticApp = (LumsticApp) getApplication();
        dbAdapter = new DBAdapter(ActiveSurveyActivity.this);

        progressDialog = new ProgressDialog(ActiveSurveyActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Surveys ");
        progressDialog.show();
        new FetchSurvey().execute();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.active_survey, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            return true;
        }
        if (id == R.id.action_logout) {
            lumsticApp.getPreferences().setAccessToken("");
            Intent i = new Intent(ActiveSurveyActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class FetchSurvey extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            fetchUrl = fetchUrl + lumsticApp.getPreferences().getAccessToken();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(fetchUrl);
                HttpResponse httpResponse = httpclient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                jsonFetchString = EntityUtils.toString(httpEntity);
                Log.e("fetchstring", jsonFetchString);


            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            return jsonFetchString;
        }

        @Override
        protected void onPostExecute(String s) {
            jsonHelper = new JsonHelper(ActiveSurveyActivity.this);
            surveysList = new ArrayList<Surveys>();
            try {

//                lumsticApp.getPreferences().setSurveyData(s);

                //       surveysList = jsonHelper.tryParsing(lumsticApp.getPreferences().getSurveyData());
                surveysList = jsonHelper.tryParsing(jsonHelper.getStringFromJson());


            } catch (Exception e) {
                e.printStackTrace();
            }
            listView = (ListView) findViewById(R.id.active_survey_list);
            if (surveysList != null) {
                dashBoardAdapter = new DashBoardAdapter(getApplicationContext(), surveysList);
                progressDialog.dismiss();
            }
            if (surveysList == null) {
                progressDialog.dismiss();
                Toast.makeText(ActiveSurveyActivity.this, "Please check your wifi or network settings", Toast.LENGTH_SHORT).show();
            }

            try {
                for (int i = 0; i < surveysList.size(); i++) {

                    Surveys surveys = surveysList.get(i);
                    long value = dbAdapter.insertDataSurveysTable(surveys);
                    //Toast.makeText(ActiveSurveyActivity.this, value + "", Toast.LENGTH_SHORT).show();
                    if (surveys.getCategories().size() > 0)
                        addCategories(surveys);
                    if (surveys.getQuestions().size() > 0)
                        addQuestions(surveys);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            listView.setAdapter(dashBoardAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Surveys surveys = surveysList.get(i);
                    Intent intent = new Intent(ActiveSurveyActivity.this, SurveyDetailsActivity.class);
                    intent.putExtra(IntentConstants.SURVEY, surveys);
                    startActivity(intent);
                }
            });
        }
    }


    public void addCategories(Surveys surveys) {

        for (int h = 0; h < surveys.getCategories().size(); h++) {
            Categories categories = surveys.getCategories().get(h);
            long value1 = dbAdapter.insertDataCategoriesTable(categories);
            Log.e("value", value1 + "");
            if (categories.getQuestionsList().size() > 0)
                addQuestionFromCategories(categories);
        }
    }

    public void addQuestions(Surveys surveys) {

        for (int j = 0; j < surveys.getQuestions().size(); j++) {
            Questions question = surveys.getQuestions().get(j);
            long value1 = dbAdapter.insertDataQuestionTable(question);
            Log.e("value", value1 + "");
            if (question.getOptions().size() > 0)
                addOptions(question);
        }
    }


    public void addQuestionFromCategories(Categories categories) {
        for (int m = 0; m < categories.getQuestionsList().size(); m++) {
            Questions question = categories.getQuestionsList().get(m);
            long value1 = dbAdapter.insertDataQuestionTable(question);
            Log.e("value", value1 + "");

            if (question.getOptions().size() > 0)
                addOptions(question);
        }
    }


    public void addOptions(Questions questions) {

        for (int k = 0; k < questions.getOptions().size(); k++) {
            Options options = questions.getOptions().get(k);
            long value1 = dbAdapter.insertDataOptionsTable(options);
            Log.e("value", value1 + "");
            if (options.getQuestions().size() > 0)
                addNestedQuestions(options);


            if (options.getCategories().size() > 0)
                addNestedCategories(options);


        }
    }


    public void addNestedCategories(Options options) {


        for (int d = 0; d < options.getCategories().size(); d++) {
            Categories categories = options.getCategories().get(d);
            long value1 = dbAdapter.insertDataCategoriesTable(categories);
            Log.e("value", value1 + "");
            if (categories.getQuestionsList().size() > 0)
                addQuestionFromCategories(categories);
        }

    }

    public void addNestedQuestions(Options options) {


        for (int l = 0; l < options.getQuestions().size(); l++) {
            Questions question = options.getQuestions().get(l);
            long value1 = dbAdapter.insertDataQuestionTable(question);
            Log.e("value", value1 + "");
            if (question.getOptions().size() > 0)
                addOptions(question);
        }

    }


}
