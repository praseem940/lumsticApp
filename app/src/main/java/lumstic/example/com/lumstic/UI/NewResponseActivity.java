package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lumstic.example.com.lumstic.Models.Categories;
import lumstic.example.com.lumstic.Models.Options;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;

public class NewResponseActivity extends Activity {


    List<Questions> questionsList;
    boolean hint = true;
    TextView dateText;
    Spinner spinner;
    RelativeLayout deleteImageRelativeLayout;
    TextView questionTextSingleLine;
    View v, v1;
    ViewPager viewPager;
    Questions currentQuestions;
    List<Questions> nestedQuestions;
    boolean nextLayoutCreated = false;
    EditText answer;
    Button counterButton;
    String htmlStringWithMathSymbols = "&#60";
    ActionBar actionBar;



    Questions qu;
    int questionCount = 0;
    LinearLayout fieldContainer;
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
    List<Integer> idList;

    Button nextQuestion, previousQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_response);
        actionBar=getActionBar();
        actionBar.setTitle("New Response Activity");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_ic_back);
        actionBar.setDisplayShowTitleEnabled(true);


        fieldContainer = (LinearLayout) findViewById(R.id.field_container);
        inflater = getLayoutInflater();
        nestedQuestions = new ArrayList<Questions>();
        idList = new ArrayList<Integer>();
        counterButton=(Button)findViewById(R.id.counter_button);

        questionsList = new ArrayList<Questions>();
        questionsList = (List<Questions>) getIntent().getExtras().getSerializable(IntentConstants.QUESTIONS);
        questionCount = questionsList.size();
        Questions currentQuestion = questionsList.get(0);


        counterButton.setText("1 out of "+questionsList.size());
        buildLayout(currentQuestion);
        boolean nextLayoutPresent = false;

        nextQuestion = (Button) findViewById(R.id.next_queation);
        previousQuestion = (Button) findViewById(R.id.previous_question);
        previousQuestion.setText("< Back");



        nextQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {





                 if(questionsList.get(questionCounter).getMandatory()==1){

                    if(answer.getText().toString().equals("")){
                        final Dialog dialog = new Dialog(NewResponseActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                        dialog.setContentView(R.layout.mandatory_question_dialog);
                        dialog.show();
                        Button button= (Button)dialog.findViewById(R.id.okay);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
                if ((questionCounter < questionCount - 1) &&(questionsList.get(questionCounter).getMandatory()!=1)) {
                    previousQuestion.setBackgroundColor(getResources().getColor(R.color.login_button_color));
                    previousQuestion.setTextColor(getResources().getColor(R.color.white));
                    nestedQuestions.clear();
                    idList.clear();
                    fieldContainer.removeAllViews();
                    questionCounter++;
                    counterButton.setText(questionCounter+1+" out of "+questionsList.size());
                    Questions currentQuestion = questionsList.get(questionCounter);
                    buildLayout(currentQuestion);
                    checkForAnswer(currentQuestion.getId());
                }
                if ((questionCounter < questionCount - 1) &&(questionsList.get(questionCounter).getMandatory()==1)&&(!answer.getText().toString().equals(""))) {
                    previousQuestion.setBackgroundColor(getResources().getColor(R.color.login_button_color));
                    previousQuestion.setTextColor(getResources().getColor(R.color.white));
                    nestedQuestions.clear();
                    idList.clear();
                    fieldContainer.removeAllViews();
                    questionCounter++;
                    counterButton.setText(questionCounter+1+" out of "+questionsList.size());
                    Questions currentQuestion = questionsList.get(questionCounter);
                    buildLayout(currentQuestion);
                }

                if(questionCounter!=0){

                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setDisplayShowTitleEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(false);
                    actionBar.setDisplayUseLogoEnabled(false);


                }
            }
        });

        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (questionCounter != 0) {
                    nestedQuestions.clear();
                    idList.clear();
                    fieldContainer.removeAllViews();
                    questionCounter--;
                    counterButton.setText(questionCounter+1+" out of "+questionsList.size());
                    Questions currentQuestion = questionsList.get(questionCounter);

                    buildLayout(currentQuestion);
                }
                if(questionCounter==0){

                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowTitleEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                    actionBar.setDisplayUseLogoEnabled(true);


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
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0,0,0,12);
            questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_single_line, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
             answer = (EditText) findViewById(R.id.answer_text);
            checkHint();
        }


        if (ques.getType().contains("MultiLineQuestion")) {

            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0,0,0,12);
            questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_multi_line, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
               answer = (EditText) findViewById(R.id.answer_text);
            checkHint();

        }

        if (ques.getType().contains("DropDownQuestion")) {


            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0,0,0,12);
            questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_dropdown, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);

            final Spinner spinner = (Spinner) findViewById(R.id.drop_down);
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
                    Options options= ques.getOptions().get(i);
                    if(options.getQuestions().size()>0){
                      //  Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                        for(int j=0;j<options.getQuestions().size();j++){
                            buildLayout(options.getQuestions().get(j));
                        }
                    }

                    if(options.getQuestions().size()<=0){

                    }
                    if(options.getQuestions().size()<=0){

                        for(int j=0;j<ques.getOptions().size();j++){

                            if(!ques.getOptions().get(j).getContent().equals(options.getContent())){


                                removeQuestionView(ques.getOptions().get(j));
                                    removeCategoryView(ques.getOptions().get(j));

                            }

                        }

                    }

                    if(options.getCategories().size()>0){
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


            Log.e("nestedquestionitem",nestedQuestions.size()+"");

            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0,0,0,12);
            questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.setId(ques.getId());
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
                checkBox.setTextSize(18);
                checkBox.setTextColor(getResources().getColor(R.color.text_color));
                checkBox.setTag(ques.getOptions().get(i));
                checkBox.setButtonDrawable(R.drawable.custom_checkbox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((CheckBox) view).isChecked()) {

                            CheckBox checkBox1= (CheckBox) view;
                            Options options= (Options) checkBox1.getTag();
                            if(options.getQuestions().size()>0){
                        //        Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                                for(int i=0;i<options.getQuestions().size();i++){
                                    buildLayout(options.getQuestions().get(i));
                                }
                            }

                            if(options.getCategories().size()>0){
                              setCategoryTitle(options);
                              }
                        }

                        if(!((CheckBox) view).isChecked()){

                          //  Toast.makeText(NewResponseActivity.this,"has been unchecked ",Toast.LENGTH_SHORT).show();
                            CheckBox checkBox1= (CheckBox) view;
                            Options options= (Options) checkBox1.getTag();

                            if(options.getQuestions().size()>0){
                                removeQuestionView(options);
                                }

                            if(options.getCategories().size()>0){
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
            questionTextSingleLine.setPadding(0,0,0,12);
            questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_numeric, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
           answer = (EditText) findViewById(R.id.answer_text);
            checkHint();

        }


        if (ques.getType().contains("DateQuestion")) {
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
            questionTextSingleLine.setPadding(0,0,0,12);
            questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_date_picker, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);


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



            nestedQuestions.add(ques);

                LinearLayout nestedContainer = new LinearLayout(this);
                nestedContainer.setOrientation(LinearLayout.VERTICAL);
                TextView questionTextSingleLine = new TextView(this);
                questionTextSingleLine.setTextSize(20);
                questionTextSingleLine.setTextColor(getResources().getColor(R.color.text_color));
                questionTextSingleLine.setPadding(0,0,0,12);
                questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
                nestedContainer.addView(questionTextSingleLine);
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());


            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            nestedContainer.addView(radioGroup);
            for (int i = 0; i < ques.getOptions().size(); i++) {
                final RadioButton radioButton= new RadioButton(this);
                radioGroup.addView(radioButton);
                radioButton.setId(ques.getOptions().get(i).getId());
                radioButton.setTextSize(18);
                radioButton.setTextColor(getResources().getColor(R.color.text_color));
                radioButton.setText(ques.getOptions().get(i).getContent());
                radioButton.setTag(ques.getOptions().get(i));
                radioButton.setButtonDrawable(R.drawable.custom_radio_button);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        View myView = findViewById(checkedId);
                        RadioButton radioButton1= (RadioButton) myView;
                        Options options= (Options) radioButton1.getTag();
                        //Toast.makeText(NewResponseActivity.this,options.getId()+"",Toast.LENGTH_SHORT).show();
                        if(options.getQuestions().size()>0){
                          //  Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                            for(int i=0;i<options.getQuestions().size();i++){
                                buildLayout(options.getQuestions().get(i));
                            }
                        }
                        if(options.getQuestions().size()<=0){

                            for(int i=0;i<ques.getOptions().size();i++){

                                if(!ques.getOptions().get(i).getContent().equals(options.getContent())){


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
            questionTextSingleLine.setPadding(0,0,0,12);
            questionTextSingleLine.setText("Q. " + ques.getOrderNumber() + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_rating, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
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


    public void setCategoryTitle(Options options){


        for(int i=0;i<options.getCategories().size();i++){
            Categories categories=options.getCategories().get(i);
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText(""+ categories.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.setId(categories.getId());
            nestedContainer.setTag(categories);
            idList.add(categories.getId());
            fieldContainer.addView(nestedContainer);

            for(int j=0;j<categories.getQuestionsList().size();j++){

                buildLayout(categories.getQuestionsList().get(j));
            }

        }

    }


    public void removeQuestionView(Options options){


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
        }catch (Exception e){
            e.printStackTrace();
        }

        }

    public void removeCategoryView(Options options){

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
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void checkForAnswer(int id){

    }

}
