package ges.kumov.my.listorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String CLIENT_NAMEORG_COLUMN = "nameOrg";
    public static final String CLIENT_URADRES_COLUMN = "urAdres";
    public static final String CLIENT_FACTADRES_COLUMN = "faktAdres";
    public static final String CLIENT_TELEFON_COLUMN = "telephon";
    public static final String CLIENT_FAKS_COLUMN = "faks";
    public static final String CLIENT_INN_COLUMN = "inn";
    public static final String CLIENT_KPP_COLUMN = "kpp";
    public static final String CLIENT_RSCHET_COLUMN = "rSchet";
    public static final String CLIENT_BANK_COLUMN = "bank";
    public static final String CLIENT_KSCHET_COLUMN = "kSchet";
    public static final String CLIENT_BIK_COLUMN = "bik";
    public static final String CLIENT_OKVED_COLUMN = "okved";
    public static final String CLIENT_OKPO_COLUMN = "okpo";
    public static final String CLIENT_OGRN_COLUMN = "ogrn";
    public static final String CLIENT_OKATO_COLUMN = "okato";
    public static final String CLIENT_DOLJNOST_COLUMN = "doljnost";
    public static final String CLIENT_FIO_COLUMN = "fio";
    public static final String CLIENT_EMAIL_COLUMN = "email";
    public static final String CLIENT_SKIDKA_COLUMN = "skidka";
    public static final String CLIENT_DOLG_COLUMN = "dolg";
    public static final String CLIENT_OTSROCHKA_COLUMN = "otsrochkadolga";

    public static final String ORDER_NUMBER_COLUMN = "numberOrer";
    public static final String ORDER_CLIENTORG_COLUMN = "clientName";
    public static final String ORDER_DATE_COLUMN = "dateorder";
    public static final String ORDER_MYORG_COLUMN = "myorg";
    public static final String ORDER_DATEOTGRUZKI_COLUMN = "dateotgruzki";
    public static final String ORDER_DATEDOSTAVKI_COLUMN = "datedostavki";
    public static final String ORDER_SUMM_COLUMN = "summ";
    public static final String ORDER_STATUS_COLUMN = "status";

    public static final String TOVAR_LABEL_COLUMN = "label";
    public static final String TOVAR_LOCALLABEL_COLUMN = "locallabel";
    public static final String TOVAR_KOLVO_COLUMN = "kolvo";
    public static final String TOVAR_ED_IZM_COLUMN = "edizm";
    public static final String TOVAR_CENNA_COLUMN = "cenna";
    public static final String TOVAR_VALUTA_COLUMN = "valuta";
    public static final String MYORG_NAME_COLUMN = "name";

    public static final String ORDERLIST_IDTOVAR_COLUMN = "idtovar";
    public static final String ORDERLIST_LABEL_COLUMN = "label";
    public static final String ORDERLIST_LOCALLABEL_COLUMN = "locallabel";
    public static final String ORDERLIST_KOLVO_COLUMN = "kolvo";
    public static final String ORDERLIST_CENNA_COLUMN = "cenna";
    public static final String ORDERLIST_NUMZAKAZ_COLUMN = "numzakaza";

    protected static final String DATABASE_CLIENT_TABLE = "client";

    protected static final String DATABASE_ORDER_TABLE = "myOrder";

    protected static final String DATABASE_TOVAR_TABLE = "tovar";

    protected static final String DATABASE_MYORG_TABLE = "myorg";

    protected static final String DATABASE_ORDERLIST_TABLE = "orderlist";

    private static final String FOREIGN_KEYS = "pragma foreign_keys=on";
    private static final String DATABASE_NAME = "orderlistdatabase.db";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CLIENT_CREATE_SCRIPT = "create table "
            + DATABASE_CLIENT_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + CLIENT_NAMEORG_COLUMN + " text not null, "
            + CLIENT_URADRES_COLUMN
            + " text not null, " + CLIENT_FACTADRES_COLUMN + " text , " + CLIENT_TELEFON_COLUMN
            + " text, " + CLIENT_FAKS_COLUMN + " text, " + CLIENT_INN_COLUMN
            + " integer , " + CLIENT_KPP_COLUMN + " integer , "
            + CLIENT_RSCHET_COLUMN
            + " integer , " + CLIENT_BANK_COLUMN + " text, " + CLIENT_KSCHET_COLUMN
            + " integer , " + CLIENT_BIK_COLUMN + " integer , "
            + CLIENT_OKVED_COLUMN
            + " integer , " + CLIENT_OKPO_COLUMN + " integer , "
            + CLIENT_OGRN_COLUMN
            + " integer , " + CLIENT_OKATO_COLUMN + " integer , "
            + CLIENT_DOLJNOST_COLUMN
            + " text, " + CLIENT_FIO_COLUMN + " text, " + CLIENT_EMAIL_COLUMN
            + " text , " + CLIENT_SKIDKA_COLUMN + " real default 1, "
            + CLIENT_DOLG_COLUMN
            + " real , " + CLIENT_OTSROCHKA_COLUMN + " text);";

    private static final String DATABASE_ORDER_CREATE_SCRIPT = "create table " + DATABASE_ORDER_TABLE
            + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + ORDER_NUMBER_COLUMN + " integer, " + ORDER_CLIENTORG_COLUMN + " text , "
            + ORDER_MYORG_COLUMN + " text , "
            + ORDER_DATE_COLUMN + " text , " + ORDER_DATEOTGRUZKI_COLUMN + " text , "
            + ORDER_DATEDOSTAVKI_COLUMN +
            " text, " + ORDER_SUMM_COLUMN + " real, " + ORDER_STATUS_COLUMN
            + " integer default 0" +");";

    private static final String DATABASE_TOVAR_CREATE_SCRIPT = "create table "
            + DATABASE_TOVAR_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + TOVAR_LABEL_COLUMN + " text, " + TOVAR_LOCALLABEL_COLUMN + " text, "
            + TOVAR_KOLVO_COLUMN + " integer, " + TOVAR_ED_IZM_COLUMN + " text, " +
            TOVAR_CENNA_COLUMN + " real, " + TOVAR_VALUTA_COLUMN + " text);";

    private static final String DATABASE_MYORG_CREATE_SCRIPT = "create table "
            + DATABASE_MYORG_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + MYORG_NAME_COLUMN + " text);";

    private static final String DATABASE_ORDERLIST_CREATE_SCRIPT = "create table "
            + DATABASE_ORDERLIST_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + ORDERLIST_IDTOVAR_COLUMN + " integer, " + ORDERLIST_LABEL_COLUMN + " text, " + ORDERLIST_LOCALLABEL_COLUMN + " text, "
            + ORDERLIST_KOLVO_COLUMN + " integer, " + ORDERLIST_CENNA_COLUMN + " real, " + ORDERLIST_NUMZAKAZ_COLUMN +
            " integer);";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FOREIGN_KEYS);
        db.execSQL(DATABASE_CLIENT_CREATE_SCRIPT);
        db.execSQL(DATABASE_MYORG_CREATE_SCRIPT);
        db.execSQL(DATABASE_TOVAR_CREATE_SCRIPT);
        db.execSQL(DATABASE_ORDER_CREATE_SCRIPT);
        db.execSQL(DATABASE_ORDERLIST_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_CLIENT_TABLE);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_MYORG_TABLE);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TOVAR_TABLE);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_ORDER_TABLE);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_ORDERLIST_TABLE);
        onCreate(db);
    }
}
