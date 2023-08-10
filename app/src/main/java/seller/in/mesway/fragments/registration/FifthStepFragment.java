package seller.in.mesway.fragments.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import seller.in.mesway.R;
import seller.in.mesway.adapters.SubscriptionViewpagerAdapter;
import seller.in.mesway.databinding.FragmentFifthStepBinding;
import seller.in.mesway.viewModels.SubsInfoCountViewModel;


public class FifthStepFragment extends Fragment {

    private FragmentFifthStepBinding fifthStepBinding;
    private SubsInfoCountViewModel subsInfoCountViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fifthStepBinding= FragmentFifthStepBinding.inflate(inflater,container,false);
        return fifthStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        addFragmentInList();
        initSubsViewPager();
        subsInfoCountObserver();
    }

    private void subsInfoCountObserver() {
        subsInfoCountViewModel.getSubs_info().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                fifthStepBinding.txtInfo.setText(s);
            }
        });

        subsInfoCountViewModel.getSubs_count().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                fifthStepBinding.tCount.setText(s);
            }
        });
    }

    private void initSubsViewPager() {
        fifthStepBinding.mainViewpager.setAdapter(new SubscriptionViewpagerAdapter(getActivity(),addFragmentInList(),requireActivity()));

    }

    private void initView() {
        subsInfoCountViewModel= new ViewModelProvider(requireActivity()).get(SubsInfoCountViewModel.class);


    }

    private List<Fragment> addFragmentInList(){
         List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new ThirtyDaySubsFragment());
        fragmentList.add(new FifteenDaySubFragment());
        fragmentList.add(new SevenDaySubsFragment());
        fragmentList.add(new OneDaySubsFragment());
        return fragmentList;

    }
}
