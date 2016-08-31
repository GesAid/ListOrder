package ges.kumov.my.listorder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NewOrder extends Tools {
/**
 * ============================================================================================
 * Свойства класса
 * ============================================================================================
 * Счетчик всех заказов
 * @param orderCounter - счетчик заказов. Увеличивается после сохранения заказа
 * @param idChooseTovar - id выбранного товара, во вкладке наменклуатура, для внесения в заказ.
 *                      Необходим для пересчета остатка товара.
 * @param idInPosorder - id выбранного товара, в БД ORDERLIST, для удаления строки из заказа.
 * @param idInTovarTable - id позиции, в которую возвращается товар, после удаления позиции.
 * @param kolvoReturnOnTovar - количество возвращаемое в остатки.
 * @param mDatabaseHelper - объект класса DatabaseHelper, для работы с базами данных.
 * @param mSqLiteDatabase - объект класса SqLiteDatabase, для работы с базами данных.
 * @param dateDostavki - TextView даты доставки товара. Вызывает FragmentDate.
 * @param dateOtgruzki - TextView даты отгрузки товара. Вызывает FragmentDate.
 * @param nazvanietovara - TextView отображающие выбранный товар для внесения в заказ.
 * @param sumOrderTV - TextView отобрыжающая сумму заказа
 * @param input_kol - EditText ввод количества товара в заказе
 * @param controlKolvo - переменная контроля заказанных едениц товаров.
 * @param kolTovar - переменная для хранения данных, о выбранном количестве товара
 * @param dateOrder - TextView дата создания товара. не изменяется.
 * @param orderCount - TextView отображение номера заказа.
 * @param myOrgName - Организация отпускающая товар.
 * @param clientOrgName - Заказчик.
 * @param skidkaClienta - Необходим для рассчета цены, с учетом скидки
*/
    CustomAdapter myOrgAdapter;
    CustomAdapter clientAdapter;
    static int orderCounter = 1;
    int idChooseTovar;
    int idInPosorder;
    int idInTovarTable;
    int kolvoReturnOnTovar;
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    TextView dateDostavki;
    TextView dateOtgruzki;
    TextView nazvanietovara;
    TextView dolgTV;
    TextView otsrochkaTV;
    TextView sumOrderTV;
    EditText input_kol;
    String controlKolvo;
    String kolTovar;
    TextView dateOrder;
    TextView orderCount;
    String myOrgName;
    String clientOrgName;
    float skidkaClienta;
/**============================================================================================
 * Поведение класса
 * ============================================================================================
 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_new_order);
        mDatabaseHelper = new DataBaseHelper(this);
        /*
         *   делаем базу открытой для редактирования/чтения
        **/
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
       /*
        * Инициализация переменных. Установка OnClickListener
        * на даты отгрузки и доставки
       **/
        nazvanietovara = (TextView) findViewById(R.id.choose_tovar);
        input_kol = (EditText) findViewById(R.id.et_input_kol_tovara);
        sumOrderTV = (TextView) findViewById(R.id.et_summa_order);
        dateOrder = (TextView) findViewById(R.id.dateOrder);
        orderCount = (TextView) findViewById(R.id.orderCount);
        dateOtgruzki = (TextView) findViewById(R.id.et_date_otgruzki);
        dolgTV = (TextView) findViewById(R.id.et_dolg);
        otsrochkaTV = (TextView) findViewById(R.id.et_otsrochka);
        dateOtgruzki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new OtgrFragmentDate();

                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });
        dateDostavki = (TextView) findViewById(R.id.et_date_dostavki);
        dateDostavki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new DostavkaFragmentDate();

                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });
         /*
        * Инициализация вкладок. Запуск метода их формирования
        */
        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        soTabs(tabs);
        //Форматирование даты заказа
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        //Устанавка даты заказа
        dateOrder.setText(format.format(new Date()));
        dateOtgruzki.setText(format.format(new Date()));
        dateDostavki.setText(format.format(new Date()));
        //Заполнение данных о собственных организациях. В отдельном потоке
        MyOrgTask myOrgTask = new MyOrgTask();
        myOrgTask.execute();
        //Необходимо для изъятия инфы о клиентах, пока не могу писать в файл
        shared = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = shared.edit();
        orderCounter = shared.getInt("orderCounter", 1);

        //Установка номера заказа
        orderCount.setText(String.format("%06d", orderCounter));

        //Выпадающий список клиентов, инициализация
        ClientOrgTask clientOrgTask = new ClientOrgTask();
        clientOrgTask.execute();

        clientAdapter.flag = false;
        myOrgAdapter.flag = false;
    }

    //Программное создание вкладок в качестве самих вкладок юзаю текствьюшки
    //В код не вдовался, так как особых проблем с ним не возникло
    private void soTabs(TabHost tabs) {

        TextView tv_data_order = new TextView(this);
        tv_data_order.setText("Данные заказа");
        tv_data_order.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        tv_data_order.setTextSize(25);
        tv_data_order.setTextColor(getResources().getColor(R.color.red_text));
        tv_data_order.setBackgroundResource(R.drawable.tab_black);

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);

        spec.setIndicator(tv_data_order);
        tabs.addTab(spec);

        TextView tv_namenklatura = new TextView(this);
        tv_namenklatura.setText("Наменклуатура");
        tv_namenklatura.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        tv_namenklatura.setTextSize(25);
        tv_namenklatura.setTextColor(getResources().getColor(R.color.red_text));
        tv_namenklatura.setBackgroundResource(R.drawable.tab_black);
        spec = tabs.newTabSpec("tag2");

        spec.setContent(R.id.tab2);
        spec.setIndicator(tv_namenklatura);
        tabs.addTab(spec);

        TextView tv_prochee = new TextView(this);
        tv_prochee.setText("Комментарии к заказу");
        tv_prochee.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        tv_prochee.setTextSize(25);
        tv_prochee.setTextColor(getResources().getColor(R.color.red_text));
        tv_prochee.setBackgroundResource(R.drawable.tab_black);

        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator(tv_prochee);
        tabs.addTab(spec);

        tabs.setCurrentTab(0);


    }

    //Добавление позиции в заказ с изменением содержимого БД.
    public void addPositionOrder(View view) {
        kolTovar = input_kol.getText().toString();
        AddPositionTask add = new AddPositionTask();
        add.execute();
        TovarListTask tovarListTask = new TovarListTask();
        tovarListTask.execute();
    }

    public void saveOrder(View view) {
        SaveOrderTask saveTask = new SaveOrderTask();
        saveTask.execute();
    }

    //Обработка запроса выбранного товара
    private class ChooseTask extends AsyncTask<Void, Void, Void> {
        String one = "";
        int two = 0;

        ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
        @Override
        protected void onPreExecute() {
            pb.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String usl = "_id=" + idChooseTovar;
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_TOVAR_TABLE,
                    new String[]{DataBaseHelper.TOVAR_LOCALLABEL_COLUMN,
                            DataBaseHelper.TOVAR_KOLVO_COLUMN}, usl, null, null, null, null);
            while (cursor.moveToNext()) {
                one = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_LOCALLABEL_COLUMN));
                two = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.TOVAR_KOLVO_COLUMN));
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            nazvanietovara.setText(one);
            input_kol.setText(two + "");
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    //Заполнение своих организаций
    private class MyOrgTask extends AsyncTask<Void, Void, Void> {
        final ArrayList<String> str = new ArrayList<String>();
        Spinner spin_myOrg = (Spinner) findViewById(R.id.tv_my_organization);

        ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
        @Override
        protected void onPreExecute() {
            pb.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor1 = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_MYORG_TABLE, new String[]{
                            DataBaseHelper.MYORG_NAME_COLUMN},
                    null, null, null, null, null);
            while (cursor1.moveToNext()) {
                String myorg = cursor1.getString(cursor1.getColumnIndex(DataBaseHelper.MYORG_NAME_COLUMN));
                str.add(myorg);
            }
            cursor1.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            myOrgAdapter =
                    new CustomAdapter(NewOrder.this, R.layout.spinner_on, str);
            myOrgAdapter.setDropDownViewResource(R.layout.spinner_drop);
            spin_myOrg.setAdapter(myOrgAdapter);
            spin_myOrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {
                    myOrgAdapter.flag = true;
                    String[] choose = str.toArray(new String[str.size()]);
                    myOrgName = "" + choose[selectedItemPosition];

                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    //Заполнение клиентской базы
    private class ClientOrgTask extends AsyncTask<Void, Void, Void> {
        ArrayList<String> str = new ArrayList<String>();
        Spinner spin_client = (Spinner) findViewById(R.id.t_v_client_info);

        ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
        @Override
        protected void onPreExecute() {
 pb.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_CLIENT_TABLE, new String[]{
                            DataBaseHelper._ID,
                            DataBaseHelper.CLIENT_NAMEORG_COLUMN},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                String nameOrg = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_NAMEORG_COLUMN));
                str.add(nameOrg);
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            clientAdapter =
                    new CustomAdapter(NewOrder.this, R.layout.spinner_on, str);
            clientAdapter.setDropDownViewResource(R.layout.spinner_drop);

            //Листнер для выбора организаций
            spin_client.setAdapter(clientAdapter);
            spin_client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {
                    if (!clientAdapter.flag){
                    clientAdapter.flag = true;
                    }
                    else {
                    String[] choose = str.toArray(new String[str.size()]);
                    clientOrgName = choose[selectedItemPosition];
                    Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_CLIENT_TABLE,
                            new String[]{DataBaseHelper.CLIENT_NAMEORG_COLUMN,DataBaseHelper.CLIENT_SKIDKA_COLUMN,
                                    DataBaseHelper.CLIENT_DOLG_COLUMN, DataBaseHelper.CLIENT_OTSROCHKA_COLUMN},
                            null, null, null, null, null);
                    while (cursor.moveToNext()){
                        dolgTV.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_DOLG_COLUMN)));
                        otsrochkaTV.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_OTSROCHKA_COLUMN)));
                        skidkaClienta = cursor.getFloat(cursor.getColumnIndex(DataBaseHelper.CLIENT_SKIDKA_COLUMN));
                        String nameOrg = cursor.getString(cursor.getColumnIndex(DataBaseHelper.CLIENT_NAMEORG_COLUMN));
                        if (nameOrg.contains(clientOrgName)){
                            break;
                        }
                    }
                    cursor.close();
                    TovarListTask tovarListTask = new TovarListTask();
                    tovarListTask.execute();
                    PositionOrderList pl = new PositionOrderList();
                    pl.execute();
                }}

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    //Заполнение остатков для формирования заказов
    private class TovarListTask extends AsyncTask<Void, Void, Void> {

        public static final String ID = "id";
        public static final String LABEL = "label";
        public static final String LOCALLABEL = "locallabel";
        public static final String KOLVO = "kolvo";
        public static final String EDIZM = "edIzm";
        public static final String CENNA = "cenna";
        public static final String VALUTA = "valuta";
        ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
        ArrayList<HashMap<String, String>> tovar = new ArrayList<HashMap<String, String>>();
        String idTovara;
        HashMap<String, String> hashmap;
        SimpleAdapter adapter_tovar;
        ListView lvTovar = (ListView) findViewById(R.id.tovar_list);
        AdapterView.OnItemClickListener click = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> itemHashMap = (HashMap<String, String>) parent.getItemAtPosition(position);
                idTovara = itemHashMap.get(ID).toString();
                idChooseTovar = Integer.parseInt(idTovara);
                ChooseTask chosser = new ChooseTask();
                chosser.execute();
            }
        };

        @Override
        protected void onPreExecute() {
            pb.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String WHERE = " kolvo>0 ";
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_TOVAR_TABLE, new String[]{
                            DataBaseHelper._ID, DataBaseHelper.TOVAR_LABEL_COLUMN, DataBaseHelper.TOVAR_LOCALLABEL_COLUMN,
                            DataBaseHelper.TOVAR_KOLVO_COLUMN, DataBaseHelper.TOVAR_ED_IZM_COLUMN,
                            DataBaseHelper.TOVAR_CENNA_COLUMN, DataBaseHelper.TOVAR_VALUTA_COLUMN},
                    WHERE, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper._ID));
                String label = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_LABEL_COLUMN));
                String locallabel = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_LOCALLABEL_COLUMN));
                String kolvo = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_KOLVO_COLUMN));
                String edIzm = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_ED_IZM_COLUMN));
                String cenna = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_CENNA_COLUMN));
                String valuta = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_VALUTA_COLUMN));
                hashmap = new HashMap<String, String>();
                hashmap.put(ID, String.format("%04d", id));
                hashmap.put(LABEL, label);
                hashmap.put(LOCALLABEL, locallabel);
                hashmap.put(KOLVO, kolvo);
                hashmap.put(EDIZM, edIzm);
                cenna=String.format("%.2f",Float.parseFloat(cenna)*skidkaClienta);
                hashmap.put(CENNA, cenna);
                hashmap.put(VALUTA, valuta);
                tovar.add(hashmap);
            }
            cursor.close();
            adapter_tovar = new SimpleAdapter(NewOrder.this, tovar,
                    R.layout.list_item_tovar, new String[]{ LABEL, LOCALLABEL,
                    KOLVO, EDIZM, CENNA, VALUTA}, new int[]{R.id.tv_label_tovar,
                    R.id.tv_locallabel_tovar, R.id.tv_kolvo_tovar, R.id.tv_edizm_tovar,
                    R.id.tv_cenna_tovar, R.id.tv_valuta_tovar});

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            lvTovar.setAdapter(adapter_tovar);
            lvTovar.setOnItemClickListener(click);
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    //Добавление полей в таблицу ОРДЕРЛИСТ
    public class AddPositionTask extends AsyncTask<Void, Void, Void> {
        String label = "";
        String locallabel = "";
        float cenna = 0;
        String usl = "_id=" + idChooseTovar;
        ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
        @Override
        protected void onPreExecute() {
            pb.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_TOVAR_TABLE,
                    new String[]{DataBaseHelper.TOVAR_LABEL_COLUMN, DataBaseHelper.TOVAR_LOCALLABEL_COLUMN,
                            DataBaseHelper.TOVAR_CENNA_COLUMN, DataBaseHelper.TOVAR_KOLVO_COLUMN}, usl, null, null, null, null);
            while (cursor.moveToNext()) {
                label = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_LABEL_COLUMN));
                locallabel = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_LOCALLABEL_COLUMN));
                cenna = cursor.getFloat(cursor.getColumnIndex(DataBaseHelper.TOVAR_CENNA_COLUMN));
                controlKolvo = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOVAR_KOLVO_COLUMN));
            }
            if (Integer.parseInt(kolTovar)>=Integer.parseInt(controlKolvo))
                kolTovar=controlKolvo;
            String WHERE = " _id="+idChooseTovar+" ";
            int kolvo= Integer.parseInt(controlKolvo)-Integer.parseInt(kolTovar);
            ContentValues valUpdate = new ContentValues();
            valUpdate.put("kolvo", kolvo);
            mSqLiteDatabase.update(DataBaseHelper.DATABASE_TOVAR_TABLE, valUpdate, WHERE, null);

            ContentValues valUpdateOL = new ContentValues();
            valUpdateOL.put("idtovar",idChooseTovar);
            valUpdateOL.put("label",label);
            valUpdateOL.put("locallabel",locallabel);
            valUpdateOL.put("kolvo",kolTovar);
            cenna*=skidkaClienta;
            valUpdateOL.put("cenna",Integer.parseInt(kolTovar) * cenna );
            valUpdateOL.put("numzakaza", orderCounter);

            mSqLiteDatabase.insert(DataBaseHelper.DATABASE_ORDERLIST_TABLE, null, valUpdateOL);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PositionOrderList pl = new PositionOrderList();
            pl.execute();
            nazvanietovara.setText("");
            input_kol.setText("");
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    //Отображение детализации заказа
    private class PositionOrderList extends AsyncTask<Void, Void, Void> {
        public static final String ID = "id";
        public static final String IDTOVAR = "idtovar";
        public static final String LABEL = "label";
        public static final String LOCALLABEL = "locallabel";
        public static final String KOLVO = "kolvo";
        public static final String CENNA = "cenna";
        ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
        ArrayList<HashMap<String, String>> orderlist = new ArrayList<HashMap<String, String>>();
        String idTovara;
        String idTovaraOnPrice;
        String kolvoForReturn;
        HashMap<String, String> hashmap;
        SimpleAdapter adapter_orderlist;
        float sumOrder=0;
        ListView lvOrder = (ListView) findViewById(R.id.order_list);
        AdapterView.OnItemLongClickListener clickthis = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> itemHashMap = (HashMap<String, String>) parent.getItemAtPosition(position);
                idTovara = itemHashMap.get(ID).toString();
                idInPosorder = Integer.parseInt(idTovara);
                idTovaraOnPrice =  itemHashMap.get(IDTOVAR).toString();
                idInTovarTable = Integer.parseInt(idTovaraOnPrice);
                kolvoForReturn = itemHashMap.get(KOLVO).toString();
                kolvoReturnOnTovar = Integer.parseInt(kolvoForReturn);
                ChooseReturnTask chosserreturn = new ChooseReturnTask();
                chosserreturn.execute();
                return true;
            }
        };

        @Override
        protected void onPreExecute() {
            pb.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String usl=" numzakaza="+orderCounter;
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_ORDERLIST_TABLE, new String[]{
                            DataBaseHelper._ID, DataBaseHelper.ORDERLIST_IDTOVAR_COLUMN, DataBaseHelper.ORDERLIST_LABEL_COLUMN,
                            DataBaseHelper.ORDERLIST_LOCALLABEL_COLUMN, DataBaseHelper.ORDERLIST_KOLVO_COLUMN,
                            DataBaseHelper.ORDERLIST_CENNA_COLUMN},
                    usl, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper._ID));
                int idtovar = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ORDERLIST_IDTOVAR_COLUMN));
                String label = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDERLIST_LABEL_COLUMN));
                String locallabel = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDERLIST_LOCALLABEL_COLUMN));
                String kolvo = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDERLIST_KOLVO_COLUMN));
                String cenna = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ORDERLIST_CENNA_COLUMN));
                hashmap = new HashMap<String, String>();
                hashmap.put(ID, String.format("%d", id));
                hashmap.put(IDTOVAR, String.format("%04d", idtovar));
                hashmap.put(LABEL, label);
                hashmap.put(LOCALLABEL, locallabel);
                hashmap.put(KOLVO, kolvo);
                sumOrder+=Float.parseFloat(cenna)*skidkaClienta;
                cenna=String.format("%.2f",Float.parseFloat(cenna)*skidkaClienta);
                hashmap.put(CENNA, cenna);

                orderlist.add(hashmap);
            }
            cursor.close();
            adapter_orderlist = new SimpleAdapter(NewOrder.this, orderlist,
                    R.layout.list_item_orderlist, new String[]{LABEL, LOCALLABEL,
                    KOLVO, CENNA}, new int[]{R.id.lv_label_tovar,
                    R.id.lv_locallabel_tovar, R.id.lv_kolvo_tovar, R.id.lv_cenna_tovar});
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sumOrderTV.setText(String.format("%.2f",sumOrder));
            lvOrder.setAdapter(adapter_orderlist);
            lvOrder.setOnItemLongClickListener(clickthis);
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    //Поток удаления позиций заказа с возвратом
    private class ChooseReturnTask extends AsyncTask<Void, Void, Void> {
        String one = "";
        int two = 0;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            String usl = "_id=" + idInPosorder;
            int kol=0;
            mSqLiteDatabase.delete(mDatabaseHelper.DATABASE_ORDERLIST_TABLE,
                    usl,null);
            String seq = "SQLITE_SEQUENCE";
            String WHEREseq = " name='orderlist'";
            mSqLiteDatabase.delete(seq,WHEREseq,null);
            String WHERE = "_id=" + idInTovarTable;
            Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_TOVAR_TABLE, new String[]{
                            DataBaseHelper.TOVAR_KOLVO_COLUMN},
                    WHERE, null, null, null, null);
            while (cursor.moveToNext()){
                kol = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.TOVAR_KOLVO_COLUMN));
            }
            cursor.close();
            ContentValues updatedValues = new ContentValues();
            updatedValues.put("kolvo", kolvoReturnOnTovar+kol);
            mSqLiteDatabase.update(mDatabaseHelper.DATABASE_TOVAR_TABLE,updatedValues ,WHERE,null);
            return null;
        }
        //Запуск потоков формирования ListView для позиций заказа и остатков.
        @Override
        protected void onPostExecute(Void result) {
            PositionOrderList pol = new PositionOrderList();
            pol.execute();
            TovarListTask tlt = new TovarListTask();
            tlt.execute();
        }
    }

    //Диалог установки даты отгрузки
    public class OtgrFragmentDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dp = new DatePickerDialog(getActivity(), AlertDialog.BUTTON_NEGATIVE, this, yy, mm, dd);

            return dp;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            dateOtgruzki.setText(day + "-" + month + "-" + year);
        }

    }

    //Диалог установки даты доставки
    public class DostavkaFragmentDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dp = new DatePickerDialog(getActivity(), AlertDialog.BUTTON_NEGATIVE, this, yy, mm, dd);

            return dp;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            dateDostavki.setText(day + "-" + month + "-" + year);
        }

    }

    //Сохранение заказа
    private class SaveOrderTask extends AsyncTask <Void, Void, Void>{

        String dateorder = dateOrder.getText().toString();
        String otgruzka = dateOtgruzki.getText().toString();
        String dostavka = dateDostavki.getText().toString();
        String sum = sumOrderTV.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("numberOrer", orderCounter);
            contentValues.put("clientName", clientOrgName);
            contentValues.put("dateorder", dateorder);
            contentValues.put("myorg", myOrgName);
            contentValues.put("dateotgruzki", otgruzka);
            contentValues.put("datedostavki", dostavka);
            contentValues.put("summ", sum);
            mSqLiteDatabase.insert(DataBaseHelper.DATABASE_ORDER_TABLE, null, contentValues);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            orderCounter++;
            PositionOrderList positionOrderList = new PositionOrderList();
            positionOrderList.execute();
            orderCount.setText(String.format("%06d", orderCounter));
            editor.putInt("orderCounter", orderCounter);
            editor.apply();
            Toast toast =Toast.makeText(NewOrder.this, "Заказ сохранен", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
