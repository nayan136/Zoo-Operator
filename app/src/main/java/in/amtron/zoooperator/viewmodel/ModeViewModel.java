package in.amtron.zoooperator.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ModeViewModel extends ViewModel {

    private MutableLiveData<Integer> currentMode;
    private MutableLiveData<Integer> chunkNo;

    public MutableLiveData<Integer> getCurrentMode() {
        if (currentMode == null) {
            currentMode = new MutableLiveData<Integer>();
        }
        return currentMode;
    }

    public MutableLiveData<Integer> getChunkNo() {
        if (chunkNo == null) {
            chunkNo = new MutableLiveData<Integer>();
        }
        return chunkNo;
    }

}
