package ges.kumov.my.listorder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a.kumov on 8/20/15.
 */
public class ListOrder extends Tools {
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    protected void onCreate(Bundle savedInstanceState) {
        mDatabaseHelper = new DataBaseHelper(this);
        /*
         *   делаем базу открытой для редактирования/чтения
        **/
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_list_order);
        OrderListTask olt = new OrderListTask();
        olt.execute();
    }



    private class OrderListTask extends AsyncTask<Void, Void, Void> {

        public static final String ID = "_id";
        public static final String NUMBER_ORDER = "numberOrer";
        public static final String CLIENT_NAME = "clientName";
        public static final String MY_ORG = "myorg";
        public static final String DATE_ORDER = "dateorder";
        public static final String OTGRUZKA = "dateotgruzki";
        public static final String DOSTAVKA = "datedostavki";
        public static final String SUMM = "summ";
        public static final String STATUS = "status";
        ArrayList<HashMap<String, String>> orders = new ArrayList<HashMap<String, String>>();
        String idZakaza;
        HashMap<String, String> hashmap;
        SimpleAdapter adapter_order;
        ListView lvOrder = (ListView) findViewById(R.id.list_view_order);
        AdapterView.OnItemClickListener click = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> itemHashMap = (HashMap<String, String>) parent.getItemAtPosition(position);
                idZakaza = itemHashMap.get(ID).toString();
            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_ORDER_TABLE, new String[]{
                            DataBaseHelper._ID, DataBaseHelper.ORDER_NUMBER_COLUMN, DataBaseHelper.ORDER_CLIENTORG_COLUMN,
                            DataBaseHelper.ORDER_MYORG_COLUMN, DataBaseHelper.ORDER_DATE_COLUMN,
                            DataBaseHelper.ORDER_DATEOTGRUZKI_COLUMN,  DataBaseHelper.ORDER_DATEDOSTAVKI_COLUMN,
                            DataBaseHelper.ORDER_SUMM_COLUMN, DataBaseHelper.ORDER_STATUS_COLUMN},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper._ID));
                String numberOrer = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_NUMBER_COLUMN));
                String clientName = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_CLIENTORG_COLUMN));
                String myorg = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_MYORG_COLUMN));
                String dateorder = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_DATE_COLUMN));
                String dateotgruzki = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_DATEOTGRUZKI_COLUMN));
                String datedostavki = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_DATEDOSTAVKI_COLUMN));
                String summ = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_SUMM_COLUMN));
                String status = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDER_STATUS_COLUMN));
                hashmap = new HashMap<String, String>();
                hashmap.put(ID, id+"");
                hashmap.put(NUMBER_ORDER, numberOrer);
                hashmap.put(CLIENT_NAME, clientName);
                hashmap.put(MY_ORG, myorg);
                hashmap.put(DATE_ORDER, dateorder);
                hashmap.put(OTGRUZKA, dateotgruzki);
                hashmap.put(DOSTAVKA, datedostavki);
                hashmap.put(SUMM, summ);
                if (Integer.parseInt(status)==0)
                    status="не отправлен";
                hashmap.put(STATUS, status);
                orders.add(hashmap);
            }
            cursor.close();
            adapter_order = new SimpleAdapter(ListOrder.this, orders,
                    R.layout.list_orders, new String[]{NUMBER_ORDER,DATE_ORDER, CLIENT_NAME,
                    MY_ORG,SUMM,STATUS}, new int[]{R.id.order_number_tv,
                    R.id.date_order_tv,R.id.client_org_tv,R.id.my_org_order_tv,
                    R.id.summ_order_tv,R.id.status_order_tv});
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            lvOrder.setAdapter(adapter_order);
            lvOrder.setOnItemClickListener(click);
        }
    }
}
