package com.example.loginjuan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    // Usuario válido
    private static final String CORREO_VALIDO = "juan@correo.com";
    private static final String PASS_VALIDA = "1234";

    // Punto extra: contador de intentos fallidos (atributo de la actividad)
    private int intentos = 0;

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

        EditText etCorreo = findViewById(R.id.etCorreo);
        EditText etPass = findViewById(R.id.etPass);
        MaterialSwitch swRecordar = findViewById(R.id.swRecordar);
        Button btnContinuar = findViewById(R.id.btnContinuar);
        TextView tvMensaje = findViewById(R.id.tvMensaje);

        btnContinuar.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String pass = etPass.getText().toString();

            // 1.4 Campos vacíos: se marca la caja y no se comprueba nada más
            boolean hayVacios = false;
            if (correo.isEmpty()) {
                etCorreo.setError(getString(R.string.error_correo_vacio));
                hayVacios = true;
            }
            if (pass.isEmpty()) {
                etPass.setError(getString(R.string.error_pass_vacia));
                hayVacios = true;
            }
            if (hayVacios) {
                return;
            }

            // 1.2 Comparación con equals (nunca con ==)
            if (correo.equals(CORREO_VALIDO) && pass.equals(PASS_VALIDA)) {
                intentos = 0; // si aciertas, el contador vuelve a 0
                tvMensaje.setTextColor(getColor(R.color.correcto));
                // 1.3 Recordar contraseña
                if (swRecordar.isChecked()) {
                    tvMensaje.setText(R.string.msg_correcto_recordar);
                } else {
                    tvMensaje.setText(R.string.msg_correcto);
                }
            } else {
                intentos++;
                tvMensaje.setTextColor(getColor(R.color.incorrecto));
                if (intentos >= 3) {
                    // Punto extra: al tercer fallo se bloquea el botón
                    btnContinuar.setEnabled(false);
                    tvMensaje.setText(R.string.msg_demasiados);
                    Toast.makeText(this, R.string.toast_demasiados, Toast.LENGTH_LONG).show();
                } else {
                    tvMensaje.setText(R.string.msg_incorrecto);
                }
            }
        });
    }
}
