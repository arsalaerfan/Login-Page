package com.example.yummly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText userId, password, name;
    Button register;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        // insert queries
        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());

        //initialize user DAO
        UserDAO userDAO = userDatabase.userDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users = userDAO.getAllUsers();

                if (users.size() <= 0){
                    User defaltUser = new User("testuser1", "testuser1", "testuser1", false);
                    userDAO.insert(defaltUser);

                    User defaltUseradmin = new User("admin2", "admin2", "admin2", true);
                    userDAO.insert(defaltUseradmin);
                }

            }
        }).start();
        //====



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we create user entity
                User user = new User();
                user.setUserId(userId.getText().toString());
                user.setPassword(password.getText().toString());
                user.setName(name.getText().toString());

                if (validateInput(user)){
                    //Initialize User Database
                    // insert queries
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());

                    //initialize user DAO
                    UserDAO userDAO = userDatabase.userDAO();

                    //insert operation on thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //call register method to register user
                            userDAO.registerUser(user);

                            //always ui tank in ui thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "User Registered!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not all fields filled!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
    }

    private Boolean validateInput(User user){
        if (user.getName().isEmpty() ||
            user.getPassword().isEmpty() ||
            user.getName().isEmpty()){

            return false;
        }
        return true;
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }
}