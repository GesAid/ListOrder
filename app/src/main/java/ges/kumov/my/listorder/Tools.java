package ges.kumov.my.listorder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by a.kumov on 8/19/15.
 */
public class Tools extends Activity {
    protected static final String APP_PREFERENCES = "mysettings";
    Intent go;
    String str = "";
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    //   public void onExit() {
    //     go = new Intent(this, MainActivity.class);
    //   startActivity(go);
    //  }
    public void onBack() {
        finish();
    }

    public void onInfo() {
        go = new Intent(this, Info.class);
        startActivity(go);
    }

    public void onNewOrder(View view) {
        go = new Intent(this, NewOrder.class);
        startActivity(go);
    }

    public void onOrderList(View view) {
        go = new Intent(this, ListOrder.class);
        startActivity(go);
    }

    public void onLicence(View view) {
        str = "Все путем, юзай пока бесплатно";
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        go = new Intent(this, Licence.class);
        startActivity(go);
    }

    public void onClientList(View view) {
        go = new Intent(this, ClientList.class);
        startActivity(go);
    }

    public void onNewClient(View view) {
        go = new Intent(this, NewClient.class);
        startActivity(go);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.for_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_info:
                onInfo();
                return true;
            case R.id.action_back:
                onBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
