package dz.youcefmegoura.test.databasepro.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dz.youcefmegoura.test.databasepro.Database.DatabaseManager;
import dz.youcefmegoura.test.databasepro.Objects.Image;
import dz.youcefmegoura.test.databasepro.R;

public class ImageGame extends AppCompatActivity {
    /***************XML References******************/
    ImageView image_view;
    TextView score_text_view, nom_image_textView;
    EditText edit_text;
    /***********************************************/

    /*****************To Get from Bundle****************/
    private int id_categorie_from_bundle;
    private int id_niveau_from_bundle;
    /************************************************/

    private DatabaseManager databaseManager;
    private ArrayList<Image> Images_array;

    private int cursseur_id_array_image;
    private int indice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_game);

        /***************XML References******************/
        score_text_view = (TextView) findViewById(R.id.score_text_view);
        image_view = (ImageView) findViewById(R.id.image_view);
        nom_image_textView = (TextView) findViewById(R.id.nom_image_textView);
        edit_text = (EditText) findViewById(R.id.edit_text);
        /**********************************************/


        /*****************Get from Bundle****************/
        Bundle bundle = getIntent().getExtras();
        id_categorie_from_bundle = bundle.getInt("id_categorie");
        id_niveau_from_bundle = bundle.getInt("id_niveau");
        /************************************************/


        /****************Initialisation******************/
        indice = 0;
        databaseManager = new DatabaseManager(this);
        Images_array = new ArrayList<>(databaseManager.readFrom_ImageTable_where_categorie_and_niveau(id_categorie_from_bundle, id_niveau_from_bundle));
        cursseur_id_array_image = Images_array.get(indice).getId_image();

        afficher_imageObject(indice);//Afficher la premiere image dans onCreate
        /*************************************************/
    }

    //Simple methode pour afficher tout les attributs d'une image dans XML ...
    public void afficher_imageObject(int cursseur_id_array_image){
        score_text_view.setText("Score : " + String.valueOf(Images_array.get(indice).getScore_image()));
        int drawableResourceId = this.getResources().getIdentifier(Images_array.get(indice).getUrl_image(), "drawable", this.getPackageName());
        image_view.setImageResource(drawableResourceId);
        nom_image_textView.setText("Nom image : " + Images_array.get(indice).getNom_image());
    }

    //onClick Button
    public void saveClick(View view) {
        if (edit_text.getText().toString().length() != 0){
            int new_score = Integer.valueOf(edit_text.getText().toString());
            databaseManager.changer_score_image(cursseur_id_array_image, new_score);
            score_text_view.setText("Score : " + edit_text.getText().toString());

            /************** Pour changer le score du niveau et de la categorie ************/
            //Niveau
            int score_images_dans_niveau = databaseManager.score_images_dans_niveau(id_categorie_from_bundle, id_niveau_from_bundle);
            databaseManager.changer_score_niveau(id_niveau_from_bundle, score_images_dans_niveau);

            //Categorie
            int score_niveau_dans_categorie = databaseManager.score_niveaux_dans_categorie(id_categorie_from_bundle);
            databaseManager.changer_score_categorie(id_categorie_from_bundle, score_niveau_dans_categorie);
            /*****************************************************************************/

        }else{
            Toast.makeText(this, "Veillez entrer score", Toast.LENGTH_SHORT).show();
        }
    }

    //onClick Button
    public void nextClick(View view) {
        if (indice == Images_array.size() - 1)
            indice = 0;
        else
            indice++;
            cursseur_id_array_image++;
        afficher_imageObject(cursseur_id_array_image);

    }

    //onClick Button
    public void backClick(View view) {
        if (indice == 0)
            indice = Images_array.size()-1;
        else
            indice --;
            cursseur_id_array_image--;
        afficher_imageObject(cursseur_id_array_image);
    }

}
