package com.rizkytm.wordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.rizkytm.wordgame.Adapter.GridViewAnswerAdapter;
import com.rizkytm.wordgame.Adapter.GridViewSuggestAdapter;
import com.rizkytm.wordgame.Common.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Integer> posisi = new ArrayList<Integer>();

    public int ke = 0;

    public List<String> suggestSource = new ArrayList<>();

    public GridViewAnswerAdapter answerAdapter;
    public GridViewSuggestAdapter suggestAdapter = new GridViewSuggestAdapter();

    public Button btnSubmit, btnDelete;

    public GridView gridViewAnswer, gridViewSuggest;

    public ImageView imgViewQuestion;

    int[] image_list = {
            R.drawable.facebook,
            R.drawable.instagram,
            R.drawable.line,
            R.drawable.twitter,
            R.drawable.whatsapp,
            R.drawable.youtube
    };

    public char[] answer;

    String correct_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        gridViewAnswer = (GridView) findViewById(R.id.gridViewAnswer);
        gridViewSuggest = (GridView) findViewById(R.id.gridViewSuggest);

        imgViewQuestion = (ImageView) findViewById(R.id.imgLogo);

        setupList();

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setEnabled(false);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ke--;
                    if (ke > 0) {
                        btnDelete.setEnabled(true);
                    } else {
                        btnDelete.setEnabled(false);
                    }
                    int terakhir = Arrays.asList(Common.user_submit_answer).indexOf(ke);
    //                char chara = Common.user_submit_answer[ke];
                    char chara = Common.user_submit_answer[ke];
                    String karakter = Character.toString(chara);
                    Common.user_submit_answer[ke] = ' ';

                    GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(Common.user_submit_answer, getApplicationContext());
                    gridViewAnswer.setAdapter(answerAdapter);
                    answerAdapter.notifyDataSetChanged();

                    Log.i("info",String.valueOf(suggestAdapter.posisi_terakhir));

                    suggestSource.set(posisi.get(posisi.size()-1),karakter);
                    posisi.remove(posisi.size()-1);
                    suggestAdapter = new GridViewSuggestAdapter(suggestSource,getApplicationContext(),MainActivity.this);
                    gridViewSuggest.setAdapter(suggestAdapter);
                    suggestAdapter.notifyDataSetChanged();
                }
            });


        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                for (int i=0;i< Common.user_submit_answer.length;i++) {
                    result+=String.valueOf(Common.user_submit_answer[i]);
                }
                if (result.equals(correct_answer)) {
                    Toast.makeText(getApplicationContext(), "Finish ! This is " + result, Toast.LENGTH_SHORT).show();

                    ke=0;

                    Common.count = 0;
                    Common.user_submit_answer = new char[correct_answer.length()];

                    GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(setupNullList(), getApplicationContext());
                    gridViewAnswer.setAdapter(answerAdapter);
                    answerAdapter.notifyDataSetChanged();

                    GridViewSuggestAdapter suggestAdapter = new GridViewSuggestAdapter(suggestSource, getApplicationContext(), MainActivity.this);
                    gridViewSuggest.setAdapter(suggestAdapter);
                    suggestAdapter.notifyDataSetChanged();

                    setupList();

                } else {
                    Toast.makeText(MainActivity.this, "Incorrect!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupList() {

        Random random = new Random();
        int imageSelected = image_list[random.nextInt(image_list.length)];
        imgViewQuestion.setImageResource(imageSelected);

        correct_answer = getResources().getResourceName(imageSelected);
        correct_answer = correct_answer.substring(correct_answer.lastIndexOf("/")+1);

        answer = correct_answer.toCharArray();

        Common.user_submit_answer = new char[answer.length];

        suggestSource.clear();

        for (char item:answer) {
            suggestSource.add(String.valueOf(item));
        }

//        for (int i = answer.length; i<answer.length*2; i++) {
//            suggestSource.add(Common.alphabet_character[random.nextInt(Common.alphabet_character.length)]);
//        }

        Collections.shuffle(suggestSource);

        answerAdapter = new GridViewAnswerAdapter(setupNullList(), this);
        suggestAdapter = new GridViewSuggestAdapter(suggestSource, this, this);

        answerAdapter.notifyDataSetChanged();
        suggestAdapter.notifyDataSetChanged();

        gridViewSuggest.setAdapter(suggestAdapter);
        gridViewAnswer.setAdapter(answerAdapter);
    }

    private char[] setupNullList() {
        char result[] = new char[answer.length];
        for (int i=0; i<answer.length; i++) {
            result[i]=' ';
        }
        return result;
    }
}
