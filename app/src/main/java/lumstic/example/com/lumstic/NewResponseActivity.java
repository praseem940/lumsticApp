package lumstic.example.com.lumstic;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Utils.IntentConstants;

public class NewResponseActivity extends Activity {

    TextView questionNumber;
    TextView questionText;
    RadioButton button;
    RadioGroup rg;
    TableRow row;
    RelativeLayout fieldContainer;
    LayoutInflater inflater;
    TextView answerText;
    RelativeLayout imageContainer;
    ImageView imageView;
    Uri picUri;
    int CAMERA_REQUEST = 1;
    final int PIC_CROP = 2;
    Spinner spinner;
    List<Questions> questionsList;
    List<String> questions;
    List<Integer> layouts;
    Questions currentQuestions;
    int questionCounter = 0;
    Button nextQuestion, previousQuestion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_response);
        getActionBar().setTitle("New Response");
        questionNumber = (TextView) findViewById(R.id.question_number_text);
        questionText = (TextView) findViewById(R.id.question_text);
        layouts = new ArrayList<Integer>();
        questionsList = new ArrayList<Questions>();
        questionsList = (List<Questions>) getIntent().getExtras().getSerializable(IntentConstants.QUESTIONS);
        questions = new ArrayList<String>();
        for (int Counter = 0; Counter < questionsList.size(); Counter++) {
            questions.add(questionsList.get(Counter).getContent());
            String questionType = questionsList.get(Counter).getType();
            checkQuestionType(questionType, Counter);
        }
        fieldContainer = (RelativeLayout) findViewById(R.id.field_container);
        nextQuestion = (Button) findViewById(R.id.next_queation);
        previousQuestion = (Button) findViewById(R.id.previous_question);
        questionText.setText(questions.get(0));
        currentQuestions = questionsList.get(0);
        fieldContainer.removeAllViews();
        inflater = getLayoutInflater();
        fieldContainer.addView(inflater.inflate(layouts.get(0), null));
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                questionCounter++;
                currentQuestions = questionsList.get(questionCounter);
                fieldContainer.removeAllViews();
                fieldContainer.addView(inflater.inflate(layouts.get(questionCounter), null));
                questionText.setText(questions.get(questionCounter));
                CheckQuestion(layouts.get(questionCounter), currentQuestions);
            }
        });
        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionCounter--;
                currentQuestions = questionsList.get(questionCounter);
                fieldContainer.removeAllViews();
                fieldContainer.addView(inflater.inflate(layouts.get(questionCounter), null));
                questionText.setText(questions.get(questionCounter));
                CheckQuestion(layouts.get(questionCounter), currentQuestions);
            }
        });
    }

    public void CheckQuestion(int layoutId, Questions currentQuestions) {
        //for date questions
        if (layoutId == R.layout.answer_date_picker) {
            TextView answerText = (TextView) findViewById(R.id.answer_text);
            answerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    System.out.println("the selected " + mDay);
                    DatePickerDialog dialog = new DatePickerDialog(NewResponseActivity.this,
                            new mDateSetListener(), mYear, mMonth, mDay);
                    dialog.show();
                }
            });
        }
        //for image questions
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

        if (layoutId == R.layout.answer_dropdown) {
            Spinner spinner = (Spinner) findViewById(R.id.drop_down);
            List<String> listOptions = new ArrayList<String>();
            for (int i = 0; i < currentQuestions.getOptions().size(); i++) {
                listOptions.add(currentQuestions.getOptions().get(i).getContent());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, listOptions);
            spinner.setAdapter(dataAdapter);
        }


        if (currentQuestions.getType().contains("RadioQuestion")) {
            rg = new RadioGroup(this);
            rg.setOrientation(RadioGroup.VERTICAL);
            for (int i = 0; i < currentQuestions.getOptions().size(); i++) {

                button = new RadioButton(this);
                rg.addView(button);
                button.setText(currentQuestions.getOptions().get(i).getContent());
                button.setId(i);
                button.setButtonDrawable(R.drawable.custom_radio_button);

            }

            fieldContainer.addView(rg);
        }

        if (currentQuestions.getType().contains("MultiChoiceQuestion")) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < currentQuestions.getOptions().size(); i++) {

                CheckBox checkBox = new CheckBox(this);
                //checkBox.setOnCheckedChangeListener(this);
                checkBox.setId(i);
                checkBox.setText(currentQuestions.getOptions().get(i).getContent());
                checkBox.setTextSize(20);
                checkBox.setButtonDrawable(R.drawable.custom_checkbox);
                ll.addView(checkBox);
            }
            fieldContainer.addView(ll);
        }

        if (currentQuestions.getType().contains("SingleLineQuestion")) {
            boolean hintAvailable = true, textHintAvailable = true, imageHintAvailable = false;
            final RelativeLayout hintContainer = (RelativeLayout) findViewById(R.id.hint_container);
            if (hintAvailable) {
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

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            answerText.setText(new StringBuilder().append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" ").toString());
        }
    }

    public void checkQuestionType(String questionType, int position) {
        if (questionType.contains("PhotoQuestion"))
            layouts.add(position, R.layout.answer_image_picker);

        if (questionType.contains("MultilineQuestion"))
            layouts.add(position, R.layout.answer_multi_line);

        if (questionType.contains("NumericQuestion"))
            layouts.add(position, R.layout.answer_numeric);

        if (questionType.contains("DateQuestion"))
            layouts.add(position, R.layout.answer_date_picker);

        if (questionType.contains("RatingQuestion"))
            layouts.add(position, R.layout.answer_rating);

        if (questionType.contains("RadioQuestion"))
            layouts.add(position, R.layout.answer_radio_button);

        if (questionType.contains("MultiChoiceQuestion"))
            layouts.add(position, R.layout.answer_multi_choice);

        if (questionType.contains("SingleLineQuestion"))
            layouts.add(position, R.layout.answer_single_line);

        if (questionType.contains("DropDownQuestion"))
            layouts.add(position, R.layout.answer_dropdown);
    }
}

