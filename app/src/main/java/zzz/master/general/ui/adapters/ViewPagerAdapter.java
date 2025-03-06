package zzz.master.general.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import zzz.master.general.ui.memory.miniMemoryFragments.Tab1;
import zzz.master.general.ui.memory.miniMemoryFragments.Tab2;
import zzz.master.general.ui.memory.miniMemoryFragments.Tab3;

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
                return new Tab1(); // Tu primer fragment
            case 1:
                return new Tab2(); // Tu segundo fragment
            case 2:
                return new Tab3(); // Tu tercer fragment
            default:
                throw new IllegalArgumentException("Posición no válida: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Número de pestañas
    }
}
