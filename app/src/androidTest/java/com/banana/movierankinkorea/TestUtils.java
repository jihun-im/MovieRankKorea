package com.banana.movierankinkorea;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Created by Jihun on 2017-07-11.
 */

public class TestUtils {
    public static Matcher<RecyclerView.ViewHolder> viewHolderWithTitle(final String title) {
        return new BoundedMatcher<RecyclerView.ViewHolder, MovieRVAdapter.MovieViewHolder>(MovieRVAdapter.MovieViewHolder.class) {
            @Override
            protected boolean matchesSafely(MovieRVAdapter.MovieViewHolder item) {
                //return item.movieTitleTextView.getText().toString().contains(title);
                return item.movieTitleTextView.getText().toString().equalsIgnoreCase(title);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("view holder with title: " + title);
            }
        };
    }
    public static Matcher<RecyclerView.ViewHolder> viewHolderItemContainsString(final String string) {
        return new BoundedMatcher<RecyclerView.ViewHolder, MovieRVAdapter.MovieViewHolder>(MovieRVAdapter.MovieViewHolder.class) {
            @Override
            protected boolean matchesSafely(MovieRVAdapter.MovieViewHolder item) {
                return item.movieTitleTextView.getText().toString().contains(string);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("view holder contains string: " + string);
            }
        };
    }
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
    static public String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
    static public Matcher<View> checkConversion(final float value){
        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View item) {
                if(!(item instanceof TextView)) return false;

                float convertedValue = Float.valueOf(((TextView) item).getText().toString());
                float delta = Math.abs(convertedValue - value);

                return delta < 0.005f;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Value expected is wrong");
            }
        };
    }
    static boolean isGradeHigherOrEqualsTo(String s1, String s2) {
        if (Float.parseFloat(s1.split("점")[0]) >= Float.parseFloat(s2.split("점")[0])) {
            return true;
        }
        return false;
    }
}
