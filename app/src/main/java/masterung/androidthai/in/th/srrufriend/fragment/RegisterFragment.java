package masterung.androidthai.in.th.srrufriend.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import masterung.androidthai.in.th.srrufriend.MainActivity;
import masterung.androidthai.in.th.srrufriend.R;
import masterung.androidthai.in.th.srrufriend.utility.AddUser;

public class RegisterFragment extends Fragment {

    private ImageView imageView;
    private Uri uri;
    private boolean aBoolean = true;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Avata Controller
        imageView = getView().findViewById(R.id.imvAvata);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose App"), 1);

            }
        });


    }   // Main Method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itmeUpload) {

            uploadValueToServer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadValueToServer() {

        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText userEditText = getView().findViewById(R.id.edtUser);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);

        String nameString = nameEditText.getText().toString().trim();
        String userString = userEditText.getText().toString().trim();
        String passwordString = passwordEditText.getText().toString().trim();

        if (aBoolean) {
            alertMessage("Non Choose Avata");
        } else if (nameString.isEmpty() || userString.isEmpty() || passwordString.isEmpty()) {
            alertMessage("Please Fill All Blank");
        } else {

//            upload Image Avata
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                    .Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String[] strings = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver()
                    .query(uri, strings, null, null, null);

            String pathAvataString = null;

            if (cursor != null) {

                cursor.moveToFirst();
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                pathAvataString = cursor.getString(index);

            } else {

                pathAvataString = uri.getPath();

            }


            Log.d("29JuneV1", "Path ==> " + pathAvataString);

            File file = new File(pathAvataString);
            FTPClient ftpClient = new FTPClient();

            try {

                ftpClient.connect("ftp.androidthai.in.th", 21);
                ftpClient.login("srru@androidthai.in.th", "Abc12345");
                ftpClient.setType(FTPClient.TYPE_BINARY);
                ftpClient.changeDirectory("Avata");
                ftpClient.upload(file, new MyUploadAvata());


            } catch (Exception e) {
                e.printStackTrace();

                try {
                    ftpClient.disconnect(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } // try1

//            upload Data to mySQL
            String nameAvata = pathAvataString.substring(pathAvataString.lastIndexOf("/"));
            nameAvata = "http://androidthai.in.th/srru/" + nameAvata;

            try {

                AddUser addUser = new AddUser(getActivity());
                addUser.execute(
                        nameString,
                        userString,
                        passwordString,
                        nameAvata,
                        "http://androidthai.in.th/srru/addData.php");

                if (Boolean.parseBoolean(addUser.get())) {

                    getActivity().getSupportFragmentManager().popBackStack();

                } else {

                    alertMessage("Cannot Upload");

                }



            } catch (Exception e) {
                e.printStackTrace();
            }

        }   //if

    }   // upload

    public class MyUploadAvata implements FTPDataTransferListener{
        @Override
        public void started() {
            alertMessage("Start Upload Avata");
        }

        @Override
        public void transferred(int i) {
            alertMessage("Continue Upload Avata");
        }

        @Override
        public void completed() {
            alertMessage("Success Upload Avata");
        }

        @Override
        public void aborted() {

        }

        @Override
        public void failed() {

        }
    }




    private void alertMessage(String strMessage) {

        Toast.makeText(getActivity(), strMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_register, menu);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == getActivity().RESULT_OK) {

            uri = data.getData();
            aBoolean = false;

            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));

                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 800, 600, true);


                imageView.setImageBitmap(bitmap1);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }   // if

    }   // Result

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Register");

        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}
