package us.mastersalud.firebaseluisjo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActualizaActivity extends AppCompatActivity {

    // Atributos de la clase
    String nuhsa;
    EditText etNombre, etApellidos, etGrupoSanguineo;
    FirebaseDatabase database;
    DatabaseReference puntoAcceso;
    Button btnActualizarPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualiza);

        // Recupero el String de NUHSA que me pasó la actividad anterior
        nuhsa = getIntent().getStringExtra(Constantes.nuhsa);

        // Busco las vistas que tengo que modificar en mi interfaz
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etGrupoSanguineo = findViewById(R.id.etGrupoSanguineo);
        btnActualizarPaciente = findViewById(R.id.btnActualizarPaciente);

        // Instancia de mi base de datos
        database = FirebaseDatabase.getInstance();

        // Punto de acceso al paciente específico por su NUHSA
        puntoAcceso = database.getReference(Constantes.pacientes + "/" + nuhsa);

        // Listener que escucha si hay algún elemento colgando de ese punto de acceso para cargar la información del paciente
        puntoAcceso.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Si el NUHSA no está vacío y hay algo colgando en el punto de acceso
                if (!nuhsa.equals("") && dataSnapshot.exists()) {
                    // Recuperamos el paciente y lo mostramos en cada uno de los EditText para editarlo
                    Paciente paciente = dataSnapshot.getValue(Paciente.class);
                    if (paciente != null) {
                        etNombre.setText(paciente.getNombre());
                        etApellidos.setText(paciente.getApellidos());
                        etGrupoSanguineo.setText(paciente.getGrupoSanguineo());
                    }
                } else {
                    // Mostramos un mensaje de no encontrado y volvemos eliminando la activity
                    Toast.makeText(ActualizaActivity.this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // Configuramos el botón para actualizar los datos del paciente
        btnActualizarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPaciente();
            }
        });
    }

    // Método para actualizar los datos del paciente
    public void actualizarPaciente() {
        String nuevoNombre = etNombre.getText().toString().trim();
        String nuevoApellido = etApellidos.getText().toString().trim();
        String nuevoGrupoSanguineo = etGrupoSanguineo.getText().toString().trim();

        // Validamos que los campos no estén vacíos
        if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevoGrupoSanguineo.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto Paciente con los nuevos datos
        Paciente pacienteActualizado = new Paciente(nuevoNombre, nuevoApellido, nuevoGrupoSanguineo,nuhsa);

        // Actualizamos la información del paciente en la base de datos bajo el nodo "Pacientes"
        puntoAcceso.setValue(pacienteActualizado)
                .addOnSuccessListener(aVoid -> {
                    // Mostrar mensaje de éxito
                    Toast.makeText(ActualizaActivity.this, "Paciente actualizado correctamente", Toast.LENGTH_SHORT).show();

                    // Navegar de vuelta a la MainActivity después de actualizar
                    Intent intent = new Intent(ActualizaActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Finalizamos ActualizaActivity para que no quede en el historial de actividades
                })
                .addOnFailureListener(e -> {
                    // Mostrar mensaje de error
                    Toast.makeText(ActualizaActivity.this, "Error al actualizar el paciente", Toast.LENGTH_SHORT).show();
                });

        // Actualizamos también la información del paciente bajo el nodo correspondiente a su grupo sanguíneo
        DatabaseReference grupoSanguineoRef = database.getReference(nuevoGrupoSanguineo + "/" + nuhsa);
        grupoSanguineoRef.setValue(pacienteActualizado)
                .addOnSuccessListener(aVoid -> {
                    // Si el nodo de grupo sanguíneo se actualiza correctamente, mostramos un mensaje de éxito
                    Toast.makeText(ActualizaActivity.this, "Paciente también actualizado en el grupo sanguíneo", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Mostrar mensaje de error si falla la actualización del grupo sanguíneo
                    Toast.makeText(ActualizaActivity.this, "Error al actualizar el grupo sanguíneo del paciente", Toast.LENGTH_SHORT).show();
                });
    }
}
