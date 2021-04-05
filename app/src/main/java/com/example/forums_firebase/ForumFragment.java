package com.example.forums_firebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForumFragment extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final private String TAG = "data";
    private Forum mForum;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String ARG_PARAM_AUTH = "ARG_PARAM_AUTH";
    private static final String ARG_PARAM_FORUM = "ARG_PARAM_FORUM";


    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance(Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForum = (Forum) getArguments().getSerializable(ARG_PARAM_FORUM);
        }
    }

    TextView textViewForumTitle, textViewForumOwnerName, textViewForumDesc, textViewNumComments;
    EditText editTextTextComment;
    RecyclerView recyclerView;
    CommentsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Comment> commentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Forum");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        textViewForumTitle = view.findViewById(R.id.textViewForumTitle);
        textViewForumOwnerName = view.findViewById(R.id.textViewForumOwnerName);
        textViewForumDesc = view.findViewById(R.id.textViewForumDesc);
        textViewNumComments = view.findViewById(R.id.textViewNumComments);

        editTextTextComment = view.findViewById(R.id.editTextTextComment);
        recyclerView = view.findViewById(R.id.forumRecyclerView);

        textViewForumTitle.setText(mForum.getTitle());
        textViewForumOwnerName.setText(mForum.getCreatedByName());
        textViewForumDesc.setText(mForum.getDesc());
        textViewNumComments.setText("");

        view.findViewById(R.id.buttonPostSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editTextTextComment.getText().toString();

                if (comment.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Comment Text", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> newComment = new HashMap<>();
                    newComment.put("comment", comment);
                    newComment.put("createdAt", Timestamp.now());
                    newComment.put("createdByUid", user.getUid());
                    newComment.put("createdByName", user.getDisplayName());

                    db.collection("forums").document(mForum.getForumId()).collection("comment").add(newComment)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "onSuccess: Created new comment");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Failed to create new comment");
                                }
                            });
                }

                editTextTextComment.setText("");


            }

        });

        recyclerView = view.findViewById(R.id.forumRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentsAdapter();
        recyclerView.setAdapter(adapter);
        setUpCommentListener();


        db.collection("forums").document(mForum.getForumId()).collection("comment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot task, @Nullable FirebaseFirestoreException error) {
                int count = 0;
                for (QueryDocumentSnapshot document : task){
                    count++;
                }
                String listSize = String.valueOf(count);
                textViewNumComments.setText(listSize);
            }
        });

        return view;
    }


    private void setUpCommentListener(){
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();

        db1.collection("forums").document(mForum.getForumId()).collection("comment")
                .orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    commentList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot){
                        Comment comment = document.toObject(Comment.class);
                        comment.setCommentId(document.getId());
                        commentList.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    error.printStackTrace();
                }
            }
        });

    }

    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_list_item, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = commentList.get(position);
            holder.setupForumRow(comment);
        }

        class CommentViewHolder extends RecyclerView.ViewHolder{
            TextView textViewComment, textViewOwner, textViewDate;
            ImageView imageViewDeleteForum;
            Comment mComment;


            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewComment = itemView.findViewById(R.id.textViewComment);
                textViewOwner = itemView.findViewById(R.id.textViewOwner);
                textViewDate = itemView.findViewById(R.id.textViewDate);
                imageViewDeleteForum = itemView.findViewById(R.id.imageViewDeleteComment);

            }

            public void setupForumRow(Comment comment){
                this.mComment = comment;
                FirebaseFirestore db = FirebaseFirestore.getInstance();


                textViewComment.setText(mComment.getComment());
                textViewOwner.setText(mComment.getCreatedByName());

                mForum.setCreatedByUid(mAuth.getCurrentUser().getUid());


                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
                textViewDate.setText(formatter.format(mComment.getCreatedAt().toDate()));


                String createdByUid = mComment.getCreatedByUid();
                String currentUid = mAuth.getCurrentUser().getUid();

                if (createdByUid.equals(currentUid)){
                    imageViewDeleteForum.setVisibility(View.VISIBLE);
                    imageViewDeleteForum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.collection("forums").document(mForum.getForumId()).collection("comment").document(comment.getCommentId()).delete();
                        }
                    });

                } else {
                    imageViewDeleteForum.setVisibility(View.INVISIBLE);
                }



            }
        }
    }

}