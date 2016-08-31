package ges.kumov.my.listorder;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Tools {
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_mein_menu);
        mDatabaseHelper = new DataBaseHelper(this);
    }

    public void onRefresh(View view) {
        str = "Обновление данных";
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    public void onConnect(View view) {
        str = "Сервер не найден";
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

}
