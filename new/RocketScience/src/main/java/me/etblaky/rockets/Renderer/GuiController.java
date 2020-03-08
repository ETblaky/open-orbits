package me.etblaky.rockets.Renderer;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import me.etblaky.rockets.Main;
import me.etblaky.rockets.Utils.Date;

import java.util.Calendar;

public class GuiController extends BaseAppState implements ScreenController {

    private DropDown<Integer> year;
    private DropDown<Integer> month;
    private DropDown<Integer> day;
    private DropDown<Integer> hour;
    private DropDown<Integer> min;
    private DropDown<Integer> sec;

    public Slider vel;
    private Button btn;

    public void bind(Nifty nifty, Screen screen) {

        year = screen.findNiftyControl("year", DropDown.class);
        month = screen.findNiftyControl("month", DropDown.class);
        day = screen.findNiftyControl("day", DropDown.class);
        hour = screen.findNiftyControl("hour", DropDown.class);
        min = screen.findNiftyControl("min", DropDown.class);
        sec = screen.findNiftyControl("sec", DropDown.class);
        vel = screen.findNiftyControl("vel", Slider.class);
        btn = screen.findNiftyControl("playBtn", Button.class);

        for (int i = 0; i < 100; i++) {
            year.addItem(i + 1950);
        }
        for (int i = 0; i < 12; i++) {
            month.addItem(i + 1);
        }
        for (int i = 0; i < 31; i++) {
            day.addItem(i + 1);
        }
        for (int i = 0; i < 24; i++) {
            hour.addItem(i);
        }
        for (int i = 0; i < 60; i++) {
            min.addItem(i);
            sec.addItem(i);
        }

        updateGuiDate();
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    protected void initialize(Application application) {
    }

    protected void cleanup(Application application) {
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public void updateBtnTxt(){
        if (Main.viewer.shouldUpdateDate) {
            btn.setText("Stop");
        } else {
            btn.setText("Play");
        }
    }

    public void toggle() {
        Main.viewer.shouldUpdateDate = !Main.viewer.shouldUpdateDate;
        updateBtnTxt();
    }

    public void now() {
        Main.viewer.date = new Date();
        Main.viewer.velocity = vel.getValue();
        Main.viewer.offsetTime = 0;

        updateGuiDate();
    }

    public void updateViewerDate() {
        Main.viewer.date.year = year.getSelection();
        Main.viewer.date.month = month.getSelection();
        Main.viewer.date.day = day.getSelection();
        Main.viewer.date.hour = hour.getSelection();
        Main.viewer.date.minute = min.getSelection();
        Main.viewer.date.second = sec.getSelection();

        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.MONTH, month.getSelection() - 1);
        temp.set(Calendar.DAY_OF_MONTH, day.getSelection());
        Main.viewer.date.day_of_year = temp.get(Calendar.DAY_OF_YEAR);

        Main.viewer.velocity = vel.getValue();
        Main.viewer.offsetTime = 0;
    }

    public void updateGuiDate() {
        year.selectItem(Main.viewer.date.year);
        month.selectItem(Main.viewer.date.month);
        day.selectItem(Main.viewer.date.day);
        hour.selectItem(Main.viewer.date.hour);
        min.selectItem(Main.viewer.date.minute);
        sec.selectItem(Main.viewer.date.second);
    }

}
