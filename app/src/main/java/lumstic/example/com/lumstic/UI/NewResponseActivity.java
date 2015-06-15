package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import lumstic.example.com.lumstic.Adapters.DBAdapter;
import lumstic.example.com.lumstic.Models.Answers;
import lumstic.example.com.lumstic.Models.Categories;
import lumstic.example.com.lumstic.Models.Choices;
import lumstic.example.com.lumstic.Models.Options;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;

public class NewResponseActivity extends Activity {


    final int PIC_CROP = 2;
    List<Questions> questionsList;
    List<Categories> categoriesList;
    boolean hint = true;
    TextView dateText;
    Spinner spinner;
    Surveys surveys;
    Long tsLong =null;
    RelativeLayout deleteImageRelativeLayout;

    List<Questions> nestedQuestions;

    EditText answer;
    Categories currentCategory;
    Button counterButton, markAsComplete;
    String htmlStringWithMathSymbols = "&#60";
    ActionBar actionBar;
    DBAdapter dbAdapter;
    int currentResponseId = 0;
    Questions universalQuestion;
    String fname = "";
    int categoryAndQuestionCount = 0;
    int totalQuestionCount = 0;
    Questions dateQuestion;


    int questionCount = 0;
    int categoryCount = 0;
    LinearLayout fieldContainer;
    LayoutInflater inflater;
    int CAMERA_REQUEST = 1;
    RelativeLayout imageContainer;
    ImageView imageViewPhotoQuestion;

    Bitmap photo = null;
    int PICK_FROM_CAMERA = 1;
    int questionCounter = 0;
    List<Integer> idList;
    RatingBar ratingBar;
    int questionOrderCounter = 0;
    List<Object> objects;

    ArrayList<Integer> types = null;

    ArrayList<String> stringTypes = null;

    Button nextQuestion, previousQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_response);
        actionBar = getActionBar();
        actionBar.setTitle("New Response Activity");
        actionBar.setDisplayHomeAsUpEnabled(true);
