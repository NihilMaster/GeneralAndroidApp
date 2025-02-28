package zzz.master.general.ui.memory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MemoryViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isItemVisible = new MutableLiveData<>();

    public LiveData<Boolean> getItemVisibility() {
        return isItemVisible;
    }

    public void setItemVisibility(Boolean visible) {
        isItemVisible.setValue(visible);
    }

}