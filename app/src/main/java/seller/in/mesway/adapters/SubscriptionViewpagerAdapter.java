package seller.in.mesway.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class SubscriptionViewpagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;
    private Context context;

    public SubscriptionViewpagerAdapter(FragmentActivity fragmentActivity, List<Fragment> fragmentList, Context context){
        super(fragmentActivity);
        this.fragmentList = fragmentList;
        this.context = context;

    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
