package com.example.forums_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, ForumsListFragment.ForumsListListener, CreateAccountFragment.RegisterListener,
        CreateForumFragment.NewForumListener {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.rootView, new ForumsListFragment()).commit();
        }


    }

    @Override
    public void gotoForumsList() {
        getSupportFragmentManager().beginTransaction().replace(R.id.rootView, new ForumsListFragment()).commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoCreateAccount() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new CreateAccountFragment())
                .commit();
    }

    @Override
    public void gotoForumDetails(Forum forum) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, ForumFragment.newInstance(forum))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logOut() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();

    }

    @Override
    public void gotoAddNewForum() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new CreateForumFragment())
                .commit();
    }


    @Override
    public void goBackToForumsList() {
        getSupportFragmentManager().beginTransaction().replace(R.id.rootView, new ForumsListFragment()).commit();
    }
}