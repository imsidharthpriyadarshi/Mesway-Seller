package seller.in.mesway.fragments.registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;
import com.github.dhaval2404.imagepicker.listener.DismissListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.net.URISyntaxException;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seller.in.mesway.R;
import seller.in.mesway.activity.App;
import seller.in.mesway.activity.MainActivity;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentSeventhStepBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.PathUtils;


public class SeventhStepFragment extends Fragment {

    private FragmentSeventhStepBinding seventhStepBinding;
    private String path;
    private boolean is_poster_uploaded = false, is_menu_uploaded = false;
    private SharedPreferences sharedPreferences;
    private Snackbar snackbar;
    private ApiInterface apiInterface;
    private View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        seventhStepBinding = FragmentSeventhStepBinding.inflate(inflater, container, false);
        return seventhStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        clickHandle(view);

    }

    private void initView(View views) {
        view = views;
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    private void clickHandle(View views) {
        seventhStepBinding.frontLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seventhStepBinding.frontLinearLayout.setVisibility(View.GONE);
                seventhStepBinding.progressBarPoster.setVisibility(View.VISIBLE);

                ImagePicker.with(requireActivity())
                        .provider(ImageProvider.BOTH)
                        .crop(3f, 2f)
                        .compress(5000)
                        .setDismissListener(new DismissListener() {
                            @Override
                            public void onDismiss() {
                                seventhStepBinding.frontLinearLayout.setVisibility(View.VISIBLE);
                                seventhStepBinding.progressBarPoster.setVisibility(View.GONE);

                            }
                        })
                        .createIntent(intent -> {
                            imagePickerActivityResult.launch(intent);
                            return null;
                        });
            }
        });

        seventhStepBinding.imgPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImagePicker.with(requireActivity())
                        .provider(ImageProvider.BOTH)
                        .crop(3f, 2f)
                        .compress(5000)
                        .createIntent(intent -> {
                            imagePickerActivityResult.launch(intent);
                            return null;
                        });
            }
        });

        seventhStepBinding.imgMessMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(requireActivity())
                        .provider(ImageProvider.BOTH)
                        .crop(2f, 3f)
                        .compress(5000)
                        .createIntent(intent -> {
                            imagePickerActivityResultMenu.launch(intent);
                            return null;
                        });
            }
        });

        seventhStepBinding.lnUploadMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(requireActivity())
                        .provider(ImageProvider.BOTH)
                        .crop(2f, 3f)
                        .compress(5000)
                        .createIntent(intent -> {
                            imagePickerActivityResultMenu.launch(intent);
                            return null;
                        });
            }
        });

        seventhStepBinding.finalizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finalStep(views);
            }
        });
    }

    private void finalStep(View view) {

        seventhStepBinding.linearLoader.setVisibility(View.VISIBLE);
        Call<Boolean> finalStepCall = apiInterface.finalize_step(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID, null)));
        finalStepCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    Boolean finalize = response.body();
                    sharedPreferences.edit()
                            .putString(Constant.REGISTRATION_STEP, "8")
                            .apply();
                    startActivity(new Intent(requireActivity(), MainActivity.class));
                    requireActivity().finish();
                    seventhStepBinding.linearLoader.setVisibility(View.GONE);


                }else {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, response.code()+": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        seventhStepBinding.linearLoader.setVisibility(View.GONE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        seventhStepBinding.linearLoader.setVisibility(View.GONE);

                    }



                }


            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                seventhStepBinding.linearLoader.setVisibility(View.GONE);
            }
        });


    }

    ActivityResultLauncher<Intent> imagePickerActivityResultMenu = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {


                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            seventhStepBinding.progressBarMenu.setVisibility(View.VISIBLE);
                            seventhStepBinding.imgMessMenu.setEnabled(false);
                            seventhStepBinding.menuLinearLayout.setVisibility(View.INVISIBLE);
                            //posterSettingsBinding.imageLoaderProgressbar.setVisibility(View.VISIBLE);


                            Uri imageUri = result.getData().getData();


                            Glide.with(requireView()).load(imageUri).into(seventhStepBinding.imgMessMenu);

                           /* if (seventhStepBinding.imgPoster.isAttachedToWindow()){

                                Toast.makeText(requireActivity(), "haaa", Toast.LENGTH_SHORT).show();
                            }*/

                            //   posterSettingsBinding.textSample.setVisibility(View.GONE);

                            try {
                                path = PathUtils.getPath(requireActivity(), imageUri);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }

                            assert path != null;
                            File file = new File(path);
                            BasicAWSCredentials credential = new BasicAWSCredentials(App.getAwsAccessKey(), App.getAwsSecretKey());
                            AmazonS3Client s3 = new AmazonS3Client(credential, Region.getRegion(Regions.AP_SOUTH_1));
                            java.security.Security.setProperty("networkaddress.cache.ttl", "60");
                            s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
                            s3.setEndpoint("https://s3.ap-south-1.amazonaws.com");
                            TransferUtility transferUtility = TransferUtility.builder().defaultBucket("mesway-data").context(requireActivity()).s3Client(s3).build();
                            TransferObserver transferObserver = transferUtility.upload("mesway-data", "routine/" + sharedPreferences.getString(Constant.MESS_ID, null) + "routine", file, CannedAccessControlList.Private);
                            transferObserver.setTransferListener(new TransferListener() {
                                @Override
                                public void onStateChanged(int id, TransferState state) {
                                    if (state == TransferState.COMPLETED) {
                                        // fifthCompa nySignupBinding.buttonFifth.setEnabled(true);

                                        seventhStepBinding.progressBarMenu.setVisibility(View.GONE);
                                        seventhStepBinding.imgMessMenu.setEnabled(true);

                                    } else if (state == TransferState.FAILED) {
                                        snackbar = Snackbar.make(view, state + "", Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                                        snackbar.show();

                                        seventhStepBinding.progressBarMenu.setVisibility(View.GONE);
                                        seventhStepBinding.imgMessMenu.setEnabled(true);

                                        Log.d("msg", "fail");
                                    }
                                }

                                @Override
                                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                                    if (bytesCurrent == bytesTotal) {
                                        // posterSettingsBinding.buttonFifth.setEnabled(true);

                                        seventhStepBinding.progressBarMenu.setVisibility(View.GONE);
                                        seventhStepBinding.imgMessMenu.setEnabled(true);
                                        is_menu_uploaded = true;
                                        enableBtn();
                                        snackbar = Snackbar.make(view, "Menu Uploaded Successfully", Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green));

                                        snackbar.show();

                                    }
                                }

                                @Override
                                public void onError(int id, Exception ex) {
                                    snackbar = Snackbar.make(view, "Failed: Check you internet connection/Something went wrong", Snackbar.LENGTH_SHORT);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                                    snackbar.show();

                                    seventhStepBinding.progressBarMenu.setVisibility(View.GONE);
                                    seventhStepBinding.imgMessMenu.setEnabled(true);


                                    Log.d("error", ex.toString());

                                }
                            });


                        }
                    }

                }
            }
    );

    ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            seventhStepBinding.progressBarPoster.setVisibility(View.VISIBLE);
                            seventhStepBinding.imgPoster.setEnabled(false);

                            seventhStepBinding.frontLinearLayout.setVisibility(View.GONE);
                            //posterSettingsBinding.imageLoaderProgressbar.setVisibility(View.VISIBLE);


                            Uri imageUri = result.getData().getData();


                            Glide.with(requireView()).load(imageUri).into(seventhStepBinding.imgPoster);


                            //   posterSettingsBinding.textSample.setVisibility(View.GONE);

                            try {
                                path = PathUtils.getPath(requireActivity(), imageUri);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }

                            assert path != null;
                            File file = new File(path);
                            BasicAWSCredentials credential = new BasicAWSCredentials(App.getAwsAccessKey(), App.getAwsSecretKey());
                            AmazonS3Client s3 = new AmazonS3Client(credential, Region.getRegion(Regions.AP_SOUTH_1));
                            java.security.Security.setProperty("networkaddress.cache.ttl", "60");
                            s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
                            s3.setEndpoint("https://s3.ap-south-1.amazonaws.com");
                            TransferUtility transferUtility = TransferUtility.builder().defaultBucket("mesway-data").context(requireActivity()).s3Client(s3).build();
                            TransferObserver transferObserver = transferUtility.upload("mesway-data", "mess_poster/" + sharedPreferences.getString(Constant.MESS_ID, null) + "first", file, CannedAccessControlList.Private);
                            transferObserver.setTransferListener(new TransferListener() {
                                @Override
                                public void onStateChanged(int id, TransferState state) {
                                    if (state == TransferState.COMPLETED) {
                                        // fifthCompa nySignupBinding.buttonFifth.setEnabled(true);

                                        seventhStepBinding.progressBarPoster.setVisibility(View.GONE);
                                        seventhStepBinding.imgPoster.setEnabled(true);

                                    } else if (state == TransferState.FAILED) {
                                        snackbar = Snackbar.make(view, state + "", Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                                        snackbar.show();

                                        seventhStepBinding.progressBarPoster.setVisibility(View.GONE);
                                        seventhStepBinding.imgPoster.setEnabled(true);

                                        Log.d("msg", "fail");
                                    }
                                }

                                @Override
                                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                                    if (bytesCurrent == bytesTotal) {
                                        // posterSettingsBinding.buttonFifth.setEnabled(true);

                                        seventhStepBinding.progressBarPoster.setVisibility(View.GONE);
                                        seventhStepBinding.imgPoster.setEnabled(true);
                                        is_poster_uploaded = true;
                                        enableBtn();
                                        snackbar = Snackbar.make(view, "Poster Uploaded Successfully", Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green));

                                        snackbar.show();

                                    }
                                }

                                @Override
                                public void onError(int id, Exception ex) {
                                    snackbar = Snackbar.make(view, "Failed: Check you internet connection/Something went wrong", Snackbar.LENGTH_SHORT);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                                    snackbar.show();

                                    seventhStepBinding.progressBarPoster.setVisibility(View.GONE);
                                    seventhStepBinding.imgPoster.setEnabled(true);


                                    Log.d("error", ex.toString());

                                }
                            });


                        }
                    }


                }
            }
    );

    private void enableBtn() {
        if (is_poster_uploaded && is_menu_uploaded) {
            seventhStepBinding.finalizeBtn.setEnabled(true);


        } else {
            seventhStepBinding.finalizeBtn.setEnabled(false);
        }
    }
}