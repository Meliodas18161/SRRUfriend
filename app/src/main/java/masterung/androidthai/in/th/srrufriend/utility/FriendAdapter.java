package masterung.androidthai.in.th.srrufriend.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import masterung.androidthai.in.th.srrufriend.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    private Context context;
    private ArrayList<String> avataStringArrayList, nameStringArrayList;
    private LayoutInflater layoutInflater;

    public FriendAdapter(Context context,
                         ArrayList<String> avataStringArrayList,
                         ArrayList<String> nameStringArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.avataStringArrayList = avataStringArrayList;
        this.nameStringArrayList = nameStringArrayList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.recyclerview_friend, viewGroup, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);

        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder friendViewHolder, int i) {

        String pathUrl = avataStringArrayList.get(i);
        String nameFriend = nameStringArrayList.get(i);

        Picasso.get().load(pathUrl).resize(150, 150).into(friendViewHolder.circleImageView);

        friendViewHolder.textView.setText(nameFriend);

    }

    @Override
    public int getItemCount() {
        return avataStringArrayList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private TextView textView;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.imvAvata);
            textView = itemView.findViewById(R.id.txtNameFriend);

        }
    }


}
