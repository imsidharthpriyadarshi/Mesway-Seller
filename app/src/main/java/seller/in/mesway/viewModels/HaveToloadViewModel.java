package seller.in.mesway.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HaveToloadViewModel extends ViewModel {
    private MutableLiveData<Boolean> have_to_load =new MutableLiveData<>();

    public LiveData<Boolean> isHave_to_load() {
        return have_to_load;
    }

    public void setHave_to_load(boolean have_load) {
        have_to_load.postValue(have_load);

    }
}
