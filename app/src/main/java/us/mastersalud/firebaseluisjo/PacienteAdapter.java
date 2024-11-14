package us.mastersalud.firebaseluisjo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder> {

    private List<Paciente> listaPacientes;

    public PacienteAdapter(List<Paciente> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    @NonNull
    @Override
    public PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente, parent, false);
        return new PacienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteViewHolder holder, int position) {
        Paciente paciente = listaPacientes.get(position);
        holder.tvNombre.setText(paciente.getNombre());
        holder.tvApellidos.setText(paciente.getApellidos());
        holder.tvGrupoSanguineo.setText(paciente.getGrupoSanguineo());
        holder.tvNuhsa.setText(paciente.getNuhsa());
    }

    @Override
    public int getItemCount() {
        return listaPacientes.size();
    }

    public static class PacienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvApellidos, tvGrupoSanguineo, tvNuhsa;

        public PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvApellidos = itemView.findViewById(R.id.tvApellidos);
            tvGrupoSanguineo = itemView.findViewById(R.id.tvGrupoSanguineo);
            tvNuhsa = itemView.findViewById(R.id.tvNuhsa);
        }
    }
}
