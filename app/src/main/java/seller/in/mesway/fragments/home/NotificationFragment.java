package seller.in.mesway.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import seller.in.mesway.adapters.NotificationAdapter;
import seller.in.mesway.databinding.FragmentNotificationBinding;
import seller.in.mesway.response.NotificationsModel;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;


public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding notificationBinding;
    private SharedPreferences sharedPreferences;
    private NavController navController;
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationBinding= FragmentNotificationBinding.inflate(inflater,container,false);
        recNotification();
        return notificationBinding.getRoot();
    }


    private void recNotification() {
        RecyclerView recyclerView = notificationBinding.recNotification;
        NotificationAdapter notificationAdapter = new NotificationAdapter(getNotificationList());
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private List<NotificationsModel> getNotificationList() {
        List<NotificationsModel> notificationsModelList= new ArrayList<>();
        notificationsModelList.add(new NotificationsModel("Welcome to Mealko","12-01-2023"));
        notificationsModelList.add(new NotificationsModel("Welcome to Mealko","12-01-2023"));
        notificationsModelList.add(new NotificationsModel("Welcome to Mealko","12-01-2023"));

        return notificationsModelList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);
        initView();



    }

    private void initView() {
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=context instanceof Activity ?(Activity) context:null;


    }
}