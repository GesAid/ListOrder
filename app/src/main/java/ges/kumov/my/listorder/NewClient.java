package ges.kumov.my.listorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by a.kumov on 8/19/15.
 */
public class NewClient extends Tools {
    EditText nameOrg;
    EditText urAdres;
    EditText faktAdres;
    EditText telephon;
    EditText faks;
    EditText inn;
    EditText kpp;
    EditText rSchet;
    EditText bank;
    EditText kSchet;
    EditText bik;
    EditText okved;
    EditText okpo;
    EditText ogrn;
    EditText okato;
    EditText doljnost;
    EditText fio;
    EditText email;
    ContentValues values;
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_new_client);
        shared = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = shared.edit();
        nameOrg = (EditText) findViewById(R.id.e_t_input_name);
        urAdres = (EditText) findViewById(R.id.e_t_input_ur_adres);
        faktAdres = (EditText) findViewById(R.id.e_t_input_fakt_adres);
        telephon = (EditText) findViewById(R.id.e_t_input_tel);
        faks = (EditText) findViewById(R.id.e_t_input_faks);
        inn = (EditText) findViewById(R.id.e_t_input_inn);
        kpp = (EditText) findViewById(R.id.e_t_input_kpp);
        rSchet = (EditText) findViewById(R.id.e_t_input_rs);
        bank = (EditText) findViewById(R.id.e_t_input_bank);
        kSchet = (EditText) findViewById(R.id.e_t_input_ks);
        bik = (EditText) findViewById(R.id.e_t_input_bik);
        okved = (EditText) findViewById(R.id.e_t_input_okved);
        okpo = (EditText) findViewById(R.id.e_t_input_okpo);
        ogrn = (EditText) findViewById(R.id.e_t_input_ogrn);
        okato = (EditText) findViewById(R.id.e_t_input_okato);
        doljnost = (EditText) findViewById(R.id.e_t_input_doljnost);
        fio = (EditText) findViewById(R.id.e_t_input_fio);
        email = (EditText) findViewById(R.id.e_t_input_email);

        mDatabaseHelper = new DataBaseHelper(this);

        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        values = new ContentValues();
    }

    public void saveClient(View view) {
        values.put("nameOrg", nameOrg.getText().toString());
        values.put("urAdres",urAdres.getText().toString());
        values.put("faktAdres",faktAdres.getText().toString());
        values.put("telephon",telephon.getText().toString());
        values.put("faks",faks.getText().toString());
        values.put("inn",inn.getText().toString());
        values.put("kpp",kpp.getText().toString());
        values.put("rSchet",rSchet.getText().toString());
        values.put("bank",bank.getText().toString());
        values.put("kSchet",kSchet.getText().toString());
        values.put("bik",bik.getText().toString());
        values.put("okved",okved.getText().toString());
        values.put("okpo",okpo.getText().toString());
        values.put("ogrn",ogrn.getText().toString());
        values.put("okato",okato.getText().toString());
        values.put("doljnost",doljnost.getText().toString());
        values.put("fio",fio.getText().toString());
        values.put("email",email.getText().toString());
        mSqLiteDatabase.insert(DataBaseHelper.DATABASE_CLIENT_TABLE,null, values);

        nameOrg.setText("");
        urAdres.setText("");
        faktAdres.setText("");
        telephon.setText("");
        faks.setText("");
        inn.setText("");
        kpp.setText("");
        rSchet.setText("");
        bank.setText("");
        kSchet.setText("");
        bik.setText("");
        okved.setText("");
        okpo.setText("");
        ogrn.setText("");
        okato.setText("");
        doljnost.setText("");
        fio.setText("");
        email.setText("");

    }
}
