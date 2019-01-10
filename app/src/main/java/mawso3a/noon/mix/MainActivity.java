package mawso3a.noon.mix;


import android.content.Intent;
import android.util.Log;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ListView;

import android.webkit.WebView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import  java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private WebView wv;

    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
       // TextView tv = findViewById(R.id.sample_text);
        //tv.setText("gggggggggggggggg");

        fa = this;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;


        if (currentapiVersion >= 19) {

            Log.d("data",getwv());

            if(getwv().equals("null")) {


                getdiag();

            }

            else{

                if(getwv().equals("nocross")){

                    Intent i = new Intent(this,NocrossActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);;
            startActivity(i);
            finish();
                }
                else{

                    Intent i = new Intent(this,CrossActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);;
                    startActivity(i);
                   // finish();
                }
            }








        } else {


            Intent i = new Intent(this,CrossActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);;
            startActivity(i);
            finish();

        }




    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String html);





    public void savewv(String url){
        SharedPreferences sp = getSharedPreferences("SP_WEBVIEW_TYPE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SAVED_wv", url);
        editor.commit();
    }


    public String getwv(){

        SharedPreferences sp = getSharedPreferences("SP_WEBVIEW_TYPE", MODE_PRIVATE);
        //If you haven't saved the url before, the default value will be google's page
        return sp.getString("SAVED_wv", "null");

    }



    @Override
    public void onStart() {
        super.onStart();
        if(!getwv().equals("null")) {


            getdiag();

        }
        //return;
    }




    public void  getdiag(){


        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" choose web view type");

// add a radio button list
        String[] animals = {"builtin", "Crosswalk Share Mode"};
        int checkedItem = 0; // cow
        builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item

                Log.d("data",String.valueOf(which));

                ListView lv = ((AlertDialog)dialog).getListView();
                lv.setTag(new String(String.valueOf(which)));

            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                ListView lw = ((AlertDialog)dialog).getListView();
                Object checkedItem = lw.getCheckedItemPosition();



                if(String.valueOf(checkedItem)=="0"){

                    savewv("nocross");

                    Intent i = new Intent(fa,NocrossActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);;
                    startActivity(i);
                    finish();

                    Log.d("data",String.valueOf(checkedItem));
                }
                else{

                    savewv("cross");

                    Intent i = new Intent(fa,CrossActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);;
                    startActivity(i);
                    // finish();

                    Log.d("data",String.valueOf(checkedItem));


                }


            }
        });
        // builder.setNegativeButton("الغاء", null);

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }




}
