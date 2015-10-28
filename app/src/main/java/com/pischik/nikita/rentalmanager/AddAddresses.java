package com.pischik.nikita.rentalmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;



import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class AddAddresses extends AppCompatActivity implements View.OnClickListener{

    private EditText streetField;
    private EditText cityField;
    private EditText stateField;
    private EditText monthlyRentField;
    private EditText landlordField;
    private TextView dateInField;
    private TextView dateOutField;
    private Button addToolbarButton;
    private Button cancelToolbarButton;
    private String mode;
    private int numEditElement;

    private boolean isCallInDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_addresses);
        mode = getIntent().getStringExtra("mode");
        numEditElement = getIntent().getIntExtra("num", -1);
        setupToolbar();
        if (mode.equals("edit")) {
            ((TextView) findViewById(R.id.title)).setText(R.string.title_edit_address);
        }
        initializeDateField();
        setupEditFields();
        initializeToolbarButton();
    }


    public void initializeToolbarButton() {
        addToolbarButton = (Button) findViewById(R.id.add_button_toolbar);
        cancelToolbarButton = (Button) findViewById(R.id.cancel_button_toolbar);

        if (mode.equals("edit")) {
            addToolbarButton.setText(R.string.save_action);
        }

        addToolbarButton.setOnClickListener(this);
        cancelToolbarButton.setOnClickListener(this);
    }

    public void initializeDateField() {

        dateInField = (TextView) findViewById(R.id.date_in_field);
        dateOutField = (TextView) findViewById(R.id.date_out_field);

        dateInField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCallInDate = true;
                showDatePickerDialog();
            }
        });

        dateOutField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCallInDate = false;
                showDatePickerDialog();
            }
        });
    }

    /**
     * method initialize EditText fields with hint
     */
    public void setupEditFields() {
        TextInputLayout til;
        til = (TextInputLayout) findViewById(R.id.street_edit_hint);
        streetField = til.getEditText();

        til = (TextInputLayout) findViewById(R.id.city_edit_hint);
        cityField = til.getEditText();

        til = (TextInputLayout) findViewById(R.id.state_edit_hint);
        stateField = til.getEditText();

        til = (TextInputLayout) findViewById(R.id.monthly_rent_edit_hint);
        monthlyRentField = til.getEditText();

        til = (TextInputLayout) findViewById(R.id.landlord_edit_hint);
        landlordField = til.getEditText();

        if (mode.equals("edit")) {
            fillEditFields();
        }

    }

    /**
     * method that fill EditFields when called Edit mode
     */
    public void fillEditFields() {
        List<AddressesItem> addressesItemList = Utils.getListAddresses();
        streetField.setText(addressesItemList.get(numEditElement).getStreet());
        cityField.setText(addressesItemList.get(numEditElement).getCity());
        stateField.setText(addressesItemList.get(numEditElement).getState());
        monthlyRentField.setText(addressesItemList.get(numEditElement).getRent());
        landlordField.setText(addressesItemList.get(numEditElement).getLandLord());
        dateInField.setText(addressesItemList.get(numEditElement).getDateIn());
        dateOutField.setText(addressesItemList.get(numEditElement).getDateOut());
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ((Button) toolbar.findViewById(R.id.add_button_toolbar))
                .setText(getResources().getString(R.string.add_action));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * method called when need to show the date pick dialog
     */
    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * method called when it was selected date
     */
    public void changeDateField(String date) {
        if (isCallInDate) {
            dateInField.setText(date);
        } else {
            dateOutField.setText(date);
        }
    }

    /**
     * method called when user click button "Add/Save" and needed check fields correctly filled
     */
    public void CheckCorrectInputField() {

        String street = streetField.getText().toString();
        String city = cityField.getText().toString();
        String state = stateField.getText().toString();
        String monthlyRent = monthlyRentField.getText().toString();
        String landLord = landlordField.getText().toString();
        String dateIn = dateInField.getText().toString();
        String dateOut = dateOutField.getText().toString();

        if (Utils.CheckCorrectStrings(street) && Utils.CheckCorrectStrings(city)
                && Utils.CheckCorrectStrings(state) && Utils.CheckCorrectStrings(landLord)
                && Utils.CheckCorrectDate(dateIn, dateOut, this)
                && Utils.CheckCorrectRent(monthlyRent)) {
            if (mode.equals("add")) {
                AcceptedNewAddress(street, city, state, monthlyRent, landLord, dateIn, dateOut);
            } else {
                AcceptedEditAddress(street, city, state, monthlyRent, landLord, dateIn, dateOut);
            }
        } else {
            int exceptionMessageId = 0;
            if (!Utils.CheckCorrectDate(dateIn, dateOut, this)) {
                exceptionMessageId = R.string.invalid_date_field;
            }
            if (!Utils.CheckCorrectStrings(landLord)) {
                exceptionMessageId = R.string.invalid_landlord;
            }
            if (!Utils.CheckCorrectRent(monthlyRent)) {
                exceptionMessageId = R.string.invalid_monthly_rent;
            }
            if (!Utils.CheckCorrectStrings(state)) {
                exceptionMessageId = R.string.invalid_state_name;
            }
            if (!Utils.CheckCorrectStrings(city)) {
                exceptionMessageId = R.string.invalid_city_name;
            }
            if (!Utils.CheckCorrectStrings(street)) {
                exceptionMessageId = R.string.invalid_street_name;
            }

            Toast toast = Toast.makeText(this,
                    exceptionMessageId, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * method called when all fields filled correctly and need to edit previously added address
     */

    private void AcceptedEditAddress(String street, String city, String state, String monthlyRent,
                                     String landLord, String dateIn, String dateOut) {

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        Dao<AddressesItem, Integer> addressesDao = databaseHelper.getAddressItemsDao();

        List<AddressesItem> listAddresses = Utils.getListAddresses();
        AddressesItem addressesItem = listAddresses.get(numEditElement);
        addressesItem.setStreet(street);
        addressesItem.setCity(city);
        addressesItem.setState(state);
        addressesItem.setRent(monthlyRent);
        addressesItem.setLandLord(landLord);
        addressesItem.setDateIn(dateIn);
        addressesItem.setDateOut(dateOut);
        listAddresses.set(numEditElement, addressesItem);
        Utils.setListAddresses(listAddresses);

        try {
            addressesDao.update(addressesItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        postData(street, landLord);

    }

    /**
     * method called when all fields filled correctly and need to add new address
     */

    private void AcceptedNewAddress(String street, String city, String state, String monthlyRent,
                                    String landLord, String dateIn, String dateOut) {

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        Dao<AddressesItem, Integer> addressesDao = databaseHelper.getAddressItemsDao();

        List<AddressesItem> listAddresses = Utils.getListAddresses();

        AddressesItem addressesItem = new AddressesItem(street, city, state, monthlyRent, landLord,
                dateIn, dateOut);

        listAddresses.add(addressesItem);
        Utils.setListAddresses(listAddresses);
        try {
            addressesDao.create(addressesItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        postData(street, landLord);

    }

    /**
     * method called when needed send post request
     */
    public void postData(final String address, final String landLord) {

        /**
         * creating new thread for sending post request
         */
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpClient httpclient = HttpClientBuilder.create().build();
                HttpPost httppost = new HttpPost("http://posttestserver.com/post.php");

                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("address", address));
                    nameValuePairs.add(new BasicNameValuePair("landLord", landLord));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity responseEntity = response.getEntity();
                    if(responseEntity!=null) {
                        Utils.setResponseString(EntityUtils.toString(responseEntity));
                    }

                } catch (IOException e) {
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Intent intent = new Intent(AddAddresses.this, MainActivity.class);
                intent.putExtra("response", Utils.getResponseString());
                startActivity(intent);
            }
        };
        asyncTask.execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button_toolbar:
                CheckCorrectInputField();
                break;
            case R.id.cancel_button_toolbar:
                Intent intent = new Intent(AddAddresses.this, MainActivity.class);
                intent.putExtra("response", Utils.getResponseString());
                startActivity(intent);
                break;
        }
    }
}
