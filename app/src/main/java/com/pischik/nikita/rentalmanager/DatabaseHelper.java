package com.pischik.nikita.rentalmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * class that get access to database, create database or update it
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "rental.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<AddressesItem, Integer> AddressItemsDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, AddressesItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, AddressesItem.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * method that return Database Access Object
     */

    public Dao<AddressesItem, Integer> getAddressItemsDao() {
        if (AddressItemsDao == null) {
            try {
                AddressItemsDao = getDao(AddressesItem.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return AddressItemsDao;
    }
}
