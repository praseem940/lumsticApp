package lumstic.example.com.lumstic.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    Questions currentQuestions;
    List<Questions> nestedQuestions;
    boolean nextLayoutCreated = false;


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
        getActionBar().setTitle("New Response Activity");
        fieldContainer = (LinearLayout) findViewById(R.id.field_container);
        inflater = getLayoutInflater();
        nestedQuestions = new ArrayList<Questions>();
        idList = new ArrayList<Integer>();

        questionsList = new ArrayList<Questions>();
        questionsList = (List<Questions>) getIntent().getExtras().getSerializable(IntentConstants.QUESTIONS);
        questionCount = questionsList.size();
        Questions currentQuestion = questionsList.get(0);

        buildLayout(currentQuestion);
        boolean nextLayoutPresent = false;

        nextQuestion = (Button) findViewById(R.id.next_queation);
        previousQuestion = (Button) findViewById(R.id.previous_question);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (questionCounter < questionCount - 1) {
                    nestedQuestions.clear();
                    idList.clear();
                    fieldContainer.removeAllViews();
                    questionCounter++;
                    Questions currentQuestion = questionsList.get(questionCounter);

                    buildLayout(currentQuestion);
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
                    Questions currentQuestion = questionsList.get(questionCounter);

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
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_single_line, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
            EditText answerSingleLine = (EditText) findViewById(R.id.answer_text);
            checkHint();
        }


        if (ques.getType().contains("MultiLineQuestion")) {

            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_multi_line, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
            EditText answerMultiLine = (EditText) findViewById(R.id.answer_text);
            checkHint();

        }

        if (ques.getType().contains("DropDownQuestion")) {


            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
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

                    Toast.makeText(NewResponseActivity.this,""+i,Toast.LENGTH_SHORT).show();
                    Options options= ques.getOptions().get(i);
                    if(options.getQuestions().size()>0){
                        Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                        for(int j=0;j<options.getQuestions().size();j++){
                            buildLayout(options.getQuestions().get(j));
                        }
                    }

                    if(options.getQuestions().size()<=0){
                        for(int j=0;j<idList.size();j++){
                            if(idList.get(j)==options.getQuestionId())
                            {
                                for(int k=j+1;k<idList.size();){
                                    try{
                                        View myView = findViewById(idList.get(k));
                                        ViewGroup parent = (ViewGroup) myView.getParent();
                                        parent.removeView(myView);
                                        idList.remove(k);
                                        nestedQuestions.remove(k);
                                    }catch ( Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
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
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
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
                checkBox.setTextSize(20);
                checkBox.setTag(ques.getOptions().get(i));
                checkBox.setButtonDrawable(R.drawable.custom_checkbox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((CheckBox) view).isChecked()) {

                            CheckBox checkBox1= (CheckBox) view;
                            Options options= (Options) checkBox1.getTag();
                            if(options.getQuestions().size()>0){
                                Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                                for(int i=0;i<options.getQuestions().size();i++){
                                    buildLayout(options.getQuestions().get(i));
                                }
                            }
                        }

                        if(!((CheckBox) view).isChecked()){

                            Toast.makeText(NewResponseActivity.this,"has been unchecked ",Toast.LENGTH_SHORT).show();
                            CheckBox checkBox1= (CheckBox) view;
                            Options options= (Options) checkBox1.getTag();

                            if(options.getQuestions().size()>0){
                            for(int j=0;j<idList.size();j++){
                                if(idList.get(j)==options.getQuestionId())
                                {
                                    for(int k=j+1;k<idList.size();){
                                        try{
                                            View myView = findViewById(idList.get(k));
                                            ViewGroup parent = (ViewGroup) myView.getParent();
                                            parent.removeView(myView);
                                                idList.remove(k);
                                            nestedQuestions.remove(k);
                                        }catch ( Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }}
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
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.addView(inflater.inflate(R.layout.answer_numeric, null));
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            fieldContainer.addView(nestedContainer);
            EditText answerMultiLine = (EditText) findViewById(R.id.answer_text);
            checkHint();

        }


        if (ques.getType().contains("DateQuestion")) {
            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
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
        if (ques.getType().contains("RadioQuestion"))  if (ques.getType().contains("RadioQuestion")) {



            nestedQuestions.add(ques);

            LinearLayout nestedContainer = new LinearLayout(this);
            nestedContainer.setOrientation(LinearLayout.VERTICAL);
            TextView questionTextSingleLine = new TextView(this);
            questionTextSingleLine.setTextSize(20);
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
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
                        Toast.makeText(NewResponseActivity.this,options.getId()+"",Toast.LENGTH_SHORT).show();
                        if(options.getQuestions().size()>0){
                            Toast.makeText(NewResponseActivity.this,"has options",Toast.LENGTH_SHORT).show();
                            for(int i=0;i<options.getQuestions().size();i++){
                                buildLayout(options.getQuestions().get(i));
                            }
                        }
                        if(options.getQuestions().size()<=0){
                            Toast.makeText(NewResponseActivity.this,"has no options",Toast.LENGTH_SHORT).show();
                            for(int j=0;j<idList.size();j++){
                                if(idList.get(j)==options.getQuestionId())
                                {
                                    for(int k=j+1;k<idList.size();){
                                        try{
                                            View myView1 = findViewById(idList.get(k));
                                            ViewGroup parent = (ViewGroup) myView1.getParent();
                                            parent.removeView(myView1);
                                            idList.remove(k);
                                            nestedQuestions.remove(k);
                                        }catch ( Exception e){
                                            e.printStackTrace();
                                        }
                                    }

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
            questionTextSingleLine.setTextColor(Color.BLACK);
            questionTextSingleLine.setPadding(8, 12, 8, 20);
            questionTextSingleLine.setText("Q" + ques.getOrderNumber() + ")" + "   " + ques.getContent());
            nestedContainer.addView(questionTextSingleLine);
            nestedContainer.setId(ques.getId());
            nestedContainer.setTag(ques);
            idList.add(ques.getId());
            RatingBar ratingBar = new RatingBar(this);
            ratingBar.setNumStars(5);
            nestedContainer.addView(ratingBar);
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

}
