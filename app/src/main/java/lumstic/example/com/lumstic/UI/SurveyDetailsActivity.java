package lumstic.example.com.lumstic.UI;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;
public class SurveyDetailsActivity extends Activity {

    LinearLayout completeResponsesLinearLayout;
    LinearLayout incompleteResponsesLinearLayout;
    Button addResponsesButton;
    ActionBar actionBar;
    Surveys surveys;
    Button uploadButton;
    TextView surveyTitleText, surveyDescriptionText, endDateText;
    List<Questions> questionsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_details);
        if (getIntent().hasExtra(IntentConstants.SURVEY)) {
            surveys = new Surveys();
            surveys = (Surveys) getIntent().getExtras().getSerializable(IntentConstants.SURVEY);
        }
        actionBar= getActionBar();
        actionBar.setTitle(surveys.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_ic_back);
        actionBar.setDisplayShowTitleEnabled(true);
        questionsList = new ArrayList<Questions>();
        questionsList = surveys.getQuestions();
        uploadButton = (Button)findViewById(R.id.upload_button);
        surveyTitleText = (TextView) findViewById(R.id.survey_title_text);
        surveyDescriptionText = (TextView) findViewById(R.id.survey_description_text);
        endDateText = (TextView) findViewById(R.id.end_date_text);
        surveyTitleText.setText(surveys.getName());
        surveyDescriptionText.setText(surveys.getDescription());
        endDateText.setText(surveys.getExpiryDate());
        addResponsesButton = (Button) findViewById(R.id.add_responses_button);
        incompleteResponsesLinearLayout = (LinearLayout) findViewById(R.id.incomplete_response_container);
        completeResponsesLinearLayout = (LinearLayout) findViewById(R.id.complete_response_container);
        incompleteResponsesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SurveyDetailsActivity.this, IncompleteResponseActivity.class);
                startActivity(i);
            }
        });

        completeResponsesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SurveyDetailsActivity.this, CompleteResponsesActivity.class);
                startActivity(i);
            }
        });

        addResponsesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SurveyDetailsActivity.this, NewResponseActivity.class);
                intent.putExtra(IntentConstants.QUESTIONS, (java.io.Serializable) questionsList);
                startActivity(intent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SurveyDetailsActivity.this, TestActivity.class);
                intent.putExtra(IntentConstants.QUESTIONS, (java.io.Serializable) questionsList);
                startActivity(intent);
            }
        });

    }

    @Override
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
        return super.onOptionsItemSelected(item);
    }

}
