package tessting.monah.com.urbanfoods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public void goToMapActivity(){
        Intent intent = new Intent(getApplicationContext(), Client.class);
        startActivity(intent);
    }

    Boolean signUpModeActive = true;
    TextView switchSignUp;

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.switchSignUP){

            Button signUpButton = findViewById(R.id.SignUp);

            if(signUpModeActive) {

                signUpModeActive = false;
                signUpButton.setText("Login");
                switchSignUp.setText("or, Signup");
            }

            else{
                signUpModeActive = true;
                signUpButton.setText("SignUp");
                switchSignUp.setText("or, Login");
            }

            Toast.makeText(MainActivity.this, "Tap grey button to Sign Up or Login", Toast.LENGTH_LONG).show();

        }

    }

    public void signUp (View view){
        EditText userNameEditText = findViewById(R.id.userNameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        if (userNameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")
                ||phoneNumberEditText.getText().toString().matches("")){

            Toast.makeText(this, "Username, Password, and Phone Number Required", Toast.LENGTH_LONG).show();
        }

        else {

            if (signUpModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(userNameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.put("phone_number", phoneNumberEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "sign up successful", Toast.LENGTH_LONG).show();

                            goToMapActivity();

                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
            else {
                ParseUser.logInInBackground(userNameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null){
                            goToMapActivity();
                        }
                        else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        switchSignUp = findViewById(R.id.switchSignUP);

        switchSignUp.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            goToMapActivity();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
