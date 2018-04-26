package com.prolificinteractive.materialcalendarview;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView.ShowOtherDates;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SHOW_DEFAULTS;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOtherMonths;
import static java.util.Calendar.DATE;

abstract class CalendarPagerView extends ViewGroup implements View.OnClickListener {

    protected static final int DEFAULT_DAYS_IN_WEEK = 7;
    protected int DEFAULT_MAX_WEEKS = 6;
    protected static final int DAY_NAMES_ROW = 1;
    private static final Calendar tempWorkingCalendar = CalendarUtils.getInstance();
    private final ArrayList<WeekDayView> weekDayViews = new ArrayList<>();
    private final ArrayList<DecoratorResult> decoratorResults = new ArrayList<>();
    @ShowOtherDates
    protected int showOtherDates = SHOW_DEFAULTS;
    private MaterialCalendarView mcv;
    private CalendarDay firstViewDay;
    private CalendarDay minDate = null;
    private CalendarDay maxDate = null;
    private int firstDayOfWeek;

    private final Collection<DayView> dayViews = new ArrayList<>();

    protected CustomMonth mCustomMonth;

    public CalendarPagerView(@NonNull MaterialCalendarView view,
                             CalendarDay firstViewDay,
                             int firstDayOfWeek) {
        super(view.getContext());
        Log.e("cuonghc", String.valueOf(firstViewDay.getDay()) + " / " + String.valueOf(firstViewDay.getMonth()));
        this.mcv = view;
        this.firstViewDay = firstViewDay;
        this.firstDayOfWeek = firstDayOfWeek;

        mCustomMonth = findCustomMonth();

        setClipChildren(false);
        setClipToPadding(false);
        buildDefaultMaxWeeks(resetAndGetWorkingCalendar());
        buildWeekDays(resetAndGetWorkingCalendar());
        buildDayViews(dayViews, resetAndGetWorkingCalendar());
    }

    private void buildDefaultMaxWeeks(Calendar calendar) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(calendar.getTime());
        tempCalendar.add(Calendar.DATE, DEFAULT_DAYS_IN_WEEK * 5);
        DEFAULT_MAX_WEEKS = tempCalendar.get(Calendar.MONTH) == getFirstViewDay().getMonth() ? 6 : 5;
    }

    private CustomMonth findCustomMonth() {
        List<CustomMonth> customMonthList = mcv.getCustomMonth();
        if (customMonthList != null) {
            for(CustomMonth customMonth : customMonthList) {
                if (getFirstViewDay().inMonth(customMonth.getCalendarDay())) {
                    return customMonth;
                }
            }
        }
        return null;
    }

    private void buildWeekDays(Calendar calendar) {
        for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
            WeekDayView weekDayView = new WeekDayView(getContext(), CalendarUtils.getDayOfWeek(calendar));
            weekDayViews.add(weekDayView);
            addView(weekDayView);
            calendar.add(DATE, 1);
        }
    }

    protected void addViewLines() {
        View view = new View(getContext());
        view.setTag(-1);
        view.setBackgroundColor(getContext().getResources().getColor(R.color.calendar_line_week));
        addView(view, new LayoutParams());
    }

    protected void addDayView(Collection<DayView> dayViews, Calendar calendar) {
        CalendarDay day = CalendarDay.from(calendar);
        DayView dayView = new DayView(getContext(), day);
        dayView.setOnClickListener(this);
        dayViews.add(dayView);
        addView(dayView, new LayoutParams());

        calendar.add(DATE, 1);

        if (mCustomMonth != null && mCustomMonth.getCustomDays() != null &&
                mCustomMonth.getCalendarDay().getMonth() == day.getMonth()) {
            CustomDay customDay = findDayCustom(mCustomMonth.getCustomDays(), day);
            dayView.setCustomDay(customDay);
        }
    }

    private CustomDay findDayCustom(List<CustomDay> customDayList, CalendarDay day) {
        for (CustomDay customDay : customDayList) {
            if (day.getDay() == customDay.getDay()) {
                return customDay;
            }
        }
        return null;
    }

    protected Calendar resetAndGetWorkingCalendar() {
        getFirstViewDay().copyTo(tempWorkingCalendar);
        //noinspection ResourceType
        tempWorkingCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        int dow = CalendarUtils.getDayOfWeek(tempWorkingCalendar);
        int delta = getFirstDayOfWeek() - dow;
        //If the delta is positive, we want to remove a week
        boolean removeRow = showOtherMonths(showOtherDates) ? delta >= 0 : delta > 0;
        if (removeRow) {
            delta -= DEFAULT_DAYS_IN_WEEK;
        }
        tempWorkingCalendar.add(DATE, delta);
        return tempWorkingCalendar;
    }

    protected int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    protected abstract void buildDayViews(Collection<DayView> dayViews, Calendar calendar);

    protected abstract boolean isDayEnabled(CalendarDay day);

    void setDayViewDecorators(List<DecoratorResult> results) {
        this.decoratorResults.clear();
        if (results != null) {
            this.decoratorResults.addAll(results);
        }
        invalidateDecorators();
    }

    public void setWeekDayTextAppearance(int taId) {
        for (WeekDayView weekDayView : weekDayViews) {
            weekDayView.setTextAppearance(getContext(), taId);
        }
    }

    public void setDateTextAppearance(int taId) {
        for (DayView dayView : dayViews) {
            dayView.setTextAppearance(getContext(), taId);
        }
    }

    public void setShowOtherDates(@ShowOtherDates int showFlags) {
        this.showOtherDates = showFlags;
        updateUi();
    }

    public void setSelectionEnabled(boolean selectionEnabled) {
        for (DayView dayView : dayViews) {
            dayView.setOnClickListener(selectionEnabled ? this : null);
            dayView.setClickable(selectionEnabled);
        }
    }

    public void setSelectionColor(int color) {
        for (DayView dayView : dayViews) {
            dayView.setSelectionColor(color);
        }
    }

    public void setWeekDayFormatter(WeekDayFormatter formatter) {
        for (WeekDayView dayView : weekDayViews) {
            dayView.setWeekDayFormatter(formatter);
        }
    }

    public void setDayFormatter(DayFormatter formatter) {
        for (DayView dayView : dayViews) {
            dayView.setDayFormatter(formatter);
        }
    }

    public void setMinimumDate(CalendarDay minDate) {
        this.minDate = minDate;
        updateUi();
    }

    public void setMaximumDate(CalendarDay maxDate) {
        this.maxDate = maxDate;
        updateUi();
    }

    public void setSelectedDates(Collection<CalendarDay> dates) {
        for (DayView dayView : dayViews) {
            CalendarDay day = dayView.getDate();
            dayView.setChecked(dates != null && dates.contains(day));
        }
        postInvalidate();
    }

    protected void updateUi() {
        for (DayView dayView : dayViews) {
            CalendarDay day = dayView.getDate();
            dayView.setupSelection(
                    showOtherDates, day.isInRange(minDate, maxDate), isDayEnabled(day));
        }
        postInvalidate();
    }

    protected void invalidateDecorators() {
        final DayViewFacade facadeAccumulator = new DayViewFacade();
        for (DayView dayView : dayViews) {
            facadeAccumulator.reset();
            for (DecoratorResult result : decoratorResults) {
                if (result.decorator.shouldDecorate(dayView.getDate())) {
                    result.result.applyTo(facadeAccumulator);
                }
            }
            dayView.applyFacade(facadeAccumulator);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof DayView) {
            final DayView dayView = (DayView) v;
            mcv.onDateClicked(dayView);
        }
    }

    /*
     * Custom ViewGroup Code
     */

    /**
     * {@inheritDoc}
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public int mHeightHeader = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        //We expect to be somewhere inside a MaterialCalendarView, which should measure EXACTLY
        if (specHeightMode == MeasureSpec.UNSPECIFIED || specWidthMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("CalendarPagerView should never be left to decide it's size");
        }

        //The spec width should be a correct multiple
        final int measureTileWidth = specWidthSize / DEFAULT_DAYS_IN_WEEK;
        mHeightHeader = (specHeightSize / getRows()) / 2;
        if (mHeightHeader > -1) {
            final int measureTileHeight = (specHeightSize - mHeightHeader) / (getRows() - 1);
            //Just use the spec sizes
            setMeasuredDimension(specWidthSize, specHeightSize);
            int count = getChildCount();

            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);

                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                        measureTileWidth,
                        MeasureSpec.EXACTLY
                );
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        measureTileHeight,
                        MeasureSpec.EXACTLY
                );
                if (mHeightHeader != -1 && i < 7) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            mHeightHeader,
                            MeasureSpec.EXACTLY
                    );
                } else {
//                    child.setBackgroundColor(Color.RED);
//                    child.requestLayout();
                }

                if (child.getTag() != null) { // lines

                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            specWidthSize,
                            MeasureSpec.EXACTLY
                    );
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            2,
                            MeasureSpec.EXACTLY
                    );

                    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                } else { // item
                    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                }
//                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            }
            return;
        }
        final int measureTileHeight = specHeightSize / getRows();

        //Just use the spec sizes
        setMeasuredDimension(specWidthSize, specHeightSize);

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    measureTileWidth,
                    MeasureSpec.EXACTLY
            );

            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    measureTileHeight,
                    MeasureSpec.EXACTLY
            );

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    /**
     * Return the number of rows to display per page
     * @return
     */
    protected abstract int getRows();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final int parentLeft = 0;

        int childTop = 0;
        int childLeft = parentLeft;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getTag() != null) { // lines
//                child.layout(0, childTop, width, childTop + height);
            } else { // items
                child.layout(childLeft, childTop, childLeft + width, childTop + height);

                childLeft += width;

                //We should warp every so many children
                if (i % DEFAULT_DAYS_IN_WEEK == (DEFAULT_DAYS_IN_WEEK - 1)) {
                    childLeft = parentLeft;
                    childTop += height;
                    onLayoutLine(i, childTop);
                }
            }
        }
    }

    private void onLayoutLine(int index, int childTop) {
        int row = index / DEFAULT_DAYS_IN_WEEK;
        if (row >= 1) {
            View view = getChildAt(getChildCount() - row);
            if (view.getTag() != null) {
                view.layout(0, childTop, view.getMeasuredWidth(), childTop + view.getMeasuredHeight());

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        canvas.drawLine(0, 500, 1000, 500, paint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams();
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams();
    }


    @Override
    public void onInitializeAccessibilityEvent(@NonNull AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CalendarPagerView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CalendarPagerView.class.getName());
    }

    protected CalendarDay getFirstViewDay() {
        return firstViewDay;
    }

    /**
     * Simple layout params class for MonthView, since every child is the same size
     */
    protected static class LayoutParams extends MarginLayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams() {
            super(WRAP_CONTENT, WRAP_CONTENT);
        }
    }
}
