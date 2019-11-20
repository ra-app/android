package org.raapp.messenger.conversation.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class AdminPageAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"unzugewiesen", "zugewiesen", "geschiossen"};
    private List<AdminConversationFragment> fragments;

    public AdminPageAdapter(@NonNull FragmentManager fm, List<AdminConversationFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
