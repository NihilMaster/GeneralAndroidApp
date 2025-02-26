package zzz.master.general.ui.memory.miniMemoryFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Random;

import zzz.master.general.R;
import zzz.master.general.utils.PrefsUtil;

public class Fragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory_tab1, container, false);

        PrefsUtil prefsUtil = new PrefsUtil(requireContext());

        TextView numberTextView = view.findViewById(R.id.random_number);
        TextView maxScoreTextView = view.findViewById(R.id.max_score);
        EditText inputNumber = view.findViewById(R.id.input_number);
        Button generate_checkButton = view.findViewById(R.id.btn_gen_check);
        Button startButton = view.findViewById(R.id.btn_start);
        Button tempButton = view.findViewById(R.id.btn_temp);

        if(prefsUtil.getBoolean("MF_clean",true)){ //VACÍO
            Log.println(Log.ASSERT,"DEBUG","VACÍO");
            prefsUtil.setBoolean("MF_clean",false);
            prefsUtil.setString("MF_random_grade","5");
        }else if(prefsUtil.getBoolean("MF_is_time",false)){ //BLOQUEADO
            Log.println(Log.ASSERT,"DEBUG","BLOQUEADO");
        }else{ // RESPONDER
            Log.println(Log.ASSERT,"DEBUG","RESPONDER");
        }

        generate_checkButton.setOnClickListener(view1 -> {
            if(prefsUtil.getBoolean("MF_is_time",true)){
                if(inputNumber.getText().toString().equals(prefsUtil.getString("MF_random_number","00000"))){
                    Toast.makeText(requireContext(), "CORRECTO", Toast.LENGTH_SHORT).show();
                    int actual = Integer.parseInt(prefsUtil.getString("MF_max_score","0"));
                    int grade = Integer.parseInt(prefsUtil.getString("MF_random_grade","5"));
                    prefsUtil.setString("MF_max_score", grade>actual ? String.valueOf(grade) : String.valueOf(actual));
                    maxScoreTextView.setText(getString(R.string.MF_tab1_tv_max_score,
                            prefsUtil.getString("MF_max_score", "0")));
                    prefsUtil.setString("MF_random_grade",manage_range(prefsUtil,true).toString());

                    //maxScoreTextView.setText("MAX: "+prefsUtil.getString("MF_max_score","0"));
                    /*String response =  manage_range(prefsUtil,true).toString();
                    Toast.makeText(requireContext(), "CORRECTO: "+response
                            + prefsUtil.getString("MF_strike","")
                            , Toast.LENGTH_LONG).show();
                    prefsUtil.setString("MF_random_grade",response);*/

                }
                else{
                    Toast.makeText(requireContext(), "INCORRECTO", Toast.LENGTH_SHORT).show();
                    prefsUtil.setString("MF_random_grade",manage_range(prefsUtil,false).toString());

                    /*String response =  manage_range(prefsUtil,false).toString();
                    Toast.makeText(requireContext(), "INCORRECTO: "+response
                            + prefsUtil.getString("MF_strike","")
                            , Toast.LENGTH_LONG).show();
                    prefsUtil.setString("MF_random_grade",response);*/
                }
                prefsUtil.setBoolean("MF_is_time",false);
                numberTextView.setText(prefsUtil.getString("MF_random_number","00000"));
                inputNumber.setVisibility(View.INVISIBLE);
                inputNumber.setEnabled(false);
                inputNumber.setText("");
                generate_checkButton.setText(R.string.MF_tab1_btn_generate);
                startButton.setEnabled(false);
                tempButton.setEnabled(false);
            }else{
                prefsUtil.setString("MF_random_number",random_digit(Integer.parseInt(prefsUtil.getString("MF_random_grade","5"))));
                numberTextView.setText(prefsUtil.getString("MF_random_number","00000"));
                generate_checkButton.setEnabled(false);
                startButton.setEnabled(true);
                tempButton.setEnabled(true);
            }
        });

        startButton.setOnClickListener(view2 -> {
            numberTextView.setText("*".repeat(Integer.parseInt(prefsUtil.getString("MF_random_grade","5"))));
            generate_checkButton.setEnabled(false);
            startButton.setEnabled(false);
            tempButton.setEnabled(true);
        });

        tempButton.setOnClickListener(view3 -> {
            prefsUtil.setBoolean("MF_is_time",true);
            inputNumber.setVisibility(View.VISIBLE);
            inputNumber.setEnabled(true);
            generate_checkButton.setText(R.string.MF_tab1_btn_check);
            generate_checkButton.setEnabled(true);
            tempButton.setEnabled(false);
        });

        return view;//inflater.inflate(R.layout.fragment_memory_tab1, container, false);
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
}