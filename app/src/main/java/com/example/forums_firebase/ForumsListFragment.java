package com.example.forums_firebase;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;


public class ForumsListFragment extends Fragment {
//    private static final String ARG_PARAM_AUTH_RES = "ARG_PARAM_AUTH_RES";
//
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        return view;
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

//    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumViewHolder> {
//
//        @NonNull
//        @Override
//        public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.forum_list_item, parent, false);
//            return new ForumViewHolder(view);
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
//            DataServices.Forum forum = forums.get(position);
//            holder.setupForumItem(forum);
//        }
//
//        class ForumViewHolder extends RecyclerView.ViewHolder{
//            TextView textViewTitle, textViewDesc, textViewOwner, textViewLikesAndDate;
//            ImageView imageViewLike, imageViewDeleteForum;
//            DataServices.Forum mForum;
//
//
//            public ForumViewHolder(@NonNull View itemView) {
//                super(itemView);
//                textViewTitle = itemView.findViewById(R.id.textViewTitle);
//                textViewDesc = itemView.findViewById(R.id.textViewDesc);
//                textViewOwner = itemView.findViewById(R.id.textViewOwner);
//                textViewLikesAndDate = itemView.findViewById(R.id.textViewLikesAndDate);
//                imageViewLike = itemView.findViewById(R.id.imageViewLike);
//                imageViewDeleteForum = itemView.findViewById(R.id.imageViewDeleteForum);
//
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mListener.gotoForumDetails(mForum);
//                    }
//                });
//            }
//
//            public void setupForumItem(DataServices.Forum forum){
//                this.mForum = forum;
//                textViewTitle.setText(forum.getTitle());
//
//                String desc200 = forum.getDescription().substring(0, Math.min(200, forum.getDescription().length()));
//
//                textViewDesc.setText(desc200);
//                textViewOwner.setText(forum.getCreatedBy().getName());
//
//                int likeCount = forum.getLikedBy().size();
//                String likeString = "No Likes";
//                if(likeCount == 1){
//                    likeString = "1 Like";
//                } else {
//                    likeString = likeCount + " Likes";
//                }
//
//                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
//                textViewLikesAndDate.setText(likeString + " | " + formatter.format(forum.getCreatedAt()));
//
//                if(forum.getCreatedBy().uid == mAuthResponse.getAccount().uid){
//                    imageViewDeleteForum.setVisibility(View.VISIBLE);
//                    imageViewDeleteForum.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new DeleteForumTask(mForum.getForumId(), mAuthResponse.getToken()).execute();
//                        }
//                    });
//                } else {
//                    imageViewDeleteForum.setVisibility(View.INVISIBLE);
//                }
//
//                if(forum.getLikedBy().contains(mAuthResponse.getAccount())){
//                    imageViewLike.setImageResource(R.drawable.like_favorite);
//                } else {
//                    imageViewLike.setImageResource(R.drawable.like_not_favorite);
//                }
//
//                imageViewLike.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(forum.getLikedBy().contains(mAuthResponse.getAccount())){
//                            new LikeOrUnlikeForumTask(mForum.getForumId(), mAuthResponse.getToken(), false).execute();
//                        } else {
//                            new LikeOrUnlikeForumTask(mForum.getForumId(), mAuthResponse.getToken(), true).execute();
//                        }
//                    }
//                });
//
//
//
//
//            }
//        }
//    }

    interface ForumsListListener{
        void gotoForumDetails();
        void logOut();
        void gotoAddNewForum();
    }
}