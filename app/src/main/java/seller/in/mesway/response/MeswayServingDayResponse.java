package seller.in.mesway.response;

public class MeswayServingDayResponse {
    private boolean breakfast;
    private boolean dinner;
    private boolean lunch;
    private int meals_number;
    private String serving_meals_id;

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isDinner() {
        return dinner;
    }

    public void setDinner(boolean dinner) {
        this.dinner = dinner;
    }

    public boolean isLunch() {
        return lunch;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public int getMeals_number() {
        return meals_number;
    }

    public void setMeals_number(int meals_number) {
        this.meals_number = meals_number;
    }

    public String getServing_meals_id() {
        return serving_meals_id;
    }

    public void setServing_meals_id(String serving_meals_id) {
        this.serving_meals_id = serving_meals_id;
    }

    @Override
    public String toString() {
        return "MeswayServingDayResponse{" +
                "breakfast=" + breakfast +
                ", dinner=" + dinner +
                ", lunch=" + lunch +
                ", meals_number=" + meals_number +
                ", serving_meals_id='" + serving_meals_id + '\'' +
                '}';
    }
}
