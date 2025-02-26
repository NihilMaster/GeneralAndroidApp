package zzz.master.general.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import zzz.master.general.ui.memory.miniMemoryFragments.Fragment1;
import zzz.master.general.ui.memory.miniMemoryFragments.Fragment2;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Devuelve el fragment correspondiente a la posición
        switch (position) {
            case 0:
                return new Fragment1(); // Tu primer fragment
            case 1:
                return new Fragment2(); // Tu segundo fragment
            default:
                throw new IllegalArgumentException("Posición no válida: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Número de pestañas
    }
}
