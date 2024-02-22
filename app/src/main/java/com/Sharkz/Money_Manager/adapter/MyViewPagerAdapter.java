package com.Sharkz.Money_Manager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.Sharkz.Money_Manager.Fragments.AsetFragment;
import com.Sharkz.Money_Manager.Fragments.RecordsFragment;
import com.Sharkz.Money_Manager.Fragments.SettingFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new RecordsFragment();
            case 1:
                return new AsetFragment();
            case 2:
                return new SettingFragment();
            default:
                return new RecordsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
