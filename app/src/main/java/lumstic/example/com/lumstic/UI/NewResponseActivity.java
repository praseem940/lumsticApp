package lumstic.example.com.lumstic.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;

public class NewResponseActivity extends Activity {


    List<Questions> questionsList;
    List<String> questions;
    boolean hint = true;
    TextView dateText;
    Spinner spinner;
    RelativeLayout deleteImageRelativeLayout;
    TextView questionTextSingleLine;
    View v, v1;
    Questions currentQuestions;
    List<Questions> nestedQuestionsList;
    boolean currentQuestionChanged=false;
    List<Integer> listOfViews;





    RadioButton button;
    RadioGroup radioGroup;
    Questions qu;

    int questionCount = 0;
    LinearLayout fieldContainer;

    LinearLayout fieldContainerNested;
    LayoutInflater inflater;
    TextView answerText;
    int CAMERA_REQUEST = 1;
    RelativeLayout imageContainer;
    ImageView imageView;
    Uri picUri;
    Bitmap photo = null;
    int PICK_FROM_CAMERA = 1;
    final int PIC_CROP = 2;
    int questionCounter = 0;

    Button nextQuestion, previousQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_response);
        getActionBar().setTitle("New Response Activity");
        fieldContainer = (LinearLayout) findViewById(R.id.field_container);
        fieldContainerNested = (LinearLayout) findViewById(R.id.field_container);
        inflater = getLayoutInflater();
        nestedQuestionsList= new ArrayList<Questions>();
        listOfViews= new ArrayList<Integer>();



        questionsList = new ArrayList<Questions>();
        questionsList = (List<Questions>) getIntent().getExtras().getSerializable(IntentConstants.QUESTIONS);
        questionCount = questionsList.size();
        questions = new ArrayList<String>();

        for (int i = 0; i < questionsList.size(); i++) {
            questions.add(questionsList.get(i).getType());


        }

        Questions currentQuestion = questionsList.get(0);
        addQuestion(currentQuestion);
        buildLayout(currentQuestion);

        nextQuestion = (Button) findViewById(R.id.next_queation);
        previousQuestion = (Button) findViewById(R.id.previous_question);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (questionCounter < questionCount - 1) {
                    nestedQuestionsList.clear();
                    fieldContainer.removeAllViews();
                    questionCounter++;
                    Questions currentQuestion = questionsList.get(questionCounter);
                    addQuestion(currentQuestion);
                    buildLayout(currentQuestion);
                }
            }
        });

        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (questionCounter != 0) {
                    nestedQuestionsList.clear();
                    fieldContainer.removeAllViews();
                    questionCounter--;
                    Questions currentQuestion = questionsList.get(questionCounter);
                    addQuestion(currentQuestion);
                    buildLayout(currentQuestion);
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_response, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void buildLayout(final Questions ques) {
        if (ques.getType().equals("SingleLineQuestion")) {

            fieldContainer.addView(inflater.inflate(R.layout.answer_single_line, null));

            EditText answerSingleLine = (EditText) findViewById(R.id.answer_text);
            checkHint();
        }
        if (ques.getType().contains("MultiLineQuestion")) {
            fieldContainer.addView(inflater.inflate(R.layout.answer_multi_line, null));
            EditText answerMultiLine = (EditText) findViewById(R.id.answer_text);
            checkHint();

        }


        if (ques.getType().contains("MultiChoiceQuestion")) {


            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < ques.getOptions().size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setId(i);
                checkBox.setText(ques.getOptions().get(i).getContent());
                checkBox.setTextSize(20);
                checkBox.setButtonDrawable(R.drawable.custom_checkbox);
                ll.addView(checkBox);
            }
            fieldContainer.addView(ll);
            checkHint();
        }


        if (ques.getType().contains("NumericQuestion")) {

            fieldContainer.addView(inflater.inflate(R.layout.answer_numeric, null));
            EditText answerMultiLine = (EditText) findViewById(R.id.answer_text);
            checkHint();

        }


        if (ques.getType().contains("DateQuestion")) {

            fieldContainer.addView(inflater.inflate(R.layout.answer_date_picker, null));

            dateText = (TextView) findViewById(R.id.answer_text);
            dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(NewResponseActivity.this,
                            new mDateSetListener(), mYear, mMonth, mDay);
                    dialog.show();
                }
            });
        }


        //for radio question
        if (ques.getType().contains("RadioQuestion")) {
            qu = ques;


            radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            for (int i = 0; i < ques.getOptions().size(); i++) {

                button = new RadioButton(this);
                radioGroup.addView(button);
                button.setText(ques.getOptions().get(i).getContent());
                button.setId(ques.getOptions().get(i).getId());
                button.setButtonDrawable(R.drawable.custom_radio_button);
                listOfViews.add(ques.getOptions().get(i).getId());



            }

            fieldContainer.addView(radioGroup);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                   for(int i =0;i<qu.getOptions().size();i++){
                       if((qu.getOptions().get(i).getId()==checkedId)&&(qu.getOptions().get(i).getQuestions()!=null)){
                           Toast.makeText(NewResponseActivity.this,"options available",Toast.LENGTH_LONG).show();
                           if(currentQuestionChanged){
                               addQuestion(currentQuestions);
                               buildLayout(currentQuestions);
                           }
                           if(!currentQuestionChanged){
                               addQuestion(qu.getOptions().get(i).getQuestions());
                               buildLayout(qu.getOptions().get(i).getQuestions());
                               currentQuestionChanged=false;
                           }

                       }
                       if((qu.getOptions().get(i).getId()==checkedId)&&(qu.getOptions().get(i).getQuestions()==null)){
                           Toast.makeText(NewResponseActivity.this,"options not available",Toast.LENGTH_LONG).show();
                       try{
                           for(int k=0;k<nestedQuestionsList.size();k++){
                           if(nestedQuestionsList.get(k).getId()==qu.getOptions().get(i).getQuestionId()){
                               currentQuestions= nestedQuestionsList.get(k+1);
                               currentQuestionChanged=true;
                           }
                       }}catch (Exception e) {
                       }
                           try{
                           for(int ko=0;ko<listOfViews.size();ko++)
                           {
                               if(listOfViews.get(ko)==checkedId){
                                   for(int ok=ko;ok<listOfViews.size();ok++)
                                   {
                                       View myView = findViewById(listOfViews.get(ko));
                                       ViewGroup parent = (ViewGroup) myView.getParent();
                                       parent.removeView(myView);
                                   }
                               }
                           }
                       }catch (Exception e){
                       e.printStackTrace();
                       }
                       }
                   }
                             }



            });
        }
        //for dropdown question
        if (ques.getType().contains("DropDownQuestion")) {

            fieldContainer.addView(inflater.inflate(R.layout.answer_dropdown, null));

            spinner = (Spinner) findViewById(R.id.drop_down);
            List<String> listOptions = new ArrayList<String>();
            for (int i = 0; i < ques.getOptions().size(); i++) {
                listOptions.add(ques.getOptions().get(i).getContent());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, listOptions);
            spinner.setAdapter(dataAdapter);
        }
        if (ques.getType().equals("RatingQuestion")) {
            fieldContainer.addView(questionTextSingleLine);
            RatingBar ratingBar = new RatingBar(this);
            ratingBar.setNumStars(5);
            fieldContainer.addView(ratingBar);
            checkHint();
        }

        //for image question
        if (ques.getType().equals("PhotoQuestion")) {
            fieldContainer.addView(inflater.inflate(R.layout.answer_image_picker, null));

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.answer_text);

            deleteImageRelativeLayout = (RelativeLayout) findViewById(R.id.image_container);

            imageView = (ImageView) findViewById(R.id.image);
            imageContainer = (RelativeLayout) findViewById(R.id.image_container);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });


            deleteImageRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageContainer.setVisibility(View.GONE);
                }
            });
        }

    }

    public void checkHint() {

        if (hint) {

            fieldContainer.addView(inflater.inflate(R.layout.hint_helper, null));
            final RelativeLayout hintContainer = (RelativeLayout) findViewById(R.id.hint_container);
            LinearLayout hintButtonContainer = (LinearLayout) findViewById(R.id.hint_buttons_container);
            final Button textHintButton = (Button) findViewById(R.id.text_hint_button);
            final Button imageHintButton = (Button) findViewById(R.id.image_hint_button);
            hintButtonContainer.setVisibility(View.VISIBLE);
            textHintButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintContainer.setVisibility(View.VISIBLE);
                    TextView textHint = (TextView) findViewById(R.id.text_hint);
                    ImageView imageHint = (ImageView) findViewById(R.id.image_hint);
                    textHint.setVisibility(View.VISIBLE);
                    textHintButton.setBackgroundResource(R.drawable.hint_button_pressed);
                    imageHintButton.setBackgroundResource(R.drawable.hint_button);
                    imageHint.setVisibility(View.GONE);
                }
            });
            imageHintButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintContainer.setVisibility(View.VISIBLE);
                    imageHintButton.setBackgroundResource(R.drawable.hint_button_pressed);
                    textHintButton.setBackgroundResource(R.drawable.hint_button);
                    TextView textHint = (TextView) findViewById(R.id.text_hint);
                    ImageView imageHint = (ImageView) findViewById(R.id.image_hint);
                    textHint.setVisibility(View.GONE);
                    imageHint.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            dateText.setText(new StringBuilder().append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" ").toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            photo = (Bitmap) data.getExtras().get("data");

            deleteImageRelativeLayout.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(photo);
        }

    }

    public int addQuestion(Questions question) {
        nestedQuestionsList.add(question);
        TextView questionTextSingleLine = new TextView(this);
        questionTextSingleLine.setId(question.getId());
        questionTextSingleLine.setTextSize(20);
        questionTextSingleLine.setTextColor(Color.BLACK);
        questionTextSingleLine.setPadding(8, 12, 8, 20);
        questionTextSingleLine.setText("Q" + question.getOrderNumber() + ")" + "   " + question.getContent());
        fieldContainer.addView(questionTextSingleLine);
        listOfViews.add(questionTextSingleLine.getId());
        return question.getId();
    }

}

