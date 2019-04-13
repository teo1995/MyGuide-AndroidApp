package hr.foi.myguide.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import hr.foi.database.entities.Termin;

import hr.foi.myguide.R;
import hr.foi.myguide.TourSchedule;

public class TerminAdapter extends RecyclerView.Adapter<TerminAdapter.TerminViewHolder> {
    private Context mCtx;
    private List<Termin> terminList;
    private TerminAdapterListener terminAdapterListener;

    public TerminAdapter(Context mCtx, List<Termin> terminList) {
        this.mCtx = mCtx;
        this.terminList = terminList;
    }
    public void updateTerminsList(List<Termin> updatedList) {
        terminList.clear();
        terminList.addAll(updatedList);
        this.notifyDataSetChanged(); //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
    }

    public void setListener(TerminAdapterListener listener) {
        this.terminAdapterListener = listener;
    }

    @Override
    public TerminAdapter.TerminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_tour_schedule_layout, null);
        return new TerminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TerminAdapter.TerminViewHolder holder, final int position) {
        Termin termin = terminList.get(position);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d H:m:s");
//        sdf.format(calendar.getTimeInMillis());

        holder.terminIdTextView.setText(termin.getId_termin().toString());
        holder.tourStartTextView.setText(termin.getVrijeme_od());
        holder.tourEndTextView.setText(termin.getVrijeme_do());
        holder.meetingPointTextView.setText(termin.getAdresa());
        holder.noteTextView.setText(termin.getNapomena());


        holder.tourScheduleListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminAdapterListener.itemClicked(position);
            }
        });
        holder.tourScheduleListLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                terminAdapterListener.itemLongClicked(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return terminList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        //public Item currentItem;

        public ViewHolder(View v) {
            super(v);
            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // item clicked
                }
            });
        }
    }

    class TerminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tourStartTextView, tourEndTextView, meetingPointTextView, noteTextView, terminIdTextView;
        LinearLayout tourScheduleListLayout;

        public TerminViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            terminIdTextView = itemView.findViewById(R.id.terminIdTextView);
            tourStartTextView = itemView.findViewById(R.id.tourStartTextView);
            tourEndTextView = itemView.findViewById(R.id.tourEndTextView);
            meetingPointTextView = itemView.findViewById(R.id.meetingPointTextView);
            noteTextView = itemView.findViewById(R.id.noteTextView);

            tourScheduleListLayout = itemView.findViewById(R.id.id_tourSchedule_layout);

        }

        private void setActivity(String typeOfDisplay){

            Integer terminId = Integer.parseInt(terminIdTextView.getText().toString());
            Termin instanceToEdit;

            Intent intent = new Intent(mCtx, TourSchedule.class);
            for (int i = 0; i < terminList.size(); i++) {
                if (terminList.get(i).id_termin == terminId) {
                    instanceToEdit = terminList.get(i);
                    intent.putExtra("terminToEdit", instanceToEdit);

                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            setActivity("extraModule");
            return true;
        }

        @Override
        public void onClick(View v) {
            setActivity("googleModule");
        }
    }
}
