package com.example.reproductorjuan;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private SeekBar seekBar;
    private TextView tvActual, tvTotal;
    private boolean arrastrando = false;


    private SoundPool soundPool;
    private final int[] idsEfectos = new int[4];
    private final Button[] botonesEfectos = new Button[4];

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable actualizar = new Runnable() {
        @Override
        public void run() {
            if (!arrastrando) {
                long posicion = player.getCurrentPosition();
                seekBar.setProgress((int) posicion);
                tvActual.setText(formatear(posicion));
            }
            handler.postDelayed(this, 500);
        }
    };

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

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPausa = findViewById(R.id.btnPausa);
        seekBar = findViewById(R.id.seekBar);
        tvActual = findViewById(R.id.tvActual);
        tvTotal = findViewById(R.id.tvTotal);

        player = new ExoPlayer.Builder(this).build();

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int estado) {
                if (estado == Player.STATE_READY) {
                    long duracion = player.getDuration();
                    seekBar.setMax((int) duracion);
                    tvTotal.setText(formatear(duracion));
                }
            }
        });

        Uri uri = Uri.parse("android.resource://"
                + getPackageName() + "/" + R.raw.cancion);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();

        btnPlay.setOnClickListener(v -> {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });
        btnPausa.setOnClickListener(v -> player.pause());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar barra) {
                arrastrando = true;
            }

            @Override
            public void onProgressChanged(SeekBar barra, int progreso, boolean delUsuario) {
                if (delUsuario) {
                    tvActual.setText(formatear(progreso));
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar barra) {
                player.seekTo(barra.getProgress());
                arrastrando = false;
            }
        });


        botonesEfectos[0] = findViewById(R.id.btnEfecto1);
        botonesEfectos[1] = findViewById(R.id.btnEfecto2);
        botonesEfectos[2] = findViewById(R.id.btnEfecto3);
        botonesEfectos[3] = findViewById(R.id.btnEfecto4);

        int[] recursos = {R.raw.efecto_aplauso, R.raw.efecto_campana,
                R.raw.efecto_laser, R.raw.efecto_moneda};

        AudioAttributes atributos = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        // Cuando un sonido termina de cargar, se activa su botón
        soundPool.setOnLoadCompleteListener((pool, idSonido, estado) -> {
            if (estado == 0) {   // 0 = cargado sin errores
                for (int i = 0; i < idsEfectos.length; i++) {
                    if (idsEfectos[i] == idSonido) {
                        botonesEfectos[i].setEnabled(true);
                    }
                }
            }
        });

        // Cargar los 4 sonidos y preparar cada botón
        for (int i = 0; i < recursos.length; i++) {
            idsEfectos[i] = soundPool.load(this, recursos[i], 1);
            final int posicion = i;
            botonesEfectos[i].setOnClickListener(v ->
                    soundPool.play(idsEfectos[posicion], 1f, 1f, 1, 0, 1f));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(actualizar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();
        handler.removeCallbacks(actualizar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        soundPool.release();
    }

    private String formatear(long ms) {
        long segundos = ms / 1000;
        return String.format(Locale.getDefault(),
                "%02d:%02d", segundos / 60, segundos % 60);
    }
}