package com.example.potatotank.dubhacks2015;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;

public class FirebaseClient {

    private static FirebaseClient reference;
    Firebase ref;

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

    public void AddTags(String[] tags){
        Firebase client = FirebaseClient.getInstance().ref;
        for(int i = 0; i < tags.length; i++) {

            if(i == 0)
                client.child("games/0/player1/tags/0").setValue(tags[i]);
            else if(i == 1)
                client.child("games/0/player1/tags/1").setValue(tags[i]);
            else if(i == 2)
                client.child("games/0/player1/tags/2").setValue(tags[i]);
            else
                System.out.println("Error: no tags");

        }
    }

}