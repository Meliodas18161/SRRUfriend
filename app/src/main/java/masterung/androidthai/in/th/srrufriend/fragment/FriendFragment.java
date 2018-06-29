package masterung.androidthai.in.th.srrufriend.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import masterung.androidthai.in.th.srrufriend.R;
import masterung.androidthai.in.th.srrufriend.utility.FriendAdapter;
import masterung.androidthai.in.th.srrufriend.utility.ReadAllData;

public class FriendFragment extends Fragment {

    private ArrayList<String> loginStringArrayList;

    public static FriendFragment friendInstant(ArrayList arrayList) {

        FriendFragment friendFragment = new FriendFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Login", arrayList);
        friendFragment.setArguments(bundle);
        return friendFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginStringArrayList = getArguments().getStringArrayList("Login");
        Log.d("30JuneV1", "Receive Login ==> " + loginStringArrayList.toString());

//        Create RecyclerView
        try {

            ReadAllData readAllData = new ReadAllData(getActivity());
            readAllData.execute("http://androidthai.in.th/srru/getAllData.php");
            String jsonString = readAllData.get();

            ArrayList<String> avataStringArrayList = new ArrayList<>();
            ArrayList<String> nameFrinedStringArrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i=0; i<jsonArray.length(); i+=1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                avataStringArrayList.add(jsonObject.getString("Avata"));
                nameFrinedStringArrayList.add(jsonObject.getString("Name"));
            }

            RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewFriend);
            FriendAdapter friendAdapter = new FriendAdapter(getActivity(),
                    avataStringArrayList, nameFrinedStringArrayList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(friendAdapter);


        } catch (Exception e) {
            Log.d("30JuneV1", "e ==> " + e.toString());
        }



    }   // Main Method

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        return view;
    }
}
