package com.pischik.nikita.rentalmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView AddressesRecyclerView;
    private Button addButton;
    private DatabaseHelper databaseHelper;
    private Dao<AddressesItem, Integer> addressesDao;
    private List<AddressesItem> addresses;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        response = getIntent().getStringExtra("response");
        setupToolbar();
        setupRecyclerView();
        addButton = (Button) findViewById(R.id.add_button_toolbar);
        addButton.setOnClickListener(this);

        databaseHelper = OpenHelperManager.getHelper(this,
                DatabaseHelper.class);

        addressesDao = databaseHelper.getAddressItemsDao();

        try {
            addresses = addressesDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Utils.setListAddresses(addresses);

        List<AddressesItem> listAddresses = Utils.getListAddresses();

        RVAddressesListAdapter adapter = new RVAddressesListAdapter(listAddresses);
        adapter.SetOnItemClickListener(new RVAddressesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, AddAddresses.class);
                intent.putExtra("mode", "edit");
                intent.putExtra("num", position);
                startActivity(intent);
            }
        });
        AddressesRecyclerView.setAdapter(adapter);


    }

    /**
     * method that setup and customize toolbar
     */
    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.cancel_button_toolbar).setVisibility(View.INVISIBLE);
        ((Button) toolbar.findViewById(R.id.add_button_toolbar))
                .setText(getResources().getString(R.string.add_action));
        toolbar.setLongClickable(true);
        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (response != null) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_LONG);
                    toast.show();
                }
                return false;
            }
        });
        setSupportActionBar(toolbar);

    }

    /**
     * method that setup RecyclerView
     */
    public void setupRecyclerView() {
        AddressesRecyclerView = (RecyclerView) findViewById(R.id.list_addresses);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AddressesRecyclerView.setLayoutManager(llm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button_toolbar:
                Intent intent = new Intent(MainActivity.this, AddAddresses.class);
                intent.putExtra("mode", "add");
                startActivity(intent);
                break;
        }
    }

}
