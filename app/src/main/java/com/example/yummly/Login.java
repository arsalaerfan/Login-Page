package com.example.yummly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Login extends AppCompatActivity {

    EditText userId, password;
    Button login;

    private User emUser;
    private UserDAO mGymLogDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        userId = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIdText = userId.getText().toString();
                String passwordText = password.getText().toString();

                if (userIdText.isEmpty() || passwordText.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    //database initialization, Queries
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                    //initializing DAO
                    UserDAO userDAO = userDatabase.userDAO();


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        //calling login method
                            User user = userDAO.login(userIdText, passwordText);
                            if (user == null){
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Invalid Userid or Password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                //go to home screen
                                String name = user.name;

                                Intent intent = HomeScreen.intentFactory(getApplicationContext(), user.getId(), name);

                                startActivity(intent);

                            }
                        }
                    }).start();
                }
            }
        });
    }


    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, Login.class);

        return intent;
    }
}