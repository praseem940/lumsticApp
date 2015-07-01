package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Adapters.DBAdapter;
import lumstic.example.com.lumstic.Adapters.IncompleteResponsesAdapter;
import lumstic.example.com.lumstic.Models.IncompleteResponses;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;

public class IncompleteResponseActivity extends Activity {

    private IncompleteResponsesAdapter incompleteResponsesAdapter;
    private DBAdapter dbAdapter;
    private ActionBar actionBar;

    private Surveys surveys;
    private Questions identifierQuestion;

    private ListView listView;
    private TextView responseCount;
    private TextView surveyTitle;

    private int incompleteResponseCount = 0;
    private int identifierQuestionId = 0;

    private List<IncompleteResponses> incompleteResponseses;
    private List<Integer> incompleteResponsesId;
    private List<String> identifierQuestionAnswers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete_response);

        //setting up action bar
        actionBar = getActionBar();
        actionBar.setTitle("Incomplete Responses");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_action_ic_back);
        actionBar.setDisplayShowTitleEnabled(true);
        dbAdapter = new DBAdapter(IncompleteResponseActivity.this);

        //initialize lists
        incompleteResponseses = new ArrayList<IncompleteResponses>();
        incompleteResponsesId = new ArrayList<Integer>();
        identifierQuestionAnswers = new ArrayList<String>();
        surveys = new Surveys();

        //defining views
        responseCount = (TextView) findViewById(R.id.incomplete_response_count);
        surveyTitle = (TextView) findViewById(R.id.survey_title_text);


        surveys = (Surveys) getIntent().getExtras().getSerializable(IntentConstants.SURVEY);
        //get counts
        incompleteResponseCount = dbAdapter.getIncompleteResponse(surveys.getId());
        incompleteResponsesId = dbAdapter.getIncompleteResponsesIds(surveys.getId());
        surveyTitle.setText(surveys.getName());
        responseCount.setText(incompleteResponseCount + "");
        for (int j = 0; j < surveys.getQuestions().size(); j++) {
            if (surveys.getQuestions().get(j).getIdentifier() == 1) {
                identifierQuestion = surveys.getQuestions().get(j);
                identifierQuestionId = surveys.getQuestions().get(j).getId();
            }
        }

        for (int i = 0; i < incompleteResponseCount; i++) {
            identifierQuestionAnswers.add(dbAdapter.getAnswer(incompleteResponsesId.get(i), identifierQuestionId));
            try {
                incompleteResponseses.add(i, new IncompleteResponses(String.valueOf(incompleteResponsesId.get(i)), identifierQuestion.getContent() + " :" + "  " + identifierQuestionAnswers.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listView = (ListView) findViewById(R.id.listview);
        incompleteResponsesAdapter = new IncompleteResponsesAdapter(getApplicationContext(), incompleteResponseses, surveys);
        listView.setAdapter(incompleteResponsesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.incomplete_response, menu);
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
}
