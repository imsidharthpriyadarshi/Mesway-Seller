package seller.in.mesway.fragments.registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentThirdStepBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.SecondStepResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;


public class ThirdStepFragment extends Fragment {
    private FragmentThirdStepBinding thirdStepBinding;
    private SharedPreferences sharedPreferences;
    private NavController navController;
    private String longitude = null, latitude = null;
    private Activity activity;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Snackbar snackbar;
    private ApiInterface apiInterface;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thirdStepBinding = FragmentThirdStepBinding.inflate(inflater, container, false);
        return thirdStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        clickHandle(view);
        textFieldOperation();


    }

    private void textFieldOperation() {
        thirdStepBinding.secondPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                thirdStepBinding.secondStepBtn.setEnabled(pinCodeValidator()&&textFieldValidate("Pincode", Objects.requireNonNull(thirdStepBinding.secondPinCode.getText()).toString(),thirdStepBinding.layoutSecondPincode) && textFieldValidate("Address", Objects.requireNonNull(thirdStepBinding.secondCompanyAddress.getText()).toString(),thirdStepBinding.layoutSecondAddress) && textFieldValidate("District", Objects.requireNonNull(thirdStepBinding.secondDistrict.getText()).toString(),thirdStepBinding.layoutSecondDistrict) &&textFieldValidate("State", Objects.requireNonNull(thirdStepBinding.secondState.getText()).toString(),thirdStepBinding.layoutSecondState));
            }
        });

        thirdStepBinding.secondCompanyAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                thirdStepBinding.secondStepBtn.setEnabled(pinCodeValidator()&&textFieldValidate("Pincode", Objects.requireNonNull(thirdStepBinding.secondPinCode.getText()).toString(),thirdStepBinding.layoutSecondPincode) && textFieldValidate("Address", Objects.requireNonNull(thirdStepBinding.secondCompanyAddress.getText()).toString(),thirdStepBinding.layoutSecondAddress) && textFieldValidate("District", Objects.requireNonNull(thirdStepBinding.secondDistrict.getText()).toString(),thirdStepBinding.layoutSecondDistrict) &&textFieldValidate("State", Objects.requireNonNull(thirdStepBinding.secondState.getText()).toString(),thirdStepBinding.layoutSecondState));

            }
        });

        thirdStepBinding.secondBuildingNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                thirdStepBinding.secondStepBtn.setEnabled(pinCodeValidator()&&textFieldValidate("Pincode", Objects.requireNonNull(thirdStepBinding.secondPinCode.getText()).toString(),thirdStepBinding.layoutSecondPincode) && textFieldValidate("Address", Objects.requireNonNull(thirdStepBinding.secondCompanyAddress.getText()).toString(),thirdStepBinding.layoutSecondAddress) && textFieldValidate("District", Objects.requireNonNull(thirdStepBinding.secondDistrict.getText()).toString(),thirdStepBinding.layoutSecondDistrict) &&textFieldValidate("State", Objects.requireNonNull(thirdStepBinding.secondState.getText()).toString(),thirdStepBinding.layoutSecondState));

            }
        });

        thirdStepBinding.secondLandmark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                thirdStepBinding.secondStepBtn.setEnabled(pinCodeValidator()&&textFieldValidate("Pincode", Objects.requireNonNull(thirdStepBinding.secondPinCode.getText()).toString(),thirdStepBinding.layoutSecondPincode) && textFieldValidate("Address", Objects.requireNonNull(thirdStepBinding.secondCompanyAddress.getText()).toString(),thirdStepBinding.layoutSecondAddress) && textFieldValidate("District", Objects.requireNonNull(thirdStepBinding.secondDistrict.getText()).toString(),thirdStepBinding.layoutSecondDistrict) &&textFieldValidate("State", Objects.requireNonNull(thirdStepBinding.secondState.getText()).toString(),thirdStepBinding.layoutSecondState));

            }
        });

        thirdStepBinding.secondDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                thirdStepBinding.secondStepBtn.setEnabled(pinCodeValidator()&&textFieldValidate("Pincode", Objects.requireNonNull(thirdStepBinding.secondPinCode.getText()).toString(),thirdStepBinding.layoutSecondPincode) && textFieldValidate("Address", Objects.requireNonNull(thirdStepBinding.secondCompanyAddress.getText()).toString(),thirdStepBinding.layoutSecondAddress) && textFieldValidate("District", Objects.requireNonNull(thirdStepBinding.secondDistrict.getText()).toString(),thirdStepBinding.layoutSecondDistrict) &&textFieldValidate("State", Objects.requireNonNull(thirdStepBinding.secondState.getText()).toString(),thirdStepBinding.layoutSecondState));

            }
        });

        thirdStepBinding.secondState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                thirdStepBinding.secondStepBtn.setEnabled(pinCodeValidator()&&textFieldValidate("Pincode", Objects.requireNonNull(thirdStepBinding.secondPinCode.getText()).toString(),thirdStepBinding.layoutSecondPincode) && textFieldValidate("Address", Objects.requireNonNull(thirdStepBinding.secondCompanyAddress.getText()).toString(),thirdStepBinding.layoutSecondAddress) && textFieldValidate("District", Objects.requireNonNull(thirdStepBinding.secondDistrict.getText()).toString(),thirdStepBinding.layoutSecondDistrict) &&textFieldValidate("State", Objects.requireNonNull(thirdStepBinding.secondState.getText()).toString(),thirdStepBinding.layoutSecondState));
                
            }
        });
    }

    private void requestLocationPermission() throws Settings.SettingNotFoundException {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                Criteria locationCriteria = new Criteria();
                locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    onGPS();
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
                }

            }else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Plz Enable precise location")
                            .setMessage("Mesway uses precise location to serves you better.")
                            .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                    settingsIntent.setData(uri);
                                    startActivity(settingsIntent);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setMessage("It looks like you have turned off permission required for this features. It can be enabled under Phone Settings> App> Mesway > Permission")
                            .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                    settingsIntent.setData(uri);
                                    startActivity(settingsIntent);
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            }else {
                requestFinePermissionLauncher.launch( Manifest.permission.ACCESS_FINE_LOCATION);
            }

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                    .setMessage("It looks like you have turned off permission required for this features. It can be enabled under Phone Settings> App> Mesway > Permission")
                    .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            settingsIntent.setData(uri);
                            startActivity(settingsIntent);
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }

    }


    private void startLocationListener(View view) {
        locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
                try {
                    onGPS();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Plz turn On location", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());


                if (longitude!=null && latitude!=null) {
                    enableAllEditText();

                }else {
                    snackbar = Snackbar.make(view, "Something went wrong when we are trying to get your current location", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                    snackbar.show();

                }

                //callGeoCoder(latitude,longitude);


                // Toast.makeText(activity, "" + longitude + " " + latitude, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //getting error
                //LocationListener.super.onStatusChanged(provider, status, extras);
            }


        };

    }

    private void enableAllEditText() {
        thirdStepBinding.layoutSecondPincode.setEnabled(true);
        thirdStepBinding.layoutSecondAddress.setEnabled(true);
        thirdStepBinding.layoutSecondBuildingNo.setEnabled(true);
        thirdStepBinding.layoutSecondLandmark.setEnabled(true);
        thirdStepBinding.layoutSecondDistrict.setEnabled(true);
        thirdStepBinding.layoutSecondState.setEnabled(true);

    }


    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                        Criteria locationCriteria = new Criteria();
                        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
                        locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            try {
                                onGPS();
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(activity, "Plz turn on location", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
                        }

                    }else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                                    .setTitle("Plz Enable precise location")
                                    .setMessage("Mesway uses precise location to serve you better.")
                                    .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            try {
                                                requestLocationPermission();
                                            } catch (Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();



                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                                    .setMessage("It looks like you have turned off permission required for this features. It can be enabled under Phone Settings> App> Mesway > Permission")
                                    .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                            settingsIntent.setData(uri);
                                            startActivity(settingsIntent);
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }

                    }else {
                        requesetFineLocation();
                    }
                }  else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Location Permission Denied")
                            .setMessage("Mesway uses this permission to detect your current location and show you great tifin provider around you. Are you sure you want to deny this permission?")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        requestLocationPermission();
                                    } catch (Settings.SettingNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            });

    private void onGPS() throws Settings.SettingNotFoundException {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        if (getLocationMode(activity) == 2) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                    .setTitle("Plz Enable precise location")
                    .setMessage("Mesway uses precise location to serve you better.")
                    .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;

        }
        builder.setMessage("Plz enable your location")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        android.app.AlertDialog locationOnDialog = builder.create();
        locationOnDialog.show();

    }

    private int getLocationMode(Context context) throws Settings.SettingNotFoundException {
        return Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE);
    }
    private void requesetFineLocation() {
        requestFinePermissionLauncher.launch( Manifest.permission.ACCESS_FINE_LOCATION);

    }

    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String> requestFinePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                    Criteria locationCriteria = new Criteria();
                    locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
                    locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            onGPS();
                        } catch (Settings.SettingNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Plz, Turn on location", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);

                    }
                } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Plz Enable precise location")
                            .setMessage("Mesway uses precise location to serve you better.")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        requestLocationPermission();
                                    } catch (Settings.SettingNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Location Permission Denied")
                            .setMessage("Mesway uses this permission to detect your current location and show you great tifin provider around you. Are you sure you want to deny this permission?")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        requestLocationPermission();
                                    } catch (Settings.SettingNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            });


    private void clickHandle(View views) {
        thirdStepBinding.myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationListener(view);
                try {
                    requestLocationPermission();
                } catch (Settings.SettingNotFoundException e) {
                    Toast.makeText(activity, "Turn on Your location", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        thirdStepBinding.secondStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regThirdStep(views);

            }
        });
    }

    private void regThirdStep(View view) {
        hideKeyboard();
        thirdStepBinding.linearLoader.setVisibility(View.VISIBLE);
        String building_no=null,landmark=null;

        if (!TextUtils.isEmpty(Objects.requireNonNull(thirdStepBinding.secondBuildingNo.getText()).toString().trim())){
            building_no=thirdStepBinding.secondBuildingNo.getText().toString().trim();


        }
        if (!TextUtils.isEmpty(Objects.requireNonNull(thirdStepBinding.secondLandmark.getText()).toString().trim())){
            landmark=thirdStepBinding.secondLandmark.getText().toString().trim();


        }


        Call<ThirdStepResponse> thirdStepResponseCall= apiInterface.third_step(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)), building_no, Objects.requireNonNull(thirdStepBinding.secondPinCode.getText()).toString(), landmark,latitude,longitude, Objects.requireNonNull(thirdStepBinding.secondCompanyAddress.getText()).toString(), Objects.requireNonNull(thirdStepBinding.secondDistrict.getText()).toString(), Objects.requireNonNull(thirdStepBinding.secondState.getText()).toString());

        thirdStepResponseCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(@NonNull Call<ThirdStepResponse> call, @NonNull Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.thirdStepFragment, true)
                            .build();
                    ThirdStepResponse thirdStepResponse = response.body();
                    sharedPreferences.edit()
                            .putString(Constant.REGISTRATION_STEP, "4")
                            .apply();
                    try {
                        navController.navigate(ThirdStepFragmentDirections.actionGlobalFourthStepFragment(), navOptions);
                    } catch (Exception ignored) {
                    }

                    thirdStepBinding.linearLoader.setVisibility(View.GONE);


                }else{
                    try {
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        thirdStepBinding.linearLoader.setVisibility(View.GONE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        thirdStepBinding.linearLoader.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ThirdStepResponse> call, @NonNull Throwable t) {
                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                thirdStepBinding.linearLoader.setVisibility(View.GONE);

            }
        });



    }

    private void initView(View view) {
        navController= Navigation.findNavController(view);
        Retrofit retrofit = ApiClient.getClient();

        apiInterface=retrofit.create(ApiInterface.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();

        }
    }
    private boolean textFieldValidate(String name,String text, TextInputLayout editTextLayout){
        if (!TextUtils.isEmpty(text)){
            editTextLayout.setErrorEnabled(false);

            return  true;

        }
        editTextLayout.setErrorEnabled(true);
        editTextLayout.setError(name+" Required*");
        return  false;

    }
    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

    private boolean pinCodeValidator(){
        if (!TextUtils.isEmpty(thirdStepBinding.secondPinCode.getText())){
            if (thirdStepBinding.secondPinCode.getText().length()==6){
                thirdStepBinding.layoutSecondPincode.setErrorEnabled(false);
                return true;

            }else{
                thirdStepBinding.layoutSecondPincode.setErrorEnabled(true);
                thirdStepBinding.layoutSecondPincode.setError("Not valid Pincode");

                return false;
            }



        }

        thirdStepBinding.layoutSecondPincode.setErrorEnabled(true);
        thirdStepBinding.layoutSecondPincode.setError("Not valid Pincode");
        return  false;
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);

    }
}