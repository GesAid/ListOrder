package ges.kumov.my.listorder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by a.kumov on 8/19/15.
 */
public class ClientList extends Tools {
    TextView mEditText;
    ContentValues values;
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_client_list);
        mDatabaseHelper = new DataBaseHelper(this);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        ClientOrgTask clientOrgTask = new ClientOrgTask();
        clientOrgTask.execute();
    }

    public void showInfoOrg(String str) {
        LinearLayout ll = new LinearLayout(this);
        ll.setBackgroundResource(R.drawable.theme_black);

        ll.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(50, ViewGroup.LayoutParams.WRAP_CONTENT, 15);
        llp.setMargins(2, 2, 2, 2);
        LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(50, ViewGroup.LayoutParams.WRAP_CONTENT, 5);
        llp1.setMargins(2, 2, 2, 2);
        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5);
        llp2.setMargins(2, 2, 2, 2);
        ll.setLayoutParams(llp2);
        TextView info = new TextView(this);
        info.setText("НАЗВАНИЕ  :\nЮРИДИЧЕСКИЙ АДРЕС  :\nФАКТИЧЕСКИЙ АДРЕС  :\nЕЛЕФОН / ФАКС  :\n" +
                "ИИН / КПП  :\nРАСЧЕТНЫЙ СЧЕТ  :\nБАНК  :\nКОР. СЧЕТ  :\nБИК  :\nОКВЭД  :\nОКПО  :\n" +
                "ОГРН  :\nОКАТО  :\nРУКОВОДИТЕЛЬ  :\nE-MAIL  :");
        info.setLayoutParams(llp1);

        info.setTextColor(getResources().getColor(R.color.red_text));
        ll.addView(info);

        TextView data_info = new TextView(this);
        data_info.setText(String.valueOf(str));
        data_info.setLayoutParams(llp);
        data_info.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientList.this, NewOrder.class);
                startActivity(intent);
                finish();
            }
        });
        data_info.setTextColor(getResources().getColor(R.color.red_text));
        ll.addView(data_info);
        LinearLayout shg = (LinearLayout) findViewById(R.id.v_l_for_client);
        shg.addView(ll);
    }

    private class ClientOrgTask extends AsyncTask<Void, Void, Void> {
        final ArrayList<String> str = new ArrayList<String>();
        Spinner spin_myOrg = (Spinner) findViewById(R.id.tv_my_organization);
        ArrayAdapter<String> clientAdapter;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_CLIENT_TABLE, new String[]{
                            DataBaseHelper.CLIENT_NAMEORG_COLUMN, DataBaseHelper.CLIENT_URADRES_COLUMN,
                            DataBaseHelper.CLIENT_FACTADRES_COLUMN, DataBaseHelper.CLIENT_TELEFON_COLUMN,
                            DataBaseHelper.CLIENT_FAKS_COLUMN, DataBaseHelper.CLIENT_INN_COLUMN, DataBaseHelper.CLIENT_KPP_COLUMN,
                            DataBaseHelper.CLIENT_RSCHET_COLUMN, DataBaseHelper.CLIENT_BANK_COLUMN,
                            DataBaseHelper.CLIENT_KSCHET_COLUMN, DataBaseHelper.CLIENT_BIK_COLUMN, DataBaseHelper.CLIENT_OKVED_COLUMN,
                            DataBaseHelper.CLIENT_OKPO_COLUMN, DataBaseHelper.CLIENT_OGRN_COLUMN, DataBaseHelper.CLIENT_OKATO_COLUMN,
                            DataBaseHelper.CLIENT_DOLJNOST_COLUMN, DataBaseHelper.CLIENT_FIO_COLUMN, DataBaseHelper.CLIENT_EMAIL_COLUMN},
                    null, null, null, null, null);

            while (cursor.moveToNext()) {
                String nameOrg = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_NAMEORG_COLUMN));
                String urAdres = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_URADRES_COLUMN));
                String faktAdres = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_FACTADRES_COLUMN));
                String telephon = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_TELEFON_COLUMN));
                String faks = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_FAKS_COLUMN));
                int inn = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_INN_COLUMN));
                int kpp = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_KPP_COLUMN));
                int rSchet = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_RSCHET_COLUMN));
                String bank = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_BANK_COLUMN));
                int kSchet = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_KSCHET_COLUMN));
                int bik = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_BIK_COLUMN));
                int okved = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_OKVED_COLUMN));
                int okpo = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_OKPO_COLUMN));
                int ogrn = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_OGRN_COLUMN));
                int okato = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CLIENT_OKATO_COLUMN));
                String doljnost = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_DOLJNOST_COLUMN));
                String fio = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_FIO_COLUMN));
                String email = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_EMAIL_COLUMN));

                String client = nameOrg + "\n" + urAdres + "\n" + faktAdres + "\n" +
                        telephon + " / " + faks + "\n" +
                        inn + "/" + kpp + "\n" +
                        rSchet + "\n" + bank + "\n" + kSchet + "\n" +
                        bik + "\n" + okved + "\n" + okpo + "\n" +
                        ogrn + "\n" + okato + "\n" +
                        doljnost + "    " + fio + "\n" + email;
                showInfoOrg(client);
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}


