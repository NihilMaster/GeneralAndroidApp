package zzz.master.general.ui.memory.miniMemoryFragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import zzz.master.general.R;
import zzz.master.general.receivers.IntentAlarmReceiver;
import zzz.master.general.ui.memory.MemoryViewModel;
import zzz.master.general.utils.PrefsUtil;

public class Tab1 extends Fragment {

    /**
     * SharedPreferences
     * _________________
     * MF_t1_random_number - Número aleatorio a recordar (String)
     * MF_t1_random_grade  - Número de cifras del número aleatorio (String)
     * MF_t1_state         - Estado actual (String)
     * MF_t1_strike        - Número de intentos fallidos (String)
     * MF_t1_max_score     - Máximo puntaje (String)
     */

    private MemoryViewModel memoryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory_tab1, container, false);

        memoryViewModel = new ViewModelProvider(requireActivity()).get(MemoryViewModel.class);

        PrefsUtil prefsUtil = new PrefsUtil(requireContext());

        // View items
        TextView numberTextView = view.findViewById(R.id.random_number);
        TextView maxScoreTextView = view.findViewById(R.id.max_score);
        EditText inputNumber = view.findViewById(R.id.input_number);
        Button generate_checkButton = view.findViewById(R.id.btn_gen_check);
        Button startButton = view.findViewById(R.id.btn_start);

        // Re-Display
        memoryViewModel.setT1RandomNumber(prefsUtil.getString("MF_t1_random_number","00000"));
        memoryViewModel.getT1RandomNumber().observe(getViewLifecycleOwner(), numberTextView::setText);
        memoryViewModel.setT1UIState(prefsUtil.getString("MF_t1_state","zeroday"));
        if(prefsUtil.getString("MF_t1_state","zeroday").equals("waited")){
            memoryViewModel.setT1UIState("waited");

            // Input focus
            new Handler(Looper.getMainLooper()).postDelayed(inputNumber::requestFocus, 100);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(inputNumber, InputMethodManager.SHOW_IMPLICIT);
                    generate_checkButton.setText(R.string.MF_tab1_btn_check);
                }
            }, 100);
        }

        // Manejo de estados
        memoryViewModel.getT1UIState().observe(getViewLifecycleOwner(), state -> {
            if (state != null && state.size() == 4) {
                generate_checkButton.setEnabled(state.get(0)); // G
                generate_checkButton.setAlpha(state.get(0) ? 1.0f : 0.5f);
                generate_checkButton.setText(state.get(0) ? R.string.MF_tab1_btn_generate : R.string.MF_tab1_btn_check);

                startButton.setEnabled(state.get(1)); // I
                startButton.setAlpha(state.get(1) ? 1.0f : 0.5f);

                if(!state.get(2)) { // N
                    numberTextView.setText("*".repeat(Integer.parseInt(prefsUtil.getString("MF_t1_random_grade","5"))));
                }else{
                    numberTextView.setText(prefsUtil.getString("MF_t1_random_number","00000"));
                }

                inputNumber.setVisibility(state.get(3) ? View.VISIBLE : View.GONE); // E
                inputNumber.setEnabled(state.get(3));
                inputNumber.setText("");
            }
        });

        // Actualización del máximo
        maxScoreTextView.setText(getString(R.string.MF_tab1_tv_max_score,
                prefsUtil.getString("MF_t1_max_score", "0")));

        // Botón de generar-verificar
        generate_checkButton.setOnClickListener(view1 -> {
            if(prefsUtil.getString("MF_t1_state","zeroday").equals("waited")){ // VERIFICAR"
                if(inputNumber.getText().toString().equals(prefsUtil.getString("MF_t1_random_number","00000"))){ // CORRECTO
                    Toast.makeText(requireContext(), R.string.correct, Toast.LENGTH_SHORT).show();

                    // Actualización del máximo
                    int maxScore = Integer.parseInt(prefsUtil.getString("MF_t1_max_score","0"));
                    int currentGrade = Integer.parseInt(prefsUtil.getString("MF_t1_random_grade","5"));
                    prefsUtil.setString("MF_t1_max_score", currentGrade>maxScore ? String.valueOf(currentGrade) : String.valueOf(maxScore));
                    maxScoreTextView.setText(
                            getString(R.string.MF_tab1_tv_max_score,
                                    prefsUtil.getString("MF_t1_max_score", "0")));
                    prefsUtil.setString("MF_t1_random_grade",manage_range(prefsUtil,true).toString());

                    // Aparición de la animación
                    memoryViewModel.setGifVisibility(true);
                    generate_checkButton.setEnabled(false);
                    new Handler(Looper.getMainLooper()).postDelayed(() ->
                            memoryViewModel.setGifVisibility(false), 800);
                }
                else{ // INCORRECTO
                    Toast.makeText(requireContext(), R.string.incorrect, Toast.LENGTH_SHORT).show();
                    prefsUtil.setString("MF_t1_random_grade",manage_range(prefsUtil,false).toString());
                }

                // Resetear UI
                memoryViewModel.getT1RandomNumber().observe(getViewLifecycleOwner(), numberTextView::setText);
                memoryViewModel.setT1UIState("zeroday");
                prefsUtil.setString("MF_t1_state","zeroday");
            }else{ // GENERAR
                String generatedRandNum = random_digit(Integer.parseInt(prefsUtil.getString("MF_t1_random_grade","5")));
                memoryViewModel.setT1UIState("generated");
                memoryViewModel.setT1RandomNumber(generatedRandNum);
                prefsUtil.setString("MF_t1_state","generated");
                prefsUtil.setString("MF_t1_random_number",generatedRandNum);
            }
        });

        // Botón de iniciar
        startButton.setOnClickListener(view2 -> {
            memoryViewModel.setT1UIState("hidden");
            prefsUtil.setString("MF_t1_state","hidden");
            scheduleRepeatingAlarm(requireContext());
        });

        return view;
    }

    private String random_digit (int n){
        StringBuilder str = new StringBuilder();
        for (int i=0;i<n;i++){
            str.append(new Random().nextInt(10));
        }
        System.out.println("STR: "+str);
        return str.toString();
    }

    private Integer manage_range (PrefsUtil prefsUtil, boolean moreorless){
        int grade = Integer.parseInt(prefsUtil.getString("MF_t1_random_grade","5"));

        if(moreorless){
            prefsUtil.deleteKey("MF_t1_strike");
            return grade+1;
        }else{
            if(grade == 5){return grade;}
            if (grade > 7) {
                int attempts = grade - 6;
                int strikes = Integer.parseInt(prefsUtil.getString("MF_t1_strike", "0"));
                if (strikes < attempts) {
                    prefsUtil.setString("MF_t1_strike", String.valueOf(strikes + 1));
                    return grade;
                }
                prefsUtil.deleteKey("MF_t1_strike");
            }
            return grade-1;
        }
    }

    public void scheduleRepeatingAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, IntentAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Establecer la hora específica (8:00 PM)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Si la hora ya pasó hoy, programar para mañana
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Programar una alarma inexacta que se repita cada 24 horas
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,   // Usa tiempo real del sistema (despierta si es necesario)
                calendar.getTimeInMillis(), // Primera ejecución
                AlarmManager.INTERVAL_DAY, // Se repite cada día
                pendingIntent
        );
    }
}