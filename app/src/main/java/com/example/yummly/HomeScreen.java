package com.example.yummly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.gymlogfinal.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.gymlogfinal.PREFERENCES_KEY";

    TextView tName;

    private int mUserId = -1;

    private SharedPreferences mPreferences = null;

    private UserDAO mGymLogDAO;

    private User mUser;

    Button tomatoButton, steakButton, baguetteButton;

    String choices = "";
    Double price = 0.00;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //methods
        checkForUser();
        addUserToPreference(mUserId);
        //loginUser(mUserId);


        tName = findViewById(R.id.name);
        String name = getIntent().getStringExtra("name");
        tName.setText(name);

        tomatoButton = (Button) findViewById(R.id.tomatoButton);
        steakButton = (Button) findViewById(R.id.steakButton);
        baguetteButton = (Button) findViewById(R.id.baguetteButton);

    }

    public void add_to_list(View view){
        if (view == findViewById(R.id.tomatoButton)){
            choices += "1 Tomato $1"+"\n";
            price += 1;
        }

        if (view == findViewById(R.id.steakButton)){
            choices += "1 Steak $3"+"\n";
            price += 3;
        }

        if (view == findViewById(R.id.baguetteButton)){
            choices += "1 Baguette $5"+"\n";
            price += 5;
        }
    }
    public void placeOrder(View view){
        Intent i = new Intent(HomeScreen.this, OrderDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString("choices", choices);
        bundle.putDouble("price", price);
        i.putExtras(bundle);
        startActivity(i);
    }

//    private void loginUser(int userId){
//        mUser = UserDAO.getUserByUserId(userId);
//        invalidateOptionsMenu();
//    }


    //TODO: finish
    private void addUserToPreference(int userId) {
        if (mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
    }

    private void checkForUser() {
        //do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        //do we have user in the preferences?
        if (mUserId != -1){
            return;
        }



        if (mPreferences == null){
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if (mUserId != -1){
            return;
        }

        //do we have any users at all
//        List<User> users = mGymLogDAO.getAllUsers();
//        if (users.size() <= 0){
//            User defaltUser = new User("testuser1", "testuser1", "testuser1", false);
//            mGymLogDAO.insert(defaltUser);
//        }

        Intent intent = MainActivity.intentFactory(this);
        startActivity(intent);


    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we don't need anything here
                    }
                });

        alertBuilder.create().show();
    }

    //clear From Intent
    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY, -1);
    }
    //TODO: implement removing user from preferences.
    private void clearUserFromPref(){
        //Toast.makeText(this,"not inplemented", Toast.LENGTH_SHORT).show();
        addUserToPreference(-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.item2:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent intentFactory(Context context, int mUserId, String name){
        Intent intent = new Intent(context, HomeScreen.class);
        intent.putExtra(USER_ID_KEY, mUserId);
        intent.putExtra("name", name);

        return intent;
    }

}