package zzz.master.general.ui.memory;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import zzz.master.general.R;
import zzz.master.general.databinding.FragmentMemoryBinding;
import zzz.master.general.ui.adapters.ViewPagerAdapter;

public class MemoryFragment extends Fragment {

    private FragmentMemoryBinding binding;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragment con ViewBinding
        binding = FragmentMemoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar ViewModel compartido
        MemoryViewModel memoryViewModel = new ViewModelProvider(requireActivity()).get(MemoryViewModel.class);

        // Observar cambios en la visibilidad del elemento
        memoryViewModel.getItemVisibility().observe(getViewLifecycleOwner(), isVisible -> {
            binding.gifCongratsView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            Glide.with(this).asGif().load(R.drawable.dog_congrats).into(binding.gifCongratsView);
            if (isVisible){
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.kissound);
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                mediaPlayer.start();
            }
        });

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