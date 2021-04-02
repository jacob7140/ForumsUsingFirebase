package com.example.forums_firebase;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateForumFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userName = FirebaseAuth.getInstance().getCurrentUser();
    final private String TAG = "data";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CreateForumFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText editTextTitle, editTextDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Create new Forum");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_forum, container, false);
        editTextTitle = view.findViewById(R.id.editTextForumTitle);
        editTextDescription = view.findViewById(R.id.editTextForumDesc);


        view.findViewById(R.id.buttonCreateForumSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTextTitle.getText().toString();
                String desc = editTextDescription.getText().toString();

                Map<String, Object> user = new HashMap<>();
                user.put("createdByName", userName.getDisplayName());
                user.put("createdAt", Timestamp.now());
                user.put("createdByUid", userName.getUid());
                user.put("title", title);
                user.put("desc", desc);

                if (title.isEmpty() | desc.isEmpty()){
                    Toast.makeText(getActivity(), "Fields Can not be empty", Toast.LENGTH_SHORT).show();
                } else{
                    db.collection("forums")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                    mListener.goBackToForumsList();

                }



            }
        });

        view.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToForumsList();
            }
        });


        return view;
    }

    NewForumListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (CreateForumFragment.NewForumListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegisterListener");
        }
    }

    interface NewForumListener{
        void goBackToForumsList();
    }
}