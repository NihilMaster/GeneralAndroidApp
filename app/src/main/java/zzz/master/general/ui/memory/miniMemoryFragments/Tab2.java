package zzz.master.general.ui.memory.miniMemoryFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import zzz.master.general.R;

public class Tab2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory_tab2, container, false);
    }

    /**
     * // Input focus
     * new Handler(Looper.getMainLooper()).postDelayed(inputNumber::requestFocus, 100);
     * new Handler(Looper.getMainLooper()).postDelayed(() -> {
     *      InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
     *      if (imm != null) {
     *          imm.showSoftInput(inputNumber, InputMethodManager.SHOW_IMPLICIT);
     *       }
     * }, 100);
     */
}
