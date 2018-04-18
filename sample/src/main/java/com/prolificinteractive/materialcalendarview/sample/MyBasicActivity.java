package com.prolificinteractive.materialcalendarview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CustomDay;
import com.prolificinteractive.materialcalendarview.CustomMonth;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Shows off the most basic usage
 */
public class MyBasicActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    @BindView(R.id.calendarView)
    MaterialCalendarView widget;

    @BindView(R.id.text_view_title)
    TextView text_view_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_basic);
        ButterKnife.bind(this);

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.setTopbarVisible(false);

//        widget.state().edit()
//                .setMinimumDate(CalendarDay.from(2018, 2, 1))
//                .commit();
//
//        widget.state().edit()
//                .setMaximumDate(CalendarDay.from(2018, 3, 30))
//                .commit();

        List<CustomMonth> customMonthList = new ArrayList<>();
        CalendarDay calendarDay = CalendarDay.from(2018, 2, 1);
        List<CustomDay> customDayList = new ArrayList<>();
        customDayList.add(new CustomDay(5, Arrays.asList("abc", "qwe"),  8000));
        customDayList.add(new CustomDay(6, new ArrayList<String>(),  -14000));
        customDayList.add(new CustomDay(19, Arrays.asList("abc"),  null));
        customDayList.add(new CustomDay(20, Arrays.asList("abc"),  null));
        customDayList.add(new CustomDay(21, Arrays.asList("abc"),  null));
//        customDayList.add(new CustomDay(10, null, null));
        customDayList.add(new CustomDay(22, null, null, false));
        customDayList.add(new CustomDay(23, null, null, false));
        customDayList.add(new CustomDay(24, null, null, false));
        customDayList.add(new CustomDay(25, null, null, false));
        customDayList.add(new CustomDay(26, null, null, false));
        customDayList.add(new CustomDay(27, null, null, false));
        customDayList.add(new CustomDay(28, null, null, false));
        customDayList.add(new CustomDay(29, null, null, false));
        customDayList.add(new CustomDay(30, null, null, false));
        customDayList.add(new CustomDay(31, null, null, false));
        customDayList.add(new CustomDay(10, null, null, false));
        customDayList.add(new CustomDay(11, null, null, false));
        customDayList.add(new CustomDay(12, null, null, false));
        customDayList.add(new CustomDay(13, null, null, false));
        customDayList.add(new CustomDay(14, null, null, false));
        customDayList.add(new CustomDay(15, null, null, false));
        customDayList.add(new CustomDay(16, null, null, false));
        customDayList.add(new CustomDay(17, null, null, false));
        customDayList.add(new CustomDay(18, null, null, false));
//        customDayList.add(new CustomDay(20, null, null, false));
//        customDayList.add(new CustomDay(27, null, null));
        CustomMonth customMonth = new CustomMonth(calendarDay, customDayList);
        customMonthList.add(customMonth);
        widget.setCustomMonth(customMonthList);
        widget.setSelectionColor(Color.YELLOW);


        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .commit();

//        widget.setWeekDayTextAppearance();
//        widget.state().edit()
//                .setMinimumDate(CalendarDay.from(2017, 6, 1))
//                .commit();
//
//        widget.state().edit()
//                .setMinimumDate(CalendarDay.from(2018, 6, 1))
//                .commit();

        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
//        widget.setPadding(0,0,0,0);
        //Setup initial text
//        text_view_title.setText(getSelectedDatesString());
        text_view_title.setText(FORMATTER.format(widget.getCurrentDate().getDate()));
        widget.setTileSize(LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
//        text_view_title.setText(getSelectedDatesString());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
//        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
        text_view_title.setText(FORMATTER.format(widget.getCurrentDate().getDate()));
    }

    @OnClick(R.id.btn_next)
    void onNext() {
        widget.goToNext();
    }

    @OnClick(R.id.btn_pre)
    void onPre() {
        widget.goToPrevious();
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
}
