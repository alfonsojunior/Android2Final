package br.edu.unidavi.alfonso.android2final.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.alfonso.android2final.entity.Atividade;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.ViewHolder> {

    private final OnAtividadeClickListener listener;
    private List<Atividade> atividades = new ArrayList<>();
    private DateFormat format = new SimpleDateFormat("dd MMM yyyy, hh:mm");

    public AtividadeAdapter(OnAtividadeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(
                android.R.layout.simple_list_item_2,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Atividade atividade = atividades.get(position);
        holder.title.setText(atividade.getTitulo());
        holder.date.setText(format.format(atividade.getData()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(atividade);
            }
        });

//        if (atividade.isStatus()) {
//            holder.title.setTextColor(Color.RED);
//        } else {
//            holder.title.setTextColor(Color.BLACK);
//        }
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    public void setup(List<Atividade> tasks) {
        this.atividades.clear();
        this.atividades.addAll(tasks);
        notifyDataSetChanged();
    }


    class  ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            date = itemView.findViewById(android.R.id.text2);
        }
    }

    interface OnAtividadeClickListener {
        void onClick(Atividade atividade);
    }
}
