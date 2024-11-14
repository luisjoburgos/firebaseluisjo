package us.mastersalud.firebaseluisjo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PacientesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPacientes;
    private PacienteAdapter pacienteAdapter;
    private List<Paciente> listaPacientes;
    private FirebaseDatabase database;
    private DatabaseReference pacientesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        // Inicializar RecyclerView y otros elementos
        recyclerViewPacientes = findViewById(R.id.recyclerViewPacientes);
        recyclerViewPacientes.setLayoutManager(new LinearLayoutManager(this));

        listaPacientes = new ArrayList<>();
        pacienteAdapter = new PacienteAdapter(listaPacientes);
        recyclerViewPacientes.setAdapter(pacienteAdapter);

        // Inicializar Firebase Database y referencia a "Pacientes"
        database = FirebaseDatabase.getInstance();
        pacientesRef = database.getReference(Constantes.pacientes);

        // Leer los pacientes desde Firebase y añadirlos a la lista
        pacientesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPacientes.clear(); // Limpiar la lista antes de actualizarla con nuevos datos
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Paciente paciente = snapshot.getValue(Paciente.class);
                    if (paciente != null) {
                        listaPacientes.add(paciente);
                    }
                }
                pacienteAdapter.notifyDataSetChanged(); // Notificar al adaptador para actualizar la vista
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PacientesActivity.this, "Error al cargar los pacientes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
