package zzz.master.general.ui.memory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

public class MemoryViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isGifVisible = new MutableLiveData<>();
    private final MutableLiveData<String> t1RandomNumber = new MutableLiveData<>();

    private final MutableLiveData<List<Boolean>> t1UIState = new MutableLiveData<>();

    public LiveData<Boolean> getGifVisibility() { return isGifVisible; }
    public LiveData<String> getT1RandomNumber() { return t1RandomNumber; }
    public LiveData<List<Boolean>> getT1UIState() { return t1UIState; }

    public void setGifVisibility(Boolean visible) { isGifVisible.setValue(visible); }
    public void setT1RandomNumber(String num) { t1RandomNumber.setValue(num); }
    public void setT1UIState(String stateName) {
        t1UIState.setValue(getT1UIStateDefinition(stateName));
    }

    private List<Boolean> getT1UIStateDefinition(String stateName){
        Map<String, List<Boolean>> states = Map.of(
                "generated", List.of(false, true, true, false),
                "hidden",    List.of(false, false, false, false),
                "waited",    List.of(true, false, false, true),
                "zeroday", List.of(true, false, true, false)
        );                       // G, I, N, E

        return states.getOrDefault(stateName, states.get("zeroday"));
    }
}