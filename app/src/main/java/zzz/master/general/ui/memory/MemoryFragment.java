package zzz.master.general.ui.memory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import zzz.master.general.databinding.FragmentMemoryBinding;
import zzz.master.general.ui.adapters.ViewPagerAdapter;

public class MemoryFragment extends Fragment {

    private FragmentMemoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragment
        binding = FragmentMemoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el ViewPager2 y el TabLayout
        setupViewPager();

        return root;
    }

    private void setupViewPager() {
        // Obtén referencias al TabLayout y ViewPager2
        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;

        // Configura el ViewPager2 con un adaptador
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Conecta el TabLayout con el ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText("Pestaña " + (position + 1)); // Títulos de las pestañas
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}