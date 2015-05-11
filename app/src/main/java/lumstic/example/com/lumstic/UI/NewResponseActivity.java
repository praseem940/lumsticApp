package lumstic.example.com.lumstic.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;

public class NewResponseActivity extends Activity {


    List<Questions> questionsList;
    List<String> questions;
    boolean hint= true;
    TextView dateText;
    Spinner spinner;

    RadioButton button;
    RadioGroup radioGroup;
    TableRow row;
    LinearLayout fieldContainer;
    LayoutInflater inflater;
    TextView answerText;
    RelativeLayout imageContainer;
    ImageView imageView;
    Uri picUri;
    int CAMERA_REQUEST =1;
    final int PIC_CROP = 2;
    int questionCounter = 0;
    Button nextQuestion, previousQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_response);
        getActionBar().setTitle("New Response Activity");
        fieldContainer=(LinearLayout)findViewById(R.id.field_container);
        inflater = getLayoutInflater();

        questionsList = new ArrayList<Questions>();
        questionsList = (List<Questions>) getIntent().getExtras().getSerializable(IntentConstants.QUESTIONS);
        questions = new ArrayList<String>();
        for(int i=0;i<questionsList.size();i++){
            questions.add(questionsList.get(i).getType());


        }

        Questions currentQuestion=questionsList.get(0);
        buildLayout(currentQuestion);

