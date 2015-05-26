package lumstic.example.com.lumstic.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
import lumstic.example.com.lumstic.Adapters.DashBoardAdapter;
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
    DashBoardAdapter dashBoardAdapter;
    String fetchUrl="http://www.lumstic.com/api/deep_surveys?access_token=";
    String jsonFetchString="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_survey);
        getActionBar().setTitle("DashBoard");

        lumsticApp= (LumsticApp) getApplication();

        new FetchSurvey().execute();



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.active_survey, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            Intent i = new Intent(ActiveSurveyActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_help) {

        }
        return super.onOptionsItemSelected(item);
    }
    class FetchSurvey extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {

            fetchUrl=fetchUrl+lumsticApp.getPreferences().getAccessToken();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet= new HttpGet(fetchUrl);
                HttpResponse httpResponse = httpclient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                jsonFetchString = EntityUtils.toString(httpEntity);
                Log.e("fetchstring",jsonFetchString);


            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            return jsonFetchString;
        }

        @Override
        protected void onPostExecute(String s) {
            jsonHelper = new JsonHelper(ActiveSurveyActivity.this);
            surveysList = new ArrayList<Surveys>();
            surveysList = jsonHelper.tryParsing(jsonHelper.getStringFromJson());
           // surveysList = jsonHelper.tryParsing(s);
            listView = (ListView) findViewById(R.id.active_survey_list);
            dashBoardAdapter = new DashBoardAdapter(getApplicationContext(), surveysList);
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
}
