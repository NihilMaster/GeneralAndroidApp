package zzz.master.general.ui.memory.miniMemoryFragments;

import android.content.Context;
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

import java.util.Random;

import zzz.master.general.R;
import zzz.master.general.ui.memory.MemoryViewModel;
import zzz.master.general.utils.PrefsUtil;

public class Tab1 extends Fragment {

    /**
     * SharedPreferences
     * _________________
     * MF_random_number - Número aleatorio a recordar (String)
     * MF_random_grade  - Número de cifras del número aleatorio (String)
     * MF_is_time       - Bandera para determinar si el tiempo ha concluido (Boolean)
     * MF_strike        - Número de intentos fallidos (String)
     * MF_max_score     - Máximo puntaje (String)
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
        Button tempButton = view.findViewById(R.id.btn_temp);

        // Actualización del máximo
        maxScoreTextView.setText(getString(R.string.MF_tab1_tv_max_score,
                prefsUtil.getString("MF_max_score", "0")));

        // Botón de generar-verificar
        generate_checkButton.setOnClickListener(view1 -> {
            if(prefsUtil.getBoolean("MF_is_time",true)){ // VERIFICAR
                if(inputNumber.getText().toString().equals(prefsUtil.getString("MF_random_number","00000"))){ // CORRECTO
                    Toast.makeText(requireContext(), R.string.correct, Toast.LENGTH_SHORT).show();

                    // Actualización del máximo
                    int maxScore = Integer.parseInt(prefsUtil.getString("MF_max_score","0"));
                    int currentGrade = Integer.parseInt(prefsUtil.getString("MF_random_grade","5"));
                    prefsUtil.setString("MF_max_score", currentGrade>maxScore ? String.valueOf(currentGrade) : String.valueOf(maxScore));
                    maxScoreTextView.setText(getString(R.string.MF_tab1_tv_max_score,
                            prefsUtil.getString("MF_max_score", "0")));
                    prefsUtil.setString("MF_random_grade",manage_range(prefsUtil,true).toString());

                    // Aparición de la animación
                    memoryViewModel.setItemVisibility(true);
                    makeDisable(generate_checkButton);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        memoryViewModel.setItemVisibility(false);
                        makeEnable(generate_checkButton);
                    }, 800);
                }
                else{ // INCORRECTO
                    Toast.makeText(requireContext(), R.string.incorrect, Toast.LENGTH_SHORT).show();
                    prefsUtil.setString("MF_random_grade",manage_range(prefsUtil,false).toString());
                }

                // Resetear UI
                prefsUtil.setBoolean("MF_is_time",false);
                numberTextView.setText(prefsUtil.getString("MF_random_number","00000"));
                inputNumber.setVisibility(View.GONE);
                inputNumber.setText("");
                generate_checkButton.setText(R.string.MF_tab1_btn_generate);
                makeDisable(inputNumber, startButton, tempButton);
            }else{ // GENERAR
                prefsUtil.setString("MF_random_number",random_digit(Integer.parseInt(prefsUtil.getString("MF_random_grade","5"))));
                numberTextView.setText(prefsUtil.getString("MF_random_number","00000"));
                makeDisable(generate_checkButton);
                makeEnable(startButton, tempButton);
            }
        });

        // Botón de iniciar
        startButton.setOnClickListener(view2 -> {
            numberTextView.setText("*".repeat(Integer.parseInt(prefsUtil.getString("MF_random_grade","5"))));
            makeEnable(tempButton);
            makeDisable(generate_checkButton, startButton);
        });

        // Botón temporal que simula el paso del tiempo
        tempButton.setOnClickListener(view3 -> {
            prefsUtil.setBoolean("MF_is_time",true);
            inputNumber.setVisibility(View.VISIBLE);
            makeEnable(inputNumber, generate_checkButton);
            makeDisable(tempButton);
            generate_checkButton.setText(R.string.MF_tab1_btn_check);
            new Handler(Looper.getMainLooper()).postDelayed(inputNumber::requestFocus, 100);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(inputNumber, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);


        });

        return view; // inflater.inflate(R.layout.fragment_memory_tab1, container, false);
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
        int grade = Integer.parseInt(prefsUtil.getString("MF_random_grade","5"));

        if(moreorless){
            prefsUtil.deleteKey("MF_strike");
            return grade+1;
        }else{
            int dif = grade - 5 - 1;
            int strike = Integer.parseInt(prefsUtil.getString("MF_strike","0"));
            if(strike==0 && prefsUtil.getBoolean("MF_is_time",false)){
                if(dif==0){
                    prefsUtil.deleteKey("MF_strike");
                    return grade-1;
                }
                prefsUtil.setString("MF_strike",String.valueOf(dif));
                return grade;
            }else{
                prefsUtil.setString("MF_strike",String.valueOf(strike-1));
                if((strike-1)==0) prefsUtil.deleteKey("MF_strike");
                return prefsUtil.getString("MF_strike","").isEmpty() ? grade-1 : grade;
            }
        }
    }

    private void makeEnable(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(true);
                view.setAlpha(1.0f);
            }
        }
    }

    private void makeDisable(View... views){
        for (View view : views) {
            if (view != null) {
                view.setEnabled(false);
                view.setAlpha(0.5f);
            }
        }
    }



}