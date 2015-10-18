package com.example.potatotank.dubhacks2015;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class FirebaseClient {

    private static FirebaseClient reference;
    Firebase ref;
    boolean isPOne = true;
    String user = "";
    String checkid = "";
    Firebase activeGame;


    public FirebaseClient() {
        ref = new Firebase("https://dubhacks2015.firebaseio.com/");
    }

    public static FirebaseClient getInstance()
    {
        if (reference == null)
        {
            synchronized(FirebaseClient.class)
            {
                if (reference == null)
                {
                    System.out.println("getInstance(): First time getInstance was invoked!");
                    reference = new FirebaseClient();
                }
            }
        }

        return reference;
    }


    //encoding bitMap image to a string for database storage
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    //decode a string back to a bitMap image
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void setLastImg(String image, String usrid){
        Firebase Rootref = new Firebase("https://dubhacks2015.firebaseio.com/");
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                checkid = snapshot.child("games/0/player1/user").getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        if (checkid == usrid) {
            Rootref.child("games/0/player1/lastImg").setValue(image);
        }else{
            Rootref.child("games/0/player2/lastImg").setValue(image);
        }
    }

    public void AddTags(String[] tags){
        Firebase client = FirebaseClient.getInstance().ref;
        if(isPOne == true) {
            for (int i = 0; i < tags.length; i++) {

                if (i == 0)
                    client.child("games/0/player1/tags/0").setValue(tags[i]);
                else if (i == 1)
                    client.child("games/0/player1/tags/1").setValue(tags[i]);
                else if (i == 2)
                    client.child("games/0/player1/tags/2").setValue(tags[i]);
                else
                    System.out.println("Error: no tags");

            }
        }
        else{
            for (int i = 0; i < tags.length; i++) {

                if (i == 0)
                    client.child("games/0/player2/tags/0").setValue(tags[i]);
                else if (i == 1)
                    client.child("games/0/player2/tags/1").setValue(tags[i]);
                else if (i == 2)
                    client.child("games/0/player2/tags/2").setValue(tags[i]);
                else
                    System.out.println("Error: no tags");

            }

        }
    }

    public void findSelf() {
//        if (this.user.equals(this.activeGame.child("player1").child("user").getValue())) {
//
//        }
    }

    public void findOpponent() {

    }

    //input are current user and with the taglist that current user's image generated
//    private boolean compareListwithTag(String usrid, ArrayList<String> taglist){
//        Firebase Rootref = new Firebase("https://dubhacks2015.firebaseio.com/");
//        if(usrid == "demo"){
//            ArrayList<String> list2compare = Rootref.child("games/0/player1/tags").getValue();
//        }
//        ArrayList<String> list2compare = Rootref.child("games/0/player2/tags").getValue();
//        for(int i=0; i< list2compare.size(); i++){
//            for(int j=0; j< taglist.size(); j++){
//                if (taglist.get(j) == list2compare.get(i)){
//                    // current user succeeded
//                    // user goes back to MainActivity
////                    Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
////                    goToMainActivity.putExtra("KEY", file);
////                    startActivity(goToMainActivity);
//                    return true;
//                }
//            }
//
//        }
//        return false;
//    }


}