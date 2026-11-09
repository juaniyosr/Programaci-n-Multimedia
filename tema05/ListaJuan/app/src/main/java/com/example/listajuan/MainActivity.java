package com.example.listajuan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Los datos: 8 elementos
        ArrayList<Videojuego> videojuegos = new ArrayList<>();
        videojuegos.add(new Videojuego("Zelda: Breath of the Wild", "Nintendo Switch", R.drawable.icono_estrella));
        videojuegos.add(new Videojuego("Mario Kart 8", "Nintendo Switch", R.drawable.icono_rayo));
        videojuegos.add(new Videojuego("The Witcher 3", "PC / PS4 / Xbox One", R.drawable.icono_corazon));
        videojuegos.add(new Videojuego("God of War", "PlayStation", R.drawable.icono_circulo));
        videojuegos.add(new Videojuego("Hollow Knight", "PC / Switch", R.drawable.icono_estrella));
        videojuegos.add(new Videojuego("Celeste", "PC / Switch", R.drawable.icono_rayo));
        videojuegos.add(new Videojuego("Stardew Valley", "PC / Móvil", R.drawable.icono_corazon));
        videojuegos.add(new Videojuego("Minecraft", "Multiplataforma", R.drawable.icono_circulo));

        // 2. La lista: cómo se colocan las filas y quién las pinta
        RecyclerView rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvLista.setAdapter(new AdaptadorVideojuegos(videojuegos));
    }
}
