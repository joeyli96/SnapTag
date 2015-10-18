package com.example.potatotank.dubhacks2015;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private boolean playerLoggedIn = false;
    public static int count = 0;
    String dir;
    ToggleButton one;
    ToggleButton two;
    private boolean pOne = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase myFirebaseRef = new Firebase("https://dubhacks2015.firebaseio.com/");

        //here, we are making a folder named picFolder to store pics taken by the camera using this application
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        ImageView logo = (ImageView)  findViewById(R.id.imageView_logo);
        logo.setImageResource(R.drawable.snaptag_logo);

        ImageView camera = (ImageView)  findViewById(R.id.imageView_camera);
        camera.setImageResource(R.drawable.camera_icon);

        ImageView notification = (ImageView) findViewById(R.id.imageView_notification);
        notification.setImageResource(R.drawable.notification_icon);
        notification.setEnabled(false);
        notification.setVisibility(View.INVISIBLE);

        // if it's your turn (consult Firebase)
//         notification.setEnabled(true);
//         notification.setVisibility(View.VISIBLE);

        CheckBox cb = (CheckBox) findViewById(R.id.check_p1);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            // when box is checked, you are player 1
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if (buttonView.isChecked()) {
                    //cb.setBackgroundColor(Color.BLUE);
                    pOne = true;
                    tryLogin("demo","demopwd");
                } else {
                    pOne = false;
                    tryLogin("test","testtest");
                }
            }
        });

//        final TextView textView = (TextView) findViewById(R.id.textView);
//        Button capture = (Button) findViewById(R.id.button_camera);

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                textView.setText("Nice!");
                // here, counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
                count++;
                String file = dir+count+".jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {}

                Uri outputFileUri = Uri.fromFile(newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, 1);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              // go to the reply activity
                goToReply();
                finish();
            }
        });

    }

    private void goToReply(){
        Intent goToReplyActivity = new Intent(getApplicationContext(), ReplyActivity.class);
        startActivity(goToReplyActivity);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String file = dir+count+".jpg";
        Bitmap bitmap1 = BitmapFactory.decodeFile(file);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
        }
        // goes to SubmitActivity
        Intent goToSubmitActivity = new Intent(getApplicationContext(), SubmitActivity.class);
        goToSubmitActivity.putExtra("KEY", file);
        startActivity(goToSubmitActivity);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void tryLogin(String user, final String pwd) {
        final String username = user;
        final String password = pwd;

        FirebaseClient.getInstance().ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot users = snapshot.child("users");
                for (DataSnapshot user : users.getChildren()) {
                    if (username.equals(user.child("name").getValue()) && password.equals(user.child("pwd").getValue())) {
                        DataSnapshot games = user.child("games");

                        playerLoggedIn = true;
                        FirebaseClient.getInstance().activeGame = games.getRef();
                        FirebaseClient.getInstance().user = username;
                        Log.d("status", String.valueOf(username));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