//        nextQuestion = (Button) findViewById(R.id.next_queation);
//        previousQuestion = (Button) findViewById(R.id.previous_question);
//        questionText.setText(questions.get(0));
//        currentQuestions = questionsList.get(0);
//        fieldContainer.removeAllViews();
//        inflater = getLayoutInflater();
//        fieldContainer.addView(inflater.inflate(layouts.get(0), null));
//
//        //for next button click
//        nextQuestion.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                questionCounter++;
//                currentQuestions = questionsList.get(questionCounter);
//                fieldContainer.removeAllViews();
//                fieldContainer.addView(inflater.inflate(layouts.get(questionCounter), null));
//                questionText.setText(questions.get(questionCounter));
//                CheckQuestion(layouts.get(questionCounter), currentQuestions);
//            }
//        });
//
//        //for previous button click
//        previousQuestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                questionCounter--;
//                currentQuestions = questionsList.get(questionCounter);
//                fieldContainer.removeAllViews();
//                fieldContainer.addView(inflater.inflate(layouts.get(questionCounter), null));
//                questionText.setText(questions.get(questionCounter));
//                CheckQuestion(layouts.get(questionCounter), currentQuestions);
//            }
//        });

    }

    public void CheckQuestion(int layoutId, final Questions currentQuestions) {


        //for image question
        if (layoutId == R.layout.answer_image_picker) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.answer_text);
            RelativeLayout deleteImageRelativeLayout = (RelativeLayout) findViewById(R.id.image_container);
            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageContainer = (RelativeLayout) findViewById(R.id.image_container);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                imageView.setImageBitmap(thePic);
            }
        }
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


    public void buildLayout(final Questions ques){
        if(ques.getType().equals("SingleLineQuestion"))
        {
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextSingleLine = (TextView) findViewById(R.id.question_text);
            questionTextSingleLine.setText(ques.getContent());
            TextView questionNumberSingleLine = (TextView) findViewById(R.id.number);
            questionNumberSingleLine.setText("Q" + ques.getOrderNumber() + ")");
            fieldContainer.addView(inflater.inflate(R.layout.answer_single_line, null));
            EditText answerSingleLine = (EditText) findViewById(R.id.answer_text);
            checkHint();
        }
            if (ques.getType().contains("MultiLineQuestion"))
            {
                fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
                TextView questionTextMultiLine = (TextView) findViewById(R.id.question_text);
                questionTextMultiLine.setText(ques.getContent());
                TextView questionNumberMultiLine = (TextView) findViewById(R.id.number);
                questionNumberMultiLine.setText("Q" + ques.getOrderNumber() + ")");
                fieldContainer.addView(inflater.inflate(R.layout.answer_multi_line, null));
                EditText answerMultiLine = (EditText) findViewById(R.id.answer_text);
                checkHint();

            }


        if (ques.getType().contains("MultiChoiceQuestion")) {
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextMultiLine = (TextView) findViewById(R.id.question_text);
            questionTextMultiLine.setText(ques.getContent());
            TextView questionNumberMultiLine = (TextView) findViewById(R.id.number);
            questionNumberMultiLine.setText("Q" + ques.getOrderNumber() + ")");

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


        if (ques.getType().contains("NumericQuestion"))
        {
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextMultiLine = (TextView) findViewById(R.id.question_text);
            questionTextMultiLine.setText(ques.getContent());
            TextView questionNumberMultiLine = (TextView) findViewById(R.id.number);
            questionNumberMultiLine.setText("Q" + ques.getOrderNumber() + ")");
            fieldContainer.addView(inflater.inflate(R.layout.answer_numeric, null));
            EditText answerMultiLine = (EditText) findViewById(R.id.answer_text);
            checkHint();

        }

        if (ques.getType().contains("DateQuestion")) {
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextMultiLine = (TextView) findViewById(R.id.question_text);
            questionTextMultiLine.setText(ques.getContent());
            TextView questionNumberMultiLine = (TextView) findViewById(R.id.number);
            questionNumberMultiLine.setText("Q" + ques.getOrderNumber() + ")");
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
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextMultiLine = (TextView) findViewById(R.id.question_text);
            questionTextMultiLine.setText(ques.getContent());
            TextView questionNumberMultiLine = (TextView) findViewById(R.id.number);
            questionNumberMultiLine.setText("Q" + ques.getOrderNumber() + ")");


            fieldContainer.addView(inflater.inflate(R.layout.answer_radio_button,null));
            radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            for (int i = 0; i < ques.getOptions().size(); i++) {

                button = new RadioButton(this);
                radioGroup.addView(button);
                button.setText(ques.getOptions().get(i).getContent());
                button.setId(i);
                button.setButtonDrawable(R.drawable.custom_radio_button);

            }
            fieldContainer.addView(radioGroup);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(ques.getOptions().get(checkedId).getQuestions()!=null){
                        Toast.makeText(NewResponseActivity.this,"this has options",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        //for dropdown question
        if (ques.getType().contains("DropDownQuestion")) {
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextMultiLine = (TextView) findViewById(R.id.question_text);
            questionTextMultiLine.setText(ques.getContent());
            TextView questionNumberMultiLine = (TextView) findViewById(R.id.number);
            questionNumberMultiLine.setText("Q" + ques.getOrderNumber() + ")");
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
        if(ques.getType().equals("RatingQuestion"))
        {
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextSingleLine = (TextView) findViewById(R.id.question_text);
            questionTextSingleLine.setText(ques.getContent());
            TextView questionNumberSingleLine = (TextView) findViewById(R.id.number);
            questionNumberSingleLine.setText("Q" + ques.getOrderNumber() + ")");
            RatingBar ratingBar = new RatingBar(this);
            ratingBar.setNumStars(5);
            fieldContainer.addView(ratingBar);
            checkHint();
        }

        //for image question
        if (ques.getType().equals("PhotoQuestion")) {
            fieldContainer.addView(inflater.inflate(R.layout.question_text, null));
            TextView questionTextSingleLine = (TextView) findViewById(R.id.question_text);
            questionTextSingleLine.setText(ques.getContent());
            TextView questionNumberSingleLine = (TextView) findViewById(R.id.number);
            questionNumberSingleLine.setText("Q" + ques.getOrderNumber() + ")");
            fieldContainer.addView(inflater.inflate(R.layout.answer_image_picker, null));

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.answer_text);
            RelativeLayout deleteImageRelativeLayout = (RelativeLayout) findViewById(R.id.image_container);
            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageContainer = (RelativeLayout) findViewById(R.id.image_container);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
    public void checkHint(){

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

}

