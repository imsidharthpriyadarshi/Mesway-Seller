package seller.in.mesway.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubsInfoCountViewModel extends ViewModel {
    private MutableLiveData<String> subs_info=new MutableLiveData<>();
    private MutableLiveData<String> subs_count= new MutableLiveData<>();


    private MutableLiveData<Boolean> is_one_req= new MutableLiveData<>();
    private MutableLiveData<Boolean> is_two_req= new MutableLiveData<>();
    private MutableLiveData<Boolean> is_three_req= new MutableLiveData<>();


    public LiveData<Boolean> getIs_one_req() {
        return is_one_req;
    }

    public LiveData<Boolean> getIs_two_req() {
        return is_two_req;
    }

    public LiveData<Boolean> getIs_three_req() {
        return is_three_req;
    }

    public void setIs_one_req(boolean is_one) {
        is_one_req.postValue(is_one);
    }

    public void setIs_two_req(boolean is_two) {
      is_two_req.postValue(is_two);
    }

    public void setIs_three_req(boolean is_three) {
        is_three_req.postValue(is_three);

    }

    public LiveData<String> getSubs_info() {
        return subs_info;
    }

    public LiveData<String> getSubs_count() {
        return subs_count;
    }

    public void setSubs_info(String info){
        subs_info.postValue(info);

    }

    public void setSubs_count(String count){
        subs_count.postValue(count);


    }
}
