package com.example.applispotifyfinal.Model;




import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MusiqueBank {

    private ArrayList<String> musiqueListe;
    private int questionIndex;






    public MusiqueBank() {
        musiqueListe = new ArrayList<>();
        musiqueListe.add("ball w/o you by 21 savage");
        musiqueListe.add("Jocelyn Flores by XXXTENTACION");
        musiqueListe.add("Fuck Love by XXXTENTACION, Trippie Red");
        musiqueListe.add("SAD! by XXXTENTACION");
        musiqueListe.add("changes by XXXTENTACION");
        musiqueListe.add("Lucid Dreams by Juice WRLD");
        musiqueListe.add("Casper by Takeoff");
        musiqueListe.add("Attendez-moi by Guizmo");
        musiqueListe.add("Les pleurs du mal by Dinos");
        musiqueListe.add("Fusil by SCH");

    }



    public List<String> getMusiqueListe() {
        return musiqueListe;
    }

    public void setMusiqueListe(ArrayList<String> musiqueListe) {
        this.musiqueListe = musiqueListe;
    }


}
