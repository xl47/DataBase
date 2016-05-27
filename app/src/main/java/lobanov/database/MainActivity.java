package lobanov.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBPersons mDBConnector;

    Context mContext;
    ListView mListView;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    myListAdapter myAdapter;

    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext=this;
        mDBConnector=new DBPersons(this);

        mListView=(ListView)findViewById(R.id.list);
        myAdapter=new myListAdapter(mContext,mDBConnector.selectAll());
        mListView.setAdapter(myAdapter);
        registerForContextMenu(mListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(mContext, AddActivity.class);
                startActivityForResult (i, ADD_ACTIVITY);
                updateList();
                return true;
            case R.id.deleteAll:
                mDBConnector.deleteAll();
                updateList();
                return true;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateList () {
        myAdapter.setArrayMyData(mDBConnector.selectAll());
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Person md = (Person) data.getExtras().getSerializable("Person");
            if (requestCode == UPDATE_ACTIVITY)
                mDBConnector.update(md);
            else
                mDBConnector.insert(md.get_firstname(), md.get_lastname());
            updateList();
        }
    }

    class myListAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<Person> arrayMyPersons;

        public myListAdapter (Context ctx, ArrayList<Person> arr) {
            mLayoutInflater = LayoutInflater.from(ctx);
            setArrayMyData(arr);
        }

        public ArrayList<Person> getArrayMyData() {
            return arrayMyPersons;
        }

        public void setArrayMyData(ArrayList<Person> arrayMyData) {
            this.arrayMyPersons = arrayMyData;
        }

        public int getCount () {
            return arrayMyPersons.size();
        }

        public Object getItem (int position) {

            return position;
        }

        public long getItemId (int position) {
            Person md = arrayMyPersons.get(position);
            if (md != null) {
                return md.get_id();
            }
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.item, null);

            TextView vFirstname= (TextView)convertView.findViewById(R.id.Firstname);
            TextView vLastname = (TextView)convertView.findViewById(R.id.Lastname);


            Person md = arrayMyPersons.get(position);
            vFirstname.setText(md.get_firstname());
            vLastname.setText(md.get_lastname());

            return convertView;
        }
    }
}
