package masterung.androidthai.in.th.srrufriend.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import masterung.androidthai.in.th.srrufriend.R;
import masterung.androidthai.in.th.srrufriend.utility.ReadAllData;

public class MainFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Register Controller
        registerController();

//        Login Controller
        loginController();


    }   // Main Method

    private void loginController() {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText userEditText = getView().findViewById(R.id.edtUser);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);

                String userString = userEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();

                if (userString.isEmpty() || passwordString.isEmpty()) {
                    alertMessage("Please Fill All Every Blank");
                } else {

                    try {

                        ReadAllData readAllData = new ReadAllData(getContext());
                        readAllData.execute("http://androidthai.in.th/srru/getAllData.php");
                        String jasonStrign = readAllData.get();
                        Log.d("29JuneV1", "JSON ==> " + jasonStrign);

                        String[] strings = new String[]{"id", "Name", "User", "Password", "Avata", "Post"};
                        ArrayList<String> stringArrayList = new ArrayList<>();
                        boolean b = true;

                        JSONArray jsonArray = new JSONArray(jasonStrign);
                        for (int i = 0; i < jsonArray.length(); i += 1) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (userString.equals(jsonObject.getString("User"))) {
                                b = false;
                                for (int i1=0; i1<strings.length; i1+=1) {
                                    stringArrayList.add(jsonObject.getString(strings[i1]));
                                }
                            }
                        }

                        Log.d("30JuneV1", "Login ==> " + stringArrayList.toString());

                        if (b) {
                            alertMessage("No " + userString + " in my Database");
                        } else if (passwordString.equals(stringArrayList.get(3))) {
                            alertMessage("Welcome " + stringArrayList.get(1));
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.contentMainFragment, FriendFragment.friendInstant(stringArrayList))
                                    .commit();
                        } else {
                            alertMessage("Please Try Again Password False");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        });
    }

    private void alertMessage(String strMessage) {
        Toast.makeText(getActivity(), strMessage, Toast.LENGTH_SHORT).show();
    }

    private void registerController() {
        TextView textView = getView().findViewById(R.id.txtNewRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Replace Fragment
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragment, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();


            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}   // Main Class
