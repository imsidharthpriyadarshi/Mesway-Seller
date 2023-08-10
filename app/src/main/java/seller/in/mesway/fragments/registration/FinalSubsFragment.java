package seller.in.mesway.fragments.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seller.in.mesway.R;
import seller.in.mesway.databinding.FragmentFinalSubsBinding;


public class FinalSubsFragment extends Fragment {

    private FragmentFinalSubsBinding finalSubsBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        finalSubsBinding=FragmentFinalSubsBinding.inflate(inflater,container,false);
        return finalSubsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiView();

    }

    private void intiView() {

    }
}