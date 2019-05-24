package tessting.monah.com.kukueats2;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseServerCode extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Enable Local Datastore
        Parse.enableLocalDatastore(this);

        //Add your initialization here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Enter Your Application id") // i could not enter my id due to security reasons
                .clientKey("Enter client key") //I could not enter my client key due to security reasons
                .server("Enter the link to your server") //I could not enter mine due to security reasons
                .build()
        );

       // ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        //Optionally enable public read access
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
