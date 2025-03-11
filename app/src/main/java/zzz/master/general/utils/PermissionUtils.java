package zzz.master.general.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtils {

    private final Fragment fragment;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Context context;

    public PermissionUtils(@NonNull Fragment fragment) {
        this.fragment = fragment;
        setupPermissionLauncher();
    }

    // Configurar el lanzador de permisos
    private void setupPermissionLauncher() {
        requestPermissionLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (shouldShowRequestPermissionRationale()) {
                                showPermissionRationaleDialog(); // Mostrar diálogo explicativo
                            } else {
                                showSettingsDialog(); // Redirigir a la configuración
                            }
                        }
                    }
                }
        );
    }

    // Solicitar un permiso específico
    public void requestPermission(String permission, Context requireContext) {
        context = requireContext;
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), permission)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permission);
        }
    }

    // Verificar si se debe mostrar el diálogo explicativo
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean shouldShowRequestPermissionRationale() {
        return fragment.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS);
    }

    // Mostrar un diálogo explicativo si el permiso es denegado
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void showPermissionRationaleDialog() {
        new AlertDialog.Builder(fragment.requireContext())
                .setTitle("Permiso requerido")
                .setMessage("Este permiso es necesario para que la aplicación funcione correctamente.")
                .setPositiveButton("Intentar de nuevo", (dialog, which) -> requestPermission(Manifest.permission.POST_NOTIFICATIONS, context))
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                    if (context instanceof ComponentActivity) {
                        ((ComponentActivity) context).getOnBackPressedDispatcher().onBackPressed();
                    }
                })
                .show();
    }

    // Mostrar un diálogo para redirigir al usuario a la configuración
    private void showSettingsDialog() {
        new AlertDialog.Builder(fragment.requireContext())
                .setTitle("Permiso requerido")
                .setMessage("El permiso fue denegado permanentemente. Puedes habilitarlo manualmente en la configuración de la aplicación.")
                .setPositiveButton("Ir a configuración", (dialog, which) -> openAppSettings())
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                    if (context instanceof ComponentActivity) {
                        ((ComponentActivity) context).getOnBackPressedDispatcher().onBackPressed();
                    }
                })
                .show();
    }

    // Abrir la configuración de la aplicación
    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", fragment.requireContext().getPackageName(), null);
        intent.setData(uri);
        fragment.startActivity(intent);
    }
}
