package br.eti.softlog.softlogtmsentregas;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PrincipalPagerAdapter extends FragmentPagerAdapter {
    private int tabsNumber;

    public PrincipalPagerAdapter(@NonNull FragmentManager fm, int tabs) {
        super(fm);
        this.tabsNumber = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PrincipalFragmentFirst();
            case 1:
                return new PrincipalFragmentSecond();
            case 2 :
                return new PrincipalFragmentThird();
                default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
