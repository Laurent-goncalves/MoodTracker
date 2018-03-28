package com.g.laurent.moodtracker.Controllers.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.g.laurent.moodtracker.Models.ListViewAdapter;
import com.g.laurent.moodtracker.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import static java.lang.System.currentTimeMillis;


public class ChronoFragment extends Fragment {

    private int[] colors;
    private String[] chrono_texts;
    private SharedPreferences sharedPreferences;
    private ListView mListView;
    private String[] list_comments;
    private int[] list_feelings;
    private ArrayList<String> table_positions;

    public ChronoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chrono, container, false);
        mListView = view.findViewById(R.id.listview_chrono);

        // Recover sharedPreferences and colors table for the different feelings
        sharedPreferences=getContext().getSharedPreferences("chrono", Context.MODE_PRIVATE);

        // Create table of values for color of each feelings and texts for chronology
        colors = getResources().getIntArray(R.array.colorPagesViewPager);
        chrono_texts = getResources().getStringArray(R.array.text_chrono_list);

        recover_data_chrono();

        // For each day in sharedpreferences, adjust the linearlayout and the icon comment
        create_layout_chronofragment();

        //System.out.println("eeee " + container.findViewById(Window.ID_ANDROID_CONTENT).getTop());

        // Inflate the layout for this fragment
        return view;

    }

    private String create_date_ddMMyyyy(Long date){
        return DateFormat.format("dd/MM/yyyy", new Date(date)).toString();
    }

    private void recover_data_chrono(){


        if(chrono_texts.length>0){

            // Initialization
            list_comments = new String[chrono_texts.length];
            list_feelings = new int[chrono_texts.length];
            table_positions = new ArrayList<>();
            int limit = chrono_texts.length-1;

            // Creation of an ArrayList to get all dates to be considered for the chronology
            for (int i = limit+1; i >= 1 ; i--)
                table_positions.add(create_date_ddMMyyyy(currentTimeMillis()-24*60*60*1000*i));

            display_data();

            // Data recovering
            if (sharedPreferences != null) {

                for (int i = limit; i >= 0; i--) { // for each date considered

                    for(int j=0; j<= limit; j++){ // check in sharedpreferences if the date appears in sharedpref

                        String mDateTime = create_date_ddMMyyyy(sharedPreferences.getLong("DATE_TIME_" + j, 0));

                        if(mDateTime.equals(table_positions.get(i))){ // if the date appears in sharedpref
                            list_comments[i]=sharedPreferences.getString("COMMENT_" + j, null);
                            list_feelings[i]=sharedPreferences.getInt("FEELING_" + j, -1);
                            break;
                        } else if (j==limit){ // if the date doesn't appear in sharedpref
                            list_comments[i]=null;
                            list_feelings[i]=-1;
                            break;
                        }
                    }
                }
            }
        }
    }


    private void display_data(){
        for(int j = 0; j<=6;j++)
        System.out.println("eeee table positions =" + table_positions.get(j));

    }


    private void create_layout_chronofragment(){

        int screen_width = this.getResources().getDisplayMetrics().widthPixels;
        int screen_height= this.getResources().getDisplayMetrics().heightPixels - getToolBarHeight();

        System.out.println("eeee " + list_feelings[0] + " " + list_feelings[1] + " " + list_feelings[2] + " " +list_feelings[3] + " " +list_feelings[4] + " " +list_feelings[5] + " " +list_feelings[6]  );

        if(colors.length>0 && list_feelings.length>0){

            final ListViewAdapter adapter = new ListViewAdapter(
                    getContext(), chrono_texts,list_comments,list_feelings,colors,screen_width,screen_height);

            mListView.setAdapter(adapter);
        } else {

            Toast toast = Toast.makeText(getContext(), "Aucune chronologie !", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    public int getToolBarHeight() {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }

}







/* if(chrono_texts.length>0){

            // Initialization
            list_comments = new String[chrono_texts.length];
            list_feelings = new int[chrono_texts.length];
            table_positions = new HashMap<>();

            // Creation of HashMap table to get all dates saved in sharedpreferences
            for (int i = 1; i <= chrono_texts.length; i++)
                table_positions.put(DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis()-24*60*60*1000*i)).toString(),chrono_texts.length-i);

            // System.out.println("eeeee " + table_positions.toString());

            // Data recovering
            if (sharedPreferences != null) {

                for (int i = chrono_texts.length-1; i >= 0; i--) {

                    int feeling_number = sharedPreferences.getInt("FEELING_" + i, -1);
                    String comment = sharedPreferences.getString("COMMENT_" + i, null);
                    String mDateTime = DateFormat.format("dd/MM/yyyy", new Date(sharedPreferences.getLong("DATE_TIME_" + i, 0))).toString();

                    System.out.println("eeeee     i=" + i + "    feeling_number=" + feeling_number);

                    if (table_positions.get(mDateTime)!=null) { // if there is a date associated to a feeling in sharedpreferences
                        int position = table_positions.get(mDateTime);
                        list_comments[position]=comment;
                        list_feelings[position]=feeling_number;
                    } else {


                    }
                }
            }
            */




/*@BindViews({ R.id.fragment_chrono_feeling1, R.id.fragment_chrono_feeling2, R.id.fragment_chrono_feeling3,
            R.id.fragment_chrono_feeling4,R.id.fragment_chrono_feeling5,R.id.fragment_chrono_feeling6,R.id.fragment_chrono_feeling7})

    List<RelativeLayout> mRelativeLayouts;

    @BindViews({ R.id.fragment_chrono_comment1, R.id.fragment_chrono_comment2, R.id.fragment_chrono_comment3,
            R.id.fragment_chrono_comment4,R.id.fragment_chrono_comment5,R.id.fragment_chrono_comment6,R.id.fragment_chrono_comment7})

    List<ImageView> mImageViews;



    private SharedPreferences sharedPreferences;
    private int position;
    private String comment;
    private Date mDateTime;
    private int screen_width;
    private int[] colors;
    private HashMap<Integer,String> CommentTable;
    private int count = 0;

    public ChronoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chrono, container, false);
        ButterKnife.bind(this, view);
        screen_width = this.getResources().getDisplayMetrics().widthPixels;

        CommentTable=new HashMap<Integer,String>();

        // Recover sharedPreferences and colors table for the different feelings
        sharedPreferences=getContext().getSharedPreferences("chrono", Context.MODE_PRIVATE);
        colors = getResources().getIntArray(R.array.colorPagesViewPager);

        // For each day in sharedpreferences, adjust the linearlayout and the icon comment
        create_layout_chronofragment();

        // Inflate the layout for this fragment
        return view;

    }

    private void create_layout_chronofragment(){

        if (sharedPreferences !=null) {

            for (int i = 6; i >= 0; i--) {

                position = sharedPreferences.getInt("FEELING_" + i, 0);
                comment = sharedPreferences.getString("COMMENT_" + i, null);
                mDateTime = new Date(sharedPreferences.getLong("DATE_TIME_" + i, 0));

                if (!mDateTime.equals(new Date(0)) && position !=-1) { // if feeling saved in sharepreferences for the day "i"
                    // the linearlayout is adjusted to the width of the view
                    mRelativeLayouts.get(i).getLayoutParams().width = (1 + position) * screen_width / 5;
                    //mImageViews.get(i).setWidth((1+position) * container.getWidth()/5);

                    // the linearlayout must be colored with the color related to the feeling
                    mRelativeLayouts.get(i).setBackgroundColor(colors[position]);

                    // the icon for the comment appears only if there is a comment different than null
                    if (comment != null && !comment.equals("")) {

                        mImageViews.get(i).setImageResource(R.drawable.ic_comment_black_48px);
                        CommentTable.put(mImageViews.get(i).getId(), comment);

                        // add a clicklistener on comment icon to open a Toast message with the comment
                        mImageViews.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast toast = Toast.makeText(getContext(), CommentTable.get(v.getId()), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }

                } else { // else, the linearlayout is invisible
                    count++;
                    mRelativeLayouts.get(i).setVisibility(View.INVISIBLE);
                }
            }
        }

        if(count == 7){
            Toast toast = Toast.makeText(getContext(), "Aucune chronologie !", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }*/