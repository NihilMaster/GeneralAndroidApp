package zzz.master.general;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import zzz.master.general.databinding.ActivityMainBinding;
import zzz.master.general.receivers.IntentAlarmReceiver;
import zzz.master.general.ui.memory.MemoryFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuración de la barra de herramientas y el DrawerLayout
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show();

            IntentAlarmReceiver intentAlarmReceiver = new IntentAlarmReceiver();
            intentAlarmReceiver.onReceive(getApplicationContext(), new Intent());
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configuración del AppBarConfiguration
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_memory, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        // Obtén el NavController desde el NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        // Configura la barra de acción y el NavigationView con el NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Manejar el Intent para abrir MemoryFragment
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("open_fragment")) {
            String fragmentName = intent.getStringExtra("open_fragment");
            if ("MemoryFragment".equals(fragmentName)) {
                int selectedTab = intent.getIntExtra("tab_position", 3);

                // Navegar al fragmento MemoryFragment usando el NavController y el Bundle para pasar el tab seleccionado
                Bundle args = new Bundle();
                args.putInt("selectedTab", selectedTab);
                navController.navigate(R.id.nav_memory, args);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}