package com.example.potatotank.dubhacks2015;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private boolean playerLoggedIn = false;
    public static int count = 0;
    String dir;

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

//        final TextView textView = (TextView) findViewById(R.id.textView);
//        Button capture = (Button) findViewById(R.id.button_camera);
        Button loginP1 = (Button) findViewById(R.id.button_login1);
        Button loginP2 = (Button) findViewById(R.id.button_login2);

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

        loginP1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tryLogin("demo","demopwd");
            }
        });

        loginP2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tryLogin("test","testtest");
            }
        });
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         File f = null;

            try {
                f = setUpPhotoFile();
                mCurrentPhotoPath = f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                mCurrentPhotoPath = null;
            }

        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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

        FirebaseClient.getInstance().ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {

                    if (username.equals(user.getKey())) {
                        Log.d("status1", String.valueOf(playerLoggedIn));
                        if (password.equals(user.child("pwd"))) {
                            DataSnapshot games = user.child("games");

                            playerLoggedIn = true;
                            Log.d("status", String.valueOf(playerLoggedIn));
                            break;
                        }
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
