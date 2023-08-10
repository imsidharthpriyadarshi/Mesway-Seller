
package seller.in.mesway.response.todayMeal;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayMeals {

    @SerializedName("today_meal")
    @Expose
    private List<TodayMeal> todayMeal;

    private boolean breakfast,lunch,dinner;

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isLunch() {
        return lunch;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public boolean isDinner() {
        return dinner;
    }

    public void setDinner(boolean dinner) {
        this.dinner = dinner;
    }

    public List<TodayMeal> getTodayMeal() {
        return todayMeal;
    }

    public void setTodayMeal(List<TodayMeal> todayMeal) {
        this.todayMeal = todayMeal;
    }

    @Override
    public String toString() {
        return "TodayMeals{" +
                "todayMeal=" + todayMeal +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                '}';
    }
}