//      actionBar.setHomeAsUpIndicator(R.drawable.ic_action_ic_back);
        actionBar.setDisplayShowTitleEnabled(true);
        fieldContainer = (LinearLayout) findViewById(R.id.field_container);
        inflater = getLayoutInflater();
        nestedQuestions = new ArrayList<Questions>();
        idList = new ArrayList<Integer>();
        counterButton = (Button) findViewById(R.id.counter_button);

        dbAdapter = new DBAdapter(NewResponseActivity.this);
        questionsList = new ArrayList<Questions>();
        categoriesList = new ArrayList<Categories>();
        objects = new ArrayList<Object>();
        markAsComplete = new Button(this);

        surveys = (Surveys) getIntent().getExtras().getSerializable(IntentConstants.SURVEY);


        if (getIntent().hasExtra(IntentConstants.RESPONSE_ID)) {
            currentResponseId = getIntent().getIntExtra(IntentConstants.RESPONSE_ID, 0);
        }
        if (!getIntent().hasExtra(IntentConstants.RESPONSE_ID)) {
            currentResponseId = (int) dbAdapter.getMaxID();
        }

        if (surveys.getQuestions().size() > 0) {
            questionsList = surveys.getQuestions();
            questionCount = questionsList.size();
        }

        categoryAndQuestionCount = questionCount + categoryCount;

        if (surveys.getCategories().size() > 0) {
            categoriesList = surveys.getCategories();
            categoryCount = categoriesList.size();
        }


        totalQuestionCount = categoryCount + questionCount;

        stringTypes = new ArrayList<String>();
        types = new ArrayList<Integer>();



        int i = 0;
        for (int count = 0; count < 100; count++) {
            for (i = 0; i < questionsList.size(); i++) {
                if (questionsList.get(i).getOrderNumber() == count) {
                    types.add(count);
                    stringTypes.add("question");
                }


            }
            for (i = 0; i < categoriesList.size(); i++) {
                if (categoriesList.get(i).getOrderNumber() == count) {
                    types.add(count);
                    stringTypes.add("category");
                }


            }
        }



        for (int j = 0; j < questionsList.size(); j++) {
            if (questionsList.get(j).getOrderNumber() == types.get(0)) {
                Questions cq = questionsList.get(j);
//                questionCounter++;
                counterButton.setText("1 out of " + totalQuestionCount);
                buildLayout(cq);
                checkForAnswer(cq, currentResponseId);
                break;
            }
        }
        for (int j = 0; j < categoriesList.size(); j++) {
            if (categoriesList.get(j).getOrderNumber() == types.get(0)) {

                counterButton.setText("1 out of " + totalQuestionCount);
//                questionCounter++;


                Categories currentCategory = categoriesList.get(j);


                buildCategoryLayout(currentCategory);


                for (int k = 0; k < currentCategory.getQuestionsList().size(); k++) {
                    checkForAnswer(currentCategory.getQuestionsList().get(k), currentResponseId);

                }
                break;
            }
        }

        nextQuestion = (Button) findViewById(R.id.next_queation);
        previousQuestion = (Button) findViewById(R.id.previous_question);
        previousQuestion.setText("BACK");

        if (questionCounter + 1 == totalQuestionCount) {

            createMarkAsComplete();
            fieldContainer.addView(markAsComplete);


            nextQuestion.setTextColor(getResources().getColor(R.color.back_button_text));
            nextQuestion.setText("NEXT");
            nextQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_next_disable, 0);

            nextQuestion.setBackgroundColor(getResources().getColor(R.color.back_button_background));
        }

        markAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!universalQuestion.getType().equals("PhotoQuestion"))

                    if (!universalQuestion.getType().equals("PhotoQuestion")) {
                        if(questionCounter==totalQuestionCount-1){   addAnswer(universalQuestion);
                        }}


                Toast.makeText(NewResponseActivity.this, "Response saved with ID" + dbAdapter.UpldateCompleteResponse(currentResponseId, questionsList.get(0).getSurveyId()) + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewResponseActivity.this, SurveyDetailsActivity.class);
                intent.putExtra(IntentConstants.SURVEY, (java.io.Serializable) surveys);
                startActivity(intent);
                finish();
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                onNextClick();


            }
        });

        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackClicked();
            }
        });


    }


    public void buildCategoryLayout(Categories categories) {
        setCategoryTitle(categories);
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
        if (id == R.id.save) {


            return true;

        }
        if (id == android.R.id.home) {

            finish();

            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void buildLayout(final Questions ques) {
        universalQuestion = ques;
        if (ques.getType().equals("SingleLineQuestion")) {
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_single_line, null));

            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
            answer = (EditText) findViewById(R.id.answer_text);
            answer.setId(ques.getId());
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);
            answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                    if (!b) {

                        Answers answers = new Answers();
                        answers.setQuestion_id(ques.getId());
                        answers.setResponseId(currentResponseId);
                        tsLong= System.currentTimeMillis() / 1000;

                        answers.setUpdated_at(tsLong);
                        answers.setContent(answer.getText().toString());
                        long x = dbAdapter.insertDataAnswersTable(answers);
                        Toast.makeText(NewResponseActivity.this, ques.getId() + "answer is saved", Toast.LENGTH_LONG).show();

                }}
            });


            try {
                checkForAnswer(ques, currentResponseId);
            } catch (Exception e) {
                e.printStackTrace();
                checkHint();
            }
        }


        if (ques.getType().contains("MultiLineQuestion")) {

            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_multi_line, null));

            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
            answer = (EditText) findViewById(R.id.answer_text);
            answer.setId(ques.getId());

            answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                    if (!b) {

                        Answers answers = new Answers();
                        answers.setQuestion_id(ques.getId());
                        answers.setResponseId(currentResponseId);
                        tsLong= System.currentTimeMillis() / 1000;
                        answers.setUpdated_at(tsLong);
                        answers.setContent(answer.getText().toString());
                        long x = dbAdapter.insertDataAnswersTable(answers);
                        Toast.makeText(NewResponseActivity.this, ques.getId() + "answer is saved", Toast.LENGTH_LONG).show();

                    }}
            });


            try {
                checkForAnswer(ques, currentResponseId);
            } catch (Exception e) {
                e.printStackTrace();
                checkHint();
            }
            checkHint();

        }

        if (ques.getType().contains("DropDownQuestion")) {


            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_dropdown, null));

            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);

            spinner = (Spinner) findViewById(R.id.drop_down);
            List<String> listOptions = new ArrayList<String>();
            for (int i = 0; i < ques.getOptions().size(); i++) {
                listOptions.add(ques.getOptions().get(i).getContent());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, listOptions);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    //Toast.makeText(NewResponseActivity.this,""+i,Toast.LENGTH_SHORT).show();
                    Options options = ques.getOptions().get(i);
                    addOptionToDataBase(options, ques);
                    removeOthersFromDataBase(options, ques);

                    if (options.getQuestions().size() > 0) {
                        //  Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                        for (int j = 0; j < options.getQuestions().size(); j++) {
                            buildLayout(options.getQuestions().get(j));
                            checkForAnswer(options.getQuestions().get(j), currentResponseId);
                        }
                    }

                    if (options.getQuestions().size() <= 0) {

                    }
                    if (options.getQuestions().size() <= 0) {

                        for (int j = 0; j < ques.getOptions().size(); j++) {

                            if (!ques.getOptions().get(j).getContent().equals(options.getContent())) {


                                removeQuestionView(ques.getOptions().get(j));
                                removeCategoryView(ques.getOptions().get(j));

                            }

                        }

                    }

                    if (options.getCategories().size() > 0) {
                        setCategoryTitle(options);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


        if (ques.getType().contains("MultiChoiceQuestion")) {


            nestedQuestions.add(ques);


            Log.e("nestedquestionitem", nestedQuestions.size() + "");

            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);

            nestedContainer.setTag(ques);
            idList.add(ques.getId());

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            nestedContainer.addView(ll);
            for (int i = 0; i < ques.getOptions().size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                ll.addView(checkBox);
                checkBox.setId(ques.getOptions().get(i).getId());
                checkBox.setText(ques.getOptions().get(i).getContent());
                checkBox.setTextSize(16);
                checkBox.setTextColor(getResources().getColor(R.color.text_color));
                checkBox.setTag(ques.getOptions().get(i));
                checkBox.setButtonDrawable(R.drawable.custom_checkbox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((CheckBox) view).isChecked()) {

                            CheckBox checkBox1 = (CheckBox) view;
                            Options options = (Options) checkBox1.getTag();

                            addOptionToDataBase(options, ques);


                            if (options.getQuestions().size() > 0) {
                                //        Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < options.getQuestions().size(); i++) {
                                    buildLayout(options.getQuestions().get(i));
                                    checkForAnswer(options.getQuestions().get(i), currentResponseId);
                                }
                            }

                            if (options.getCategories().size() > 0) {
                                setCategoryTitle(options);
                            }
                        }

                        if (!((CheckBox) view).isChecked()) {


                            //  Toast.makeText(NewResponseActivity.this,"has been unchecked ",Toast.LENGTH_SHORT).show();
                            CheckBox checkBox1 = (CheckBox) view;
                            Options options = (Options) checkBox1.getTag();


                            removeOptionFromDataBase(options, ques);

                            if (options.getQuestions().size() > 0) {
                                removeQuestionView(options);
                            }

                            if (options.getCategories().size() > 0) {
                                removeCategoryView(options);
                            }
                        }

                    }
                });


            }
            fieldContainer.addView(nestedContainer);
            checkHint();
        }


        if (ques.getType().contains("NumericQuestion")) {
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());

            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_numeric, null));

            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
            answer = (EditText) findViewById(R.id.answer_text);
            answer.setId(ques.getId());
            answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                    if (!b) {

                        Answers answers = new Answers();
                        answers.setQuestion_id(ques.getId());
                        tsLong= System.currentTimeMillis() / 1000;
                        answers.setUpdated_at(tsLong);
                        answers.setResponseId(currentResponseId);
                        answers.setContent(answer.getText().toString());
                        long x = dbAdapter.insertDataAnswersTable(answers);
                        Toast.makeText(NewResponseActivity.this, ques.getId() + "answer is saved", Toast.LENGTH_LONG).show();

                    }}
            });
            try {
                checkForAnswer(ques, currentResponseId);
            } catch (Exception e) {
                e.printStackTrace();
                checkHint();
            }

            checkHint();

        }


        if (ques.getType().contains("DateQuestion")) {
            dateQuestion=ques;
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());

            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_date_picker, null));

            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);


            dateText = (TextView) findViewById(R.id.answer_text_date);
            dateText.setId(ques.getId());
            dateText.setText("dd.yy.mm");
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




            try {
                checkForAnswer(ques, currentResponseId);
            } catch (Exception e) {
                e.printStackTrace();
                checkHint();
            }
        }


        //for radio question
        if (ques.getType().contains("RadioQuestion")) {


            nestedQuestions.add(ques);

            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);

            nestedContainer.setTag(ques);
            idList.add(ques.getId());


            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            nestedContainer.addView(radioGroup);
            for (int i = 0; i < ques.getOptions().size(); i++) {
                final RadioButton radioButton = new RadioButton(this);
                radioGroup.addView(radioButton);
                radioButton.setId(ques.getOptions().get(i).getId());
                radioButton.setTextSize(16);
                radioButton.setTextColor(getResources().getColor(R.color.text_color));
                radioButton.setText(ques.getOptions().get(i).getContent());
                radioButton.setTag(ques.getOptions().get(i));
                radioButton.setButtonDrawable(R.drawable.custom_radio_button);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        View myView = findViewById(checkedId);
                        RadioButton radioButton1 = (RadioButton) myView;
                        Options options = (Options) radioButton1.getTag();


                        addOptionToDataBase(options, ques);
                        removeOthersFromDataBase(options, ques);


                        //Toast.makeText(NewResponseActivity.this,options.getId()+"",Toast.LENGTH_SHORT).show();
                        if (options.getQuestions().size() > 0) {
                            //  Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < options.getQuestions().size(); i++) {
                                buildLayout(options.getQuestions().get(i));
                                checkForAnswer(options.getQuestions().get(i), currentResponseId);
                            }
                        }
                        if (options.getQuestions().size() <= 0) {
                            for (int i = 0; i < ques.getOptions().size(); i++) {
                                if (!ques.getOptions().get(i).getContent().equals(options.getContent())) {
                                    removeQuestionView(ques.getOptions().get(i));
                                    removeCategoryView(ques.getOptions().get(i));
                                }

                            }

                        }

                    }
                });

            }
            fieldContainer.addView(nestedContainer);
            checkHint();

        }
        if (ques.getType().equals("RatingQuestion")) {
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_rating, null));


            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
            ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            ratingBar.setId(ques.getId());
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    Answers answers = new Answers();
                    answers.setResponseId((int) dbAdapter.getMaxID());
                    answers.setQuestion_id(ques.getId());
                    tsLong= System.currentTimeMillis() / 1000;
                    answers.setUpdated_at(tsLong);
                    answers.setContent(String.valueOf(v));
                    long x = dbAdapter.insertDataAnswersTable(answers);
                }
            });


            checkHint();
        }

        //for image question
        if (ques.getType().equals("PhotoQuestion")) {
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0, 0, 0, 16);
            questionTextSingleLine.setText("Q. " + (questionCounter + 1) + "   " + ques.getContent());
            if (ques.getParentId() > 0)
                questionTextSingleLine.setText("Q. " + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);


            nestedContainer.addView(inflater.inflate(R.layout.answer_image_picker, null));
            fieldContainer.addView(nestedContainer);

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.answer_text_image);

            deleteImageRelativeLayout = (RelativeLayout) findViewById(R.id.image_container);

            imageViewPhotoQuestion = (ImageView) findViewById(R.id.image);
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
                    dbAdapter.deleteImagePath(currentResponseId, ques.getId());

                }
            });
        }
    }

    public void checkHint() {

//        if (hint) {
//
//            fieldContainer.addView(inflater.inflate(R.layout.hint_helper, null));
//            final RelativeLayout hintContainer = (RelativeLayout) findViewById(R.id.hint_container);
//            LinearLayout hintButtonContainer = (LinearLayout) findViewById(R.id.hint_buttons_container);
//            final Button textHintButton = (Button) findViewById(R.id.text_hint_button);
//            final Button imageHintButton = (Button) findViewById(R.id.image_hint_button);
//            hintButtonContainer.setVisibility(View.VISIBLE);
//            textHintButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    hintContainer.setVisibility(View.VISIBLE);
//                    TextView textHint = (TextView) findViewById(R.id.text_hint);
//                    ImageView imageHint = (ImageView) findViewById(R.id.image_hint);
//                    textHint.setVisibility(View.VISIBLE);
//                    textHintButton.setBackgroundResource(R.drawable.hint_button_pressed);
//                    imageHintButton.setBackgroundResource(R.drawable.hint_button);
//                    imageHint.setVisibility(View.GONE);
//                }
//            });
//            imageHintButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    hintContainer.setVisibility(View.VISIBLE);
//                    imageHintButton.setBackgroundResource(R.drawable.hint_button_pressed);
//                    textHintButton.setBackgroundResource(R.drawable.hint_button);
//                    TextView textHint = (TextView) findViewById(R.id.text_hint);
//                    ImageView imageHint = (ImageView) findViewById(R.id.image_hint);
//                    textHint.setVisibility(View.GONE);
//                    imageHint.setVisibility(View.VISIBLE);
//                }
//            });
//        }
    }


    public void onNextClick() {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (questionsList.get(questionCounter).getMandatory() == 1) {
//
//            if (answer.getText().toString().equals("")) {
//                final Dialog dialog = new Dialog(NewResponseActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
//                dialog.setContentView(R.layout.mandatory_question_dialog);
//                dialog.show();
//                Button button = (Button) dialog.findViewById(R.id.okay);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }


        if (questionCounter < totalQuestionCount - 1) {
            previousQuestion.setBackgroundColor(getResources().getColor(R.color.login_button_color));
            previousQuestion.setTextColor(getResources().getColor(R.color.white));
            previousQuestion.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back, 0, 0, 0);
            nestedQuestions.clear();
            nestedQuestions.clear();
            idList.clear();
            fieldContainer.removeAllViews();


            if (!universalQuestion.getType().equals("PhotoQuestion")) {
                if(questionCounter==totalQuestionCount-1){   addAnswer(universalQuestion);
                }}

            questionCounter++;
            counterButton.setText(questionCounter + 1 + " out of " + totalQuestionCount);

            for (int j = 0; j < categoriesList.size(); j++) {
                if (categoriesList.get(j).getOrderNumber() == types.get(questionCounter)) {


                    currentCategory = categoriesList.get(j);


//                    if (currentCategory.getType().equals("MultiRecordCategory")) {
//                        Button addRecord = new Button(this);
//                        addRecord.setBackgroundResource(R.drawable.custom_button);
//                        addRecord.setText("+  Add Record");
//                        addRecord.setTextColor(getResources().getColor(R.color.white));
//                        fieldContainer.addView(addRecord);
//                        addRecord.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                buildCategoryLayout(currentCategory);
//                                for (int k = 0; k < currentCategory.getQuestionsList().size(); k++) {
//                                    checkForAnswer(currentCategory.getQuestionsList().get(k), currentResponseId);
//                                }
//                            }
//                        });
//                    }
                    buildCategoryLayout(currentCategory);
//                    for (int k = 0; k < currentCategory.getQuestionsList().size(); k++) {
//                        checkForAnswer(currentCategory.getQuestionsList().get(k), currentResponseId);
//
//                    }

                }
            }

            for (int j = 0; j < questionsList.size(); j++) {
                if (questionsList.get(j).getOrderNumber() == types.get(questionCounter)) {
                    Questions cq = questionsList.get(j);
                    buildLayout(cq);
                    checkForAnswer(cq, currentResponseId);
                    break;
                }
            }

            if (questionCounter + 1 == totalQuestionCount) {

                createMarkAsComplete();
                markAsComplete.setVisibility(View.VISIBLE);
                nextQuestion.setTextColor(getResources().getColor(R.color.back_button_text));
                nextQuestion.setText("NEXT");
                nextQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_next_disable, 0);
                nextQuestion.setBackgroundColor(getResources().getColor(R.color.back_button_background));
                fieldContainer.addView(markAsComplete);
            }
        }

        if (questionCounter != 0) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
        }
    }


    public void createMarkAsComplete() {


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 5;
        params.topMargin = 10;


        markAsComplete.setBackgroundResource(R.drawable.custom_button);
        markAsComplete.setText("mark as complete");
        markAsComplete.setGravity(Gravity.CENTER_HORIZONTAL);
        markAsComplete.setTextColor(getResources().getColor(R.color.white));
        markAsComplete.setLayoutParams(params);

    }


    public void onBackClicked() {
        markAsComplete.setVisibility(View.GONE);

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (questionCounter != 0) {
            nestedQuestions.clear();
            idList.clear();
            fieldContainer.removeAllViews();

            if (!universalQuestion.getType().equals("PhotoQuestion")) {
             if(questionCounter==totalQuestionCount-1){   addAnswer(universalQuestion);
            }}

            questionCounter--;
            counterButton.setText(questionCounter + 1 + " out of " + totalQuestionCount);
            for (int j = 0; j < categoriesList.size(); j++) {
                if (categoriesList.get(j).getOrderNumber() == types.get(questionCounter)) {


                    Categories currentCategory = categoriesList.get(j);
                    buildCategoryLayout(currentCategory);
//                    for (int k = 0; k < currentCategory.getQuestionsList().size(); k++) {
//                        checkForAnswer(currentCategory.getQuestionsList().get(k), currentResponseId);
//
//                    }
                    break;
                }
            }

            for (int j = 0; j < questionsList.size(); j++) {
                if (questionsList.get(j).getOrderNumber() == types.get(questionCounter)) {
                    Questions cq = questionsList.get(j);
                    buildLayout(cq);
                    checkForAnswer(cq, currentResponseId);
                    break;
                }
            }

            if (questionCounter == 0) {
                previousQuestion.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back_enable, 0, 0, 0);
                previousQuestion.setTextColor(getResources().getColor(R.color.back_button_text));
                previousQuestion.setBackgroundColor(getResources().getColor(R.color.back_button_background));
            }


            if (questionCounter + 1 != totalQuestionCount) {

                nextQuestion.setTextColor(getResources().getColor(R.color.white));
                nextQuestion.setText("NEXT");
                nextQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_next, 0);
                nextQuestion.setBackgroundColor(getResources().getColor(R.color.login_button_color));
            }


        }
        if (questionCounter == 0) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            photo = (Bitmap) data.getExtras().get("data");

            deleteImageRelativeLayout.setVisibility(View.VISIBLE);
            imageViewPhotoQuestion.setImageBitmap(photo);
            SaveImage(photo);
            addAnswer(universalQuestion);

        }
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadImageFromStorage(String path, String fileName) {

        try {
            File f = new File(path, fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            imageViewPhotoQuestion.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void setCategoryTitle(Options options) {


        for (int i = 0; i < options.getCategories().size(); i++) {
            Categories categories = options.getCategories().get(i);
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("" + categories.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.setId(categories.getId());
            nestedContainer.setTag(categories);
            idList.add(categories.getId());
            fieldContainer.addView(nestedContainer);
            for (int j = 0; j < 1; j++) {

                buildLayout(categories.getQuestionsList().get(0));
                checkForAnswer(categories.getQuestionsList().get(0),currentResponseId);
            }

        }

    }

    public void setCategoryTitle(Categories categories) {


        LinearLayout nestedContainer = new LinearLayout(this);
        nestedContainer.setOrientation(LinearLayout.VERTICAL);
        TextView questionTextSingleLine = new TextView(this);
        questionTextSingleLine.setTextSize(20);
        questionTextSingleLine.setTextColor(Color.BLACK);
        questionTextSingleLine.setPadding(8, 12, 8, 20);
        questionTextSingleLine.setText("" + categories.getContent());
        nestedContainer.addView(questionTextSingleLine);
        nestedContainer.setId(categories.getId());
        nestedContainer.setTag(categories);
        idList.add(categories.getId());
        fieldContainer.addView(nestedContainer);

        for (int j = 0; j < categories.getQuestionsList().size(); j++) {

            buildLayout(categories.getQuestionsList().get(j));
            checkForAnswer(categories.getQuestionsList().get(j),currentResponseId);
        }


    }

    public void removeQuestionView(Options options) {


        try {
            for (int i = 0; i < options.getQuestions().size(); i++) {

                View myView = findViewById(options.getQuestions().get(i).getId());
                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                // idList.remove(options.getQuestions().get(i).getId());
                nestedQuestions.remove(options.getQuestions().get(i));

                if (options.getQuestions().get(i).getOptions().size() > 0) {

                    for (int j = 0; j < options.getQuestions().get(i).getOptions().size(); j++) {
                        removeQuestionView(options.getQuestions().get(i).getOptions().get(j));
                        removeCategoryView(options.getQuestions().get(i).getOptions().get(j));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeCategoryView(Options options) {

        try {
            for (int k = 0; k < options.getCategories().size(); k++) {

                View myView = findViewById(options.getCategories().get(k).getId());
                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);


                for (int h = 0; h < options.getCategories().get(k).getQuestionsList().size(); h++) {
                    View myView2 = findViewById(options.getCategories().get(k).getQuestionsList().get(h).getId());
                    ViewGroup parent2 = (ViewGroup) myView2.getParent();
                    parent2.removeView(myView2);


                    if (options.getCategories().get(k).getQuestionsList().get(h).getOptions().size() > 0) {

                        for (int j = 0; j < options.getCategories().get(k).getQuestionsList().get(h).getOptions().size(); j++) {
                            removeQuestionView(options.getCategories().get(k).getQuestionsList().get(h).getOptions().get(j));
                            removeCategoryView(options.getCategories().get(k).getQuestionsList().get(h).getOptions().get(j));
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAnswer(Questions questions) {


        if (questions.getType().equals("SingleLineQuestion")) {
            Answers answers = new Answers();
            answers.setQuestion_id(questions.getId());
            answers.setResponseId(currentResponseId);
            answers.setContent(answer.getText().toString());
            tsLong= System.currentTimeMillis() / 1000;
            answers.setUpdated_at(tsLong);
            long x = dbAdapter.insertDataAnswersTable(answers);
            //Toast.makeText(NewResponseActivity.this,currentResponseId+"",Toast.LENGTH_SHORT).show();
        }

        if (questions.getType().equals("MultiLineQuestion")) {
            Answers answers = new Answers();
            answers.setQuestion_id(questions.getId());
            answers.setResponseId((int) dbAdapter.getMaxID());
            answers.setContent(answer.getText().toString());
            tsLong= System.currentTimeMillis() / 1000;
            answers.setUpdated_at(tsLong);
            long x = dbAdapter.insertDataAnswersTable(answers);
            //Toast.makeText(NewResponseActivity.this,x+"",Toast.LENGTH_SHORT).show();
        }

        if (questions.getType().equals("NumericQuestion")) {
            Answers answers = new Answers();
            answers.setResponseId((int) dbAdapter.getMaxID());
            answers.setQuestion_id(questions.getId());
            tsLong= System.currentTimeMillis() / 1000;
            answers.setUpdated_at(tsLong);
            answers.setContent(answer.getText().toString());
            long x = dbAdapter.insertDataAnswersTable(answers);
            //Toast.makeText(NewResponseActivity.this,x+"",Toast.LENGTH_SHORT).show();
        }


        if (questions.getType().equals("DateQuestion")) {
            Answers answers = new Answers();
            answers.setResponseId((int) dbAdapter.getMaxID());
            answers.setQuestion_id(questions.getId());
            tsLong= System.currentTimeMillis() / 1000;
            answers.setUpdated_at(tsLong);
            answers.setContent(dateText.getText().toString());
            long x = dbAdapter.insertDataAnswersTable(answers);
            //Toast.makeText(NewResponseActivity.this,x+"",Toast.LENGTH_SHORT).show();
        }


        if (questions.getType().equals("RatingQuestion")) {
            Answers answers = new Answers();
            answers.setResponseId((int) dbAdapter.getMaxID());
            answers.setQuestion_id(questions.getId());
            tsLong= System.currentTimeMillis() / 1000;
            answers.setUpdated_at(tsLong);
            answers.setContent(String.valueOf(ratingBar.getRating()));
            long x = dbAdapter.insertDataAnswersTable(answers);
            //Toast.makeText(NewResponseActivity.this,x+"",Toast.LENGTH_SHORT).show();
        }


        if (questions.getType().equals("PhotoQuestion")) {

            Answers answers = new Answers();
            answers.setResponseId((int) dbAdapter.getMaxID());
            answers.setQuestion_id(questions.getId());
            answers.setImage(fname);
            tsLong= System.currentTimeMillis() / 1000;
            answers.setUpdated_at(tsLong);
            long x = dbAdapter.insertDataAnswersTable(answers);
            Toast.makeText(NewResponseActivity.this, x + "", Toast.LENGTH_LONG).show();
        }


    }

    public void checkForAnswer(Questions qu, int responseId) {

        if (qu.getType().equals("SingleLineQuestion")) {
            answer= (EditText) findViewById(qu.getId());
            answer.setText(dbAdapter.getAnswer(responseId, qu.getId()));
        }


        if (qu.getType().equals("MultiLineQuestion")) {
            answer= (EditText) findViewById(qu.getId());
            answer.setText(dbAdapter.getAnswer(responseId, qu.getId()));
        }


        if (qu.getType().equals("DateQuestion")) {
            dateText= (TextView) findViewById(qu.getId());
            dateText.setText(dbAdapter.getAnswer(responseId, qu.getId()));
        }

        if (qu.getType().equals("NumericQuestion")) {
            answer= (EditText) findViewById(qu.getId());
            answer.setText(dbAdapter.getAnswer(responseId, qu.getId()));
        }
        if (qu.getType().equals("RatingQuestion")) {

            ratingBar= (RatingBar) findViewById(qu.getId());
            dbAdapter.getAnswer(responseId, qu.getId());
            //Integer.parseInt(dbAdapter.getAnswer(responseId, qu.getId()));
            try {
                float f = Float.parseFloat((dbAdapter.getAnswer(responseId, qu.getId())));
                ratingBar.setRating(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (qu.getType().equals("PhotoQuestion")) {
            if (!dbAdapter.getImage(responseId, qu.getId()).equals("")) {
                deleteImageRelativeLayout.setVisibility(View.VISIBLE);
                loadImageFromStorage(Environment.getExternalStorageDirectory().toString() + "/saved_images", dbAdapter.getImage(responseId, qu.getId()));
            }
        }


        //to remake multi choice view
        if (qu.getType().equals("MultiChoiceQuestion")) {
            List<Options> options = new ArrayList<Options>();
            options = qu.getOptions();
            for (int i = 0; i < options.size(); i++) {
                options.get(i).getId();
            }


            List<Integer> integers = new ArrayList<Integer>();
            integers = dbAdapter.getIdFromAnswerTable(responseId, qu.getId());


            List<Integer> list2 =
                    dbAdapter.getOptionIds(integers);

            for (int i = 0; i < list2.size(); i++) {
                list2.get(i);
                CheckBox checkBox = (CheckBox) findViewById(list2.get(i));
                checkBox.setChecked(true);


                for (int k = 0; k < options.size(); k++) {
                    if (options.get(k).getId() == list2.get(i)) {
                        // Toast.makeText(NewResponseActivity.this, "thi9s is workingh", Toast.LENGTH_SHORT).show();
                        Options options1 = options.get(k);
                        if (options1.getQuestions().size() > 0) {
                            for (int l = 0; l < options1.getQuestions().size(); l++) {
                                buildLayout(options1.getQuestions().get(l));
                                //checkForAnswer(options1.getQuestions().get(l),currentResponseId);
                            }
                        }
                    }
                }

            }

        }


        // to remake radio button view
        if (qu.getType().equals("RadioQuestion")) {
            List<Options> options = new ArrayList<Options>();
            options = qu.getOptions();
            for (int i = 0; i < options.size(); i++) {
                options.get(i).getId();
            }

            List<Integer> integers = new ArrayList<Integer>();
            integers = dbAdapter.getIdFromAnswerTable(responseId, qu.getId());


            List<Integer> list2 =
                    dbAdapter.getOptionIds(integers);

            for (int i = 0; i < list2.size(); i++) {
                list2.get(i);
                try {
                    RadioButton radioButton = (RadioButton) findViewById(list2.get(i));
                    radioButton.setChecked(true);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


        //to remake dropdown view
        if (qu.getType().equals("DropDownQuestion")) {
            List<Options> options = new ArrayList<Options>();
            options = qu.getOptions();
            for (int i = 0; i < options.size(); i++) {
                options.get(i).getId();
            }

            List<Integer> integers = new ArrayList<Integer>();
            integers = dbAdapter.getIdFromAnswerTable(responseId, qu.getId());


            List<Integer> list2 =
                    dbAdapter.getOptionIds(integers);

            for (int i = 0; i < list2.size(); i++) {

                for (int j = 0; j < qu.getOptions().size(); j++) {
                    if (qu.getOptions().get(j).getId() == list2.get(i)) {

                        // Toast.makeText(NewResponseActivity.this, list2.get(i) + "idid", Toast.LENGTH_LONG).show();
                        spinner.setSelection(j);

                    }

                }
            }
        }


    }

    //add record to database in case of selectedlement
    public void addOptionToDataBase(Options options, Questions qu) {

        Answers answers = new Answers();
        answers.setQuestion_id(qu.getId());
        answers.setResponseId(currentResponseId);
        answers.setContent("");
        long x = dbAdapter.insertDataAnswersTable(answers);
        Choices choices = new Choices();
        choices.setAnswerId((int) dbAdapter.getMaxIDAnswersTabele());
        choices.setOptionId(options.getId());
        choices.setOption(options.getContent());
        dbAdapter.insertDataChoicesTable(choices);
        //Toast.makeText(NewResponseActivity.this,dbAdapter.insertDataChoicesTable(choices)+"add",Toast.LENGTH_LONG).show();
    }

    //this is remove option from table in case of multichoice
    public void removeOptionFromDataBase(Options options, Questions qu) {
        dbAdapter.deleteOption(options);
        //Toast.makeText(NewResponseActivity.this,dbAdapter.deleteOption(options)
        //+"delete",Toast.LENGTH_LONG).show();

    }

    //this is for not selected elements of radio
    public void removeOthersFromDataBase(Options options, Questions qu) {
        for (int i = 0; i < qu.getOptions().size(); i++) {
            if (options.getId() != qu.getOptions().get(i).getId()) {
                dbAdapter.deleteFromChoicesTableWhereOptionId(qu.getOptions().get(i).getId());
            }
        }
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            dateText.setText(new StringBuilder().append(mYear).append("/").append(mMonth + 1).append("/").append(mDay).toString());


                Answers answers = new Answers();
                answers.setResponseId((int) dbAdapter.getMaxID());
                answers.setQuestion_id(dateQuestion.getId());
                answers.setContent(dateText.getText().toString());
            tsLong= System.currentTimeMillis() / 1000;
            answers.setUpdated_at(tsLong);
                long x = dbAdapter.insertDataAnswersTable(answers);
                //Toast.makeText(NewResponseActivity.this,x+"",Toast.LENGTH_SHORT).show();

        }
    }


}