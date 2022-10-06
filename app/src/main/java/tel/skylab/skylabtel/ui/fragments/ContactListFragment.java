package tel.skylab.skylabtel.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.alphabetik.Alphabetik;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.adapter.AdapterContactList;
import tel.skylab.skylabtel.models.ContactModel;
import tel.skylab.skylabtel.ui.activities.DashboardActivity;
import tel.skylab.skylabtel.utils.Constants;

/** A simple {@link Fragment} subclass. */
public class ContactListFragment extends Fragment {

    public DashboardActivity dashboard;

    MatrixCursor mMatrixCursor;
    ArrayList<ContactModel> mArrayList;
    ArrayList<ContactModel> mSearchList;
    ArrayList<ContactModel> SavedList;
    RecyclerView recycler;
    ProgressDialog pb;
    EditText et_Search_contact;
    AdapterContactList Adapter;
    ImageView iv_save_btn;
    private SwipeRefreshLayout swipRefresh;
    Alphabetik alphabetik;
    String status;
    int i = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        swipRefresh = view.findViewById(R.id.swipRefresh);
        recycler = view.findViewById(R.id.recycler);
        et_Search_contact = view.findViewById(R.id.et_Search_contact);
        iv_save_btn = view.findViewById(R.id.iv_save_btn);
        alphabetik = (Alphabetik) view.findViewById(R.id.alphSectionIndex);

        SharedPreferences preferences =
                getContext().getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        status = preferences.getString(Constants.PREF_SAVED_CONTACTS_STATUS, "no");
        if (status.equalsIgnoreCase("yes")) {
            String response = preferences.getString(Constants.PREF_SAVED_CONTACTS, null);
            if (response != null) {
                mArrayList = new ArrayList<ContactModel>();
                mSearchList = new ArrayList<ContactModel>();
                Gson gson = new Gson();
                mArrayList =
                        gson.fromJson(
                                response, new TypeToken<ArrayList<ContactModel>>() {}.getType());
                if (mArrayList.size() > 0) {
                    mSearchList.addAll(mArrayList);

                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    Adapter = new AdapterContactList(getActivity(), mSearchList);
                    recycler.setAdapter(Adapter);
                }
            }
        } else {
            mMatrixCursor = new MatrixCursor(new String[] {"_id", "name", "photo", "details"});

            // Creating an AsyncTask object to retrieve and load RecyclerView with
            // contacts
            ListViewContactsLoader listViewContactsLoader = new ListViewContactsLoader();

            // Starting the AsyncTask process to retrieve and load RecyclerView with
            // contacts
            listViewContactsLoader.execute();
        }

        alphabetik.onSectionIndexClickListener(
                new Alphabetik.SectionIndexClickListener() {
                    @Override
                    public void onItemClick(View view, int position, String character) {
                        String info = " Position = " + position + " Char = " + character;
                        Log.i("View: ", view + "," + info);
                        // Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
                        recycler.smoothScrollToPosition(getPositionFromData(character));
                    }
                });

        swipRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        i = 1;

                        mMatrixCursor =
                                new MatrixCursor(new String[] {"_id", "name", "photo", "details"});

                        // Creating an AsyncTask object to retrieve
                        ListViewContactsLoader listViewContactsLoader =
                                new ListViewContactsLoader();
                        // Starting the AsyncTask process to retrieve
                        listViewContactsLoader.execute();
                    }
                });

        et_Search_contact.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mSearchList.clear();
                        if (s.equals("")) {
                            mSearchList.addAll(mArrayList);
                        } else {
                            for (int i = 0; i < mArrayList.size(); i++) {
                                if (mArrayList.get(i).getName() != null
                                        && mArrayList
                                                .get(i)
                                                .getName()
                                                .toLowerCase()
                                                .contains(s.toString().toLowerCase())) {
                                    mSearchList.add(mArrayList.get(i));
                                }
                            }
                        }
                        //                Adapter.updateAdapter(mSearchList);
                        Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

        iv_save_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        contactIntent
                                .putExtra(ContactsContract.Intents.Insert.NAME, "")
                                .putExtra(ContactsContract.Intents.Insert.PHONE, "");
                        startActivityForResult(contactIntent, 1);
                    }
                });

        return view;
    }

    private class ListViewContactsLoader extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (i == 1) {
                swipRefresh.setRefreshing(true);
            } else {
                pb = new ProgressDialog(getActivity());
                pb.setTitle("Refresh your device contacts");
                pb.setMessage("Please wait.....");
                pb.setCancelable(false);
                pb.show();
            }
        }

        @Override
        protected Cursor doInBackground(Void... params) {

            Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;

            // Querying the table ContactsContract.Contacts to retrieve all the
            // contacts
            Cursor contactsCursor =
                    getActivity()
                            .getContentResolver()
                            .query(
                                    contactsUri,
                                    null,
                                    null,
                                    null,
                                    ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

            //            if (contactsCursor.moveToFirst()) {
            if (contactsCursor != null && contactsCursor.moveToFirst()) {
                do {
                    long contactId = contactsCursor.getLong(contactsCursor.getColumnIndex("_ID"));

                    Uri dataUri = ContactsContract.Data.CONTENT_URI;

                    // Querying the table ContactsContract.Data to retrieve
                    // individual items like
                    // home phone, mobile phone, work email etc corresponding to
                    // each contact
                    Cursor dataCursor =
                            getActivity()
                                    .getContentResolver()
                                    .query(
                                            dataUri,
                                            null,
                                            ContactsContract.Data.CONTACT_ID + "=" + contactId,
                                            null,
                                            null);

                    String displayName = "";
                    String homePhone = "";
                    String mobilePhone = "";
                    String workPhone = "";
                    String photoPath = "" + R.drawable.ic_launcher_background;
                    byte[] photoByte = null;

                    if (dataCursor != null && dataCursor.moveToFirst()) {
                        // Getting Display Name
                        displayName =
                                dataCursor.getString(
                                        dataCursor.getColumnIndex(
                                                ContactsContract.Data.DISPLAY_NAME));
                        do {

                            // Getting Phone numbers
                            if (dataCursor
                                    .getString(dataCursor.getColumnIndex("mimetype"))
                                    .equals(
                                            ContactsContract.CommonDataKinds.Phone
                                                    .CONTENT_ITEM_TYPE)) {
                                switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                        homePhone =
                                                dataCursor.getString(
                                                        dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                        mobilePhone =
                                                dataCursor.getString(
                                                        dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                        workPhone =
                                                dataCursor.getString(
                                                        dataCursor.getColumnIndex("data1"));
                                        break;
                                }
                            }

                            // Getting Photo
                            if (dataCursor
                                    .getString(dataCursor.getColumnIndex("mimetype"))
                                    .equals(
                                            ContactsContract.CommonDataKinds.Photo
                                                    .CONTENT_ITEM_TYPE)) {
                                photoByte = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));

                                if (photoByte != null) {
                                    Bitmap bitmap =
                                            BitmapFactory.decodeByteArray(
                                                    photoByte, 0, photoByte.length);

                                    // Getting Caching directory
                                    File cacheDirectory = getActivity().getCacheDir();

                                    // Temporary file to store the contact image
                                    File tmpFile =
                                            new File(
                                                    cacheDirectory.getPath()
                                                            + "/wpta_"
                                                            + contactId
                                                            + ".png");

                                    // The FileOutputStream to the temporary
                                    // file
                                    try {
                                        FileOutputStream fOutStream = new FileOutputStream(tmpFile);

                                        // Writing the bitmap to the temporary
                                        // file as png file
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);

                                        // Flush the FileOutputStream
                                        fOutStream.flush();

                                        // Close the FileOutputStream
                                        fOutStream.close();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    photoPath = tmpFile.getPath();
                                }
                            }

                        } while (dataCursor.moveToNext());

                        String details = "";

                        // Concatenating various information to single string

                        if (mobilePhone != null && !mobilePhone.equals("")) details = mobilePhone;

                        // Adding id, display name, path to photo and other
                        // details to cursor
                        mMatrixCursor.addRow(
                                new Object[] {
                                    Long.toString(contactId), displayName, photoPath, details
                                });
                    }

                } while (contactsCursor.moveToNext());
            }
            return mMatrixCursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            // Setting the cursor containing contacts to RecyclerView
            /*if (pb != null) {
                pb.dismiss();
            }*/

            mArrayList = new ArrayList<>();
            mSearchList = new ArrayList<>();
            for (mMatrixCursor.moveToFirst();
                    !mMatrixCursor.isAfterLast();
                    mMatrixCursor.moveToNext()) {
                // The Cursor is now set to the right position
                int ids = mMatrixCursor.getColumnIndex("_id");
                int idName = mMatrixCursor.getColumnIndex("name");
                int idPhoto = mMatrixCursor.getColumnIndex("photo");
                int idDetails = mMatrixCursor.getColumnIndex("details");
                String idss = mMatrixCursor.getString(ids);
                String name = mMatrixCursor.getString(idName);
                String photo = mMatrixCursor.getString(idPhoto);
                String details = mMatrixCursor.getString(idDetails);
                if (details != null) {
                    if (!details.equalsIgnoreCase("")) {
                        details = details.replaceAll(" ", "");
                        details = details.replaceAll("-", "");
                        details = details.replaceAll("[\\\\p{Ps}\\\\p{Pe}]", "");
                        mArrayList.add(new ContactModel(idss, name, details, photo, false));
                    }
                }
            }

            mSearchList.addAll(mArrayList);

            Gson gson = new Gson();
            String json = gson.toJson(mSearchList);
            SharedPreferences preferences =
                    getActivity()
                            .getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.PREF_SAVED_CONTACTS, json);
            editor.putString(Constants.PREF_SAVED_CONTACTS_STATUS, "yes");
            editor.apply();

            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            //            Adapter = new AdapterContactList(getActivity(), mSearchList);
            Adapter = new AdapterContactList(getActivity(), mSearchList);
            recycler.setAdapter(Adapter);

            if (i == 1) {
                swipRefresh.setRefreshing(false);
            } else {
                if (pb != null) {
                    pb.dismiss();
                }
            }
        }
    }

    private int getPositionFromData(String character) {
        int position = 0;
        for (ContactModel s : mSearchList) {
            String letter = "" + s.getName().charAt(0);
            if (letter.equals("" + character)) {
                return position;
            }
            position++;
        }
        return 0;
    }
}
