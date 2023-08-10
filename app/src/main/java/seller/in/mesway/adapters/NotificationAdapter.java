package seller.in.mesway.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import seller.in.mesway.R;
import seller.in.mesway.response.NotificationsModel;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<NotificationsModel> notificationsModelList;

    public NotificationAdapter(List<NotificationsModel> notificationsModelList) {
        this.notificationsModelList = notificationsModelList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_notification,parent,false);
        NotificationViewHolder viewholder = new NotificationViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        NotificationsModel current_notification=notificationsModelList.get(position);
        holder.setDate(current_notification.getTime());
        holder.setTitle(current_notification.getNotification_title());
    }

    @Override
    public int getItemCount() {
        return notificationsModelList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        private final  TextView title,date;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            title= itemView.findViewById(R.id.t_title_notification);
            date= itemView.findViewById(R.id.t_date_notification);
        }

        private void setTitle(String nTitle){
            title.setText(nTitle);
        }

        private void setDate(String nDate){
            date.setText(nDate);

        }
    }
}
