package com.example.applispotifyfinal.Controler;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applispotifyfinal.Model.MusiqueBank;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.ImagesApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.PendingResult;
import com.spotify.protocol.client.PendingResultBase;
import com.spotify.protocol.client.Result;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.applispotifyfinal.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.icu.util.MeasureUnit.MILLISECOND;

public class JeuActivity extends AppCompatActivity implements View.OnClickListener {

    /*Declaration de nos Views*/
    private ImageView imageView;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;

    /*Declaration Labonne musique qui stockera la bonne reponse*/
    private String LabonneMuisique;

    /*Declaration d'une liste de boutton qui permettra de repartir nos reponses de maniere aleatoire*/
    private ArrayList<Button> buttonList = new ArrayList<>();

    /*Declaration de MusiqueBank qui stock des String(mauvaise reponses)*/
    private MusiqueBank musiqueBank;

    /*Declaration ddes credentials pour se connecté a l'api spotify*/
    private static final String CLIENT_ID = "47b2a5b2587340ad940970fe063b0b32";
    private static final String REDIRECT_URI = "http://com.example.applispotifyfinal/callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        /*Referencement de nos Views*/
        imageView = findViewById(R.id.activity_game_question_text);
        answer1 = findViewById(R.id.activity_game_answer1_btn);
        answer2 = findViewById(R.id.activity_game_answer2_btn);
        answer3 = findViewById(R.id.activity_game_answer3_btn);
        answer4 = findViewById(R.id.activity_game_answer4_btn);

        /*On remplit notre liste avec nos 4 bouttons*/
        buttonList.add(answer1);
        buttonList.add(answer2);
        buttonList.add(answer3);
        buttonList.add(answer4);

        /*On leurs associent leur onClicck methode*/
        for (Button b : buttonList ) {
            b.setOnClickListener(this);
        }

        /*Instenciation de notre liste de mauvaises reponses*/
        musiqueBank = new MusiqueBank();

    }

    /*Methode on start qui va lancer l'Authentification a l'api*/
    @Override
    protected void onStart() {
        super.onStart();
        /*Set the Connection Param (prepare the authentification) */
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        /*Utilisation de SpotifyAppRemote pour se connecter a spotify et get une instance de SpotifyRemote si il y a un probleme onFaillure nous envvera un msg d'erreur*/
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;

                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void connected() {

        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        /*Grace a playerState on reccupere la track puis de maniere asynchrone on va chercher l'image de la musique et l'a set dans notre imageView*/
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                                      @Override
                                      public void onEvent(PlayerState playerState) {
                                          final Track track = playerState.track;
                                          if(track != null)
                                          {
                                              mSpotifyAppRemote.getImagesApi().getImage(track.imageUri, Image.Dimension.X_SMALL).setResultCallback(new CallResult.ResultCallback<Bitmap>() {
                                                  @Override
                                                  public void onResult(Bitmap bitmap) {
                                                      imageView.setImageBitmap(bitmap);
                                                  }
                                              });
                                          }
                                      }
                                  });

                        /*Grace a playerState on reccupere la track(musique en train d'etre jouer) puis on réccupere la musique et l'artiste que l'on stock dans notre variable Labonnereponse */
                        mSpotifyAppRemote.getPlayerApi()
                                .subscribeToPlayerState()
                                .setEventCallback(playerState -> {
                                    final Track track = playerState.track;
                                    if (track != null) {
                                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                                        LabonneMuisique = track.name + " by " + track.artist.name;

                                        /*Ici on set la bonne reponse dans un button de maniere aleatoire*/
                                        int random = (int) (Math.random() * 3);
                                        buttonList.get(random).setText(LabonneMuisique);

                                        /*puis on set les mauvaise reponses dans les autres buttons*/
                                        for (int i = 0; i < 4; i++) {
                                            if (i == random) {

                                            } else {
                                                int secondRandom = (int) (Math.random() * 9);
                                                buttonList.get(i).setText(musiqueBank.getMusiqueListe().get(secondRandom));
                                            }
                                        }

                                    }
                                });
    }

    /*Deconnexion from App remote*/
    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    /*Ici on verifie si le joueur a taper la bonne reponse puis on lance la methode redirection si il a bon*/
    @Override
    public void onClick(View view) {



        if(((Button) view).getText() == LabonneMuisique)
        {
            Rediection();

        }
    }

    /*On change d'activité*/
    private void Rediection() {

        Intent intent = new Intent(JeuActivity.this, GoodAnswer.class);
        startActivity(intent);
    }


}