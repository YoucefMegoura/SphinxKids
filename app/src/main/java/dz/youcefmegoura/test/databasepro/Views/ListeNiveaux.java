package dz.youcefmegoura.test.databasepro.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import dz.youcefmegoura.test.databasepro.Database.DatabaseManager;
import dz.youcefmegoura.test.databasepro.Objects.Niveau;
import dz.youcefmegoura.test.databasepro.R;
import dz.youcefmegoura.test.databasepro.Views.Menu.AboutUs;

/**
 * Created by Youcef Mégoura and Moussaoui Mekka on 21/04/2018.
 */

public class ListeNiveaux extends AppCompatActivity {

    private ArrayList<Niveau> ListeNiveaux_array = new ArrayList<>();
    private DatabaseManager databaseManager;
    private int id_categorie_from_bundle;
    private GridView gridView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_niveaux);


        /*****************Get from Bundle****************/
        Bundle bundle = getIntent().getExtras();
        id_categorie_from_bundle = bundle.getInt("id_categorie");



        /************************************************/
        databaseManager = new DatabaseManager( this , ListeCategories.DB_NAME);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    //creation Menu toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;

    }

    //select item menu toolbar


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myintent;
        int id = item.getItemId();
        switch (id){
            case R.id.setting_id:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutus_id:
                myintent= new Intent(this, AboutUs.class);
                startActivity(myintent);
                break;
            case R.id.dashboard_id:
                Toast.makeText(this, "dashboard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_id:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);


    }

    ////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();
        ListeNiveaux_array = new ArrayList<Niveau>(databaseManager.readFrom_NiveauTable_where_categorie(id_categorie_from_bundle)) ;

        CustAdapt cus = new CustAdapt(ListeNiveaux_array);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(cus);
        databaseManager.close();
    }

    public void back_click(View view) {
        finish();
    }

    class CustAdapt extends BaseAdapter {

        private final static int SCORE_TO_UNLOCK_LEVEL = 20;

        ArrayList<Niveau> items = new ArrayList<>();

        CustAdapt(ArrayList items){
            this.items = items;
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.indexOf(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater myInflater = getLayoutInflater();
            View myView = myInflater.inflate(R.layout.template_niveau, null);

            TextView id_niveau = myView.findViewById(R.id.id_niveau);
            SimpleRatingBar simpleRatingBar = myView.findViewById(R.id.ratingBarID);
            ImageView imageView = myView.findViewById(R.id.Locked_IV);
            id_niveau.setText(String.valueOf(position + 1));
            simpleRatingBar.setRating(items.get(position).getScore_niveau()/(databaseManager.combien_dimage_dans_niveau(id_categorie_from_bundle, items.get(position).getId_niveau())));

            if( position != 0 && items.get(position - 1).getScore_niveau() < SCORE_TO_UNLOCK_LEVEL){
                imageView.setVisibility(View.VISIBLE);
                //TODO : set enabled
                myView.setEnabled(false);
                myView.setOnClickListener(null);
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id_niveau", ListeNiveaux_array.get(position).getId_niveau());
                    bundle.putInt("id_categorie", id_categorie_from_bundle);
                    Intent intent = new Intent(ListeNiveaux.this, ImageGame.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return myView;
        }
    }
}
