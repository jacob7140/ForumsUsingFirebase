package com.example.forums_firebase;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ForumsListFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final private String TAG = "data";


    public ForumsListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView recyclerView;
    ForumsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Forums List");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums_list, container, false);
        String userName = user.getDisplayName();
        String userEmail = user.getEmail();

        Log.d(TAG, "onCreateView: " + userName + " " + userEmail);

        view.findViewById(R.id.buttonForumsLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logOut();
                mAuth.signOut();
            }
        });

        recyclerView = view.findViewById(R.id.forumRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ForumsAdapter();
        recyclerView.setAdapter(adapter);

        setUpForumsListener();

        view.findViewById(R.id.buttonForumsAddForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoAddNewForum();
            }
        });



        return view;
    }

    private void setUpForumsListener(){
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();

        db1.collection("forums").orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    forumsList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot){
                        Forum forum = document.toObject(Forum.class);
                        forum.setForumId(document.getId());
                        forumsList.add(forum);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    error.printStackTrace();
                }
            }
        });

    }

    ForumsListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ForumsListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ForumsListListener");
        }
    }

    ArrayList<Forum> forumsList = new ArrayList<Forum>();

    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumViewHolder> {

        @NonNull
        @Override
        public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.forum_list_item, parent, false);
            return new ForumViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return forumsList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
            Forum forum = forumsList.get(position);
            holder.setupForumRow(forum);
        }

        class ForumViewHolder extends RecyclerView.ViewHolder{
            TextView textViewTitle, textViewDesc, textViewOwner, textViewLikesAndDate;
            ImageView imageViewLike, imageViewDeleteForum;
            Forum mForum;


            public ForumViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewDesc = itemView.findViewById(R.id.textViewComment);
                textViewOwner = itemView.findViewById(R.id.textViewOwner);
                textViewLikesAndDate = itemView.findViewById(R.id.textViewLikesAndDate);
                imageViewDeleteForum = itemView.findViewById(R.id.imageViewDeleteForum);
                imageViewLike = itemView.findViewById(R.id.imageViewLike);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.gotoForumDetails(mForum);
                    }
                });
            }

            public void setupForumRow(Forum forum){
                this.mForum = forum;
                textViewTitle.setText(mForum.getTitle());
                FirebaseFirestore db = FirebaseFirestore.getInstance();


                textViewDesc.setText(getSafeSubstring(mForum.getDesc(), 200));
                textViewOwner.setText(mForum.getCreatedByName());

                mForum.setCreatedByUid(mAuth.getCurrentUser().getUid());


                int likeCount = mForum.getLikedBy().size();
                String likeString;
                if (likeCount == 0){
                    likeString = "No Likes";
                } else if (likeCount == 1){
                    likeString = "1 Like";
                } else {
                    likeString = likeCount + " likes";
                }

                if (mForum.getLikedBy().contains(mAuth.getCurrentUser())){
                    imageViewLike.setImageResource(R.drawable.like_not_favorite);
                } else {
                    imageViewLike.setImageResource(R.drawable.like_favorite);
                }

                imageViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mForum.getLikedBy().contains(mAuth.getCurrentUser().getUid())){
                            imageViewLike.setImageResource(R.drawable.like_not_favorite);
                            mForum.likedBy.remove(mAuth.getCurrentUser().getUid());

                            HashMap<String, Object> unlikeUpdate = new HashMap<>();
                            db.collection("forums").document(mForum.getForumId());

                            Log.d(TAG, "onClick: Unliked Post" + mForum.getLikedBy());

                        } else {
                            mForum.likedBy.add(mAuth.getCurrentUser().getUid());
                            imageViewLike.setImageResource(R.drawable.like_favorite);
                            HashMap<String, Object> updateLikedBy = new HashMap<>();
                            updateLikedBy.put("likedBy", mForum.getLikedBy());
                            db.collection("forums").document(mForum.getForumId()).update(updateLikedBy);
                            Log.d(TAG, "onClick: Liked post" + mForum.getLikedBy());
                        }
                    }
                });


                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
                textViewLikesAndDate.setText(likeString + " | " + formatter.format(mForum.getCreatedAt().toDate()));

                if (mForum.getCreatedByUid().equals(mAuth.getCurrentUser().getUid())){
                    imageViewDeleteForum.setVisibility(View.VISIBLE);
                    imageViewDeleteForum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.collection("forums").document(mForum.getForumId()).delete();
                        }
                    });

                } else {
                    imageViewDeleteForum.setVisibility(View.INVISIBLE);
                }


            }
        }
    }



    public String getSafeSubstring(String s, int maxLength){
        if(!TextUtils.isEmpty(s)){
            if(s.length() >= maxLength){
                return s.substring(0, maxLength);
            }
        }
        return s;
    }

    interface ForumsListListener{
        void gotoForumDetails(Forum forum);
        void logOut();
        void gotoAddNewForum();
    }
}