package seller.in.mesway.fragments.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.activity.App;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentFourthStepBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.MeswayServingDayResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;


public class FourthStepFragment extends Fragment {
    private FragmentFourthStepBinding fourthStepBinding;
    private SharedPreferences sharedPreferences;
    private NavController navController;
    private ApiInterface apiInterface;
    private String breakfast_time, lunch_time, dinner_time;
    private List<MeswayServingDayResponse> meswayServingDayResponseList;
    private Snackbar snackbar;

    private boolean is_breakfast_req = false, is_dinner_req = false, is_lunch_req = false;
    private ArrayAdapter<String> breakfast_time_adapter, lunch_time_adapter, dinner_time_adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fourthStepBinding = FragmentFourthStepBinding.inflate(inflater, container, false);
        return fourthStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getAllTypeServingMeal(view);
        autocompleteTvOperations(view);

    }

    private void getAllTypeServingMeal(View view) {
        fourthStepBinding.linearLoader.setVisibility(View.VISIBLE);
        Call<List<MeswayServingDayResponse>> listCall = apiInterface.get_all_mesway_serving_meal(App.getAPIKey());
        listCall.enqueue(new Callback<List<MeswayServingDayResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MeswayServingDayResponse>> call, @NonNull Response<List<MeswayServingDayResponse>> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    meswayServingDayResponseList = response.body();

                    for (int i = 0; i < meswayServingDayResponseList.size(); i++) {
                        if (Objects.equals(meswayServingDayResponseList.get(i).getServing_meals_id(), sharedPreferences.getString(Constant.MESS_SERVING_MEAL, null))) {
                            if (meswayServingDayResponseList.get(i).isBreakfast()) {
                                is_breakfast_req = true;

                            }
                            if (meswayServingDayResponseList.get(i).isLunch()) {

                                is_lunch_req = true;
                            }
                            if (meswayServingDayResponseList.get(i).isDinner()) {

                                is_dinner_req = true;
                            }

                            fourthStepBinding.linearLoader.setVisibility(View.GONE);
                        }
                    }

                    enableMealTime();

                } else {

                    try {
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();


                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();


                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MeswayServingDayResponse>> call, @NonNull Throwable t) {
                snackbar = Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                snackbar.show();

            }
        });
    }

    private void enableMealTime() {

        if (is_breakfast_req){
            fourthStepBinding.etlBreakfastTime.setEnabled(true);
        }
        if (is_lunch_req){

            fourthStepBinding.etlLunchTime.setEnabled(true);
        }

        if(is_dinner_req){
            fourthStepBinding.etlDinnerTime.setEnabled(true);

        }


    }


    private void clickHandle(View views) {
        fourthStepBinding.fourthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fourthStepReg(views);
            }
        });
        fourthStepBinding.autoCompleteTvBreakfastTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fourthStepBinding.autoCompleteTvBreakfastTime.setAdapter(breakfast_time_adapter);
                fourthStepBinding.autoCompleteTvBreakfastTime.showDropDown();

            }
        });

        fourthStepBinding.autoCompleteTvLunchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fourthStepBinding.autoCompleteTvLunchTime.setAdapter(lunch_time_adapter);
                fourthStepBinding.autoCompleteTvLunchTime.showDropDown();

            }
        });
        fourthStepBinding.autoCompleteTvDinnerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fourthStepBinding.autoCompleteTvDinnerTime.setAdapter(dinner_time_adapter);
                fourthStepBinding.autoCompleteTvDinnerTime.showDropDown();
                enableBtn();

            }
        });

        fourthStepBinding.autoCompleteTvBreakfastTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                breakfast_time = breakfast_time_adapter.getItem(i);
                enableBtn();
            }
        });
        fourthStepBinding.autoCompleteTvLunchTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lunch_time = lunch_time_adapter.getItem(i);
                enableBtn();

            }
        });

        fourthStepBinding.autoCompleteTvDinnerTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dinner_time = dinner_time_adapter.getItem(i);
                enableBtn();
            }
        });


    }

    private void fourthStepReg(View view) {
        hideKeyboard();
        fourthStepBinding.linearLoader.setVisibility(View.VISIBLE);
        String breakfast_time=null,lunch_time=null,dinner_time=null;
        if (is_breakfast_req){
            breakfast_time=fourthStepBinding.autoCompleteTvBreakfastTime.getText().toString();
        }

        if (is_lunch_req){
            lunch_time=fourthStepBinding.autoCompleteTvLunchTime.getText().toString();

        }

        if (is_dinner_req){
            dinner_time=fourthStepBinding.autoCompleteTvDinnerTime.getText().toString();
        }

        Call<ThirdStepResponse> fourthStepResponseCall=apiInterface.fourth_step(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)),lunch_time,dinner_time,breakfast_time);

        fourthStepResponseCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(@NonNull Call<ThirdStepResponse> call, @NonNull Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.fourthStepFragment, true)
                            .build();
                    ThirdStepResponse thirdStepResponse = response.body();
                    sharedPreferences.edit()
                            .putString(Constant.REGISTRATION_STEP, "5")
                            .apply();
                    try {
                        navController.navigate(FourthStepFragmentDirections.actionGlobalFifthStepFragment(), navOptions);
                    } catch (Exception ignored) {
                    }

                    fourthStepBinding.linearLoader.setVisibility(View.GONE);


                }else{
                    try {
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        fourthStepBinding.linearLoader.setVisibility(View.GONE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        fourthStepBinding.linearLoader.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ThirdStepResponse> call, @NonNull Throwable t) {
                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                fourthStepBinding.linearLoader.setVisibility(View.GONE);
            }
        });



    }

    private void enableBtn() {
        if ((breakfast_time != null) == (is_breakfast_req) && (lunch_time != null) == (is_lunch_req) && (dinner_time != null) == (is_dinner_req)) {
            fourthStepBinding.fourthBtn.setEnabled(true);
        } else {
            fourthStepBinding.fourthBtn.setEnabled(false);

        }


    }

    private void autocompleteTvOperations(View views) {
        List<String> breakfast_time_interval = new ArrayList<>();
        breakfast_time_interval.add("6:00-7:00");
        breakfast_time_interval.add("7:00-8:00");
        breakfast_time_interval.add("8:00-9:00");
        breakfast_time_interval.add("9:00-10:00");
        breakfast_time_interval.add("7:00-9:00");
        breakfast_time_interval.add("8:00-10:00");


        List<String> lunch_time_interval = new ArrayList<>();

        lunch_time_interval.add("12:00-1:00");
        lunch_time_interval.add("1:00-2:00");
        lunch_time_interval.add("12:00-2:00");


        List<String> dinner_time_interval = new ArrayList<>();
        dinner_time_interval.add("6:00-7:00");
        dinner_time_interval.add("7:00-8:00");
        dinner_time_interval.add("8:00-9:00");
        dinner_time_interval.add("9:00-10:00");
        dinner_time_interval.add("8:00-10:00");
        dinner_time_interval.add("7:00-9:00");


        breakfast_time_adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, breakfast_time_interval);
        lunch_time_adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, lunch_time_interval);
        dinner_time_adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, dinner_time_interval);


        clickHandle(views);


    }

    private void initView(View view) {
        navController = Navigation.findNavController(view);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());


    }

    private void hideKeyboard() {
        View views = requireActivity().getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

}