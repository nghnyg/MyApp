package nagihan.myapp.models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nagihan.myapp.R;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Person> persons;

    public  View.OnClickListener mOnClickListener;
    public void setmOnClickListener(View.OnClickListener listener){

        mOnClickListener=listener;

    }

    public PersonAdapter(Context context, ArrayList<Person> persons) {
        this.context = context;
        this.persons = persons;
    }


    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view = inflate.from(parent.getContext()).inflate(R.layout.itemperson, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Person person = persons.get(position);
        holder.tvUsername.setText(persons.get(position).getUsername());
        holder.tvLanguage.setText(persons.get(position).getLanguage());
        holder.tvArea.setText(persons.get(position).getArea());
        holder.ivImage.setImageResource(persons.get(position).getPhoto());

    }


    @Override
    public int getItemCount() {
        if (persons != null) {
            return persons.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;
        public TextView tvUsername;
        public TextView tvLanguage;
        public TextView tvArea;


        public ViewHolder(View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvLanguage = (TextView) itemView.findViewById(R.id.tvLanguage);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea);

        }


    }


}
