package com.banana.movierankinkorea;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.banana.movierankinkorea.activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.banana.movierankinkorea", appContext.getPackageName());
    }


    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.grid_recyclerview)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldBeAbleToLoadMoviesInThreeSeconds() throws InterruptedException {
        onView(withId(R.id.grid_recyclerview)).check(matches(isDisplayed()));
        Thread.sleep(3000);
        //Matcher<RecyclerView.ViewHolder> matcher = TestUtils.viewHolderItemContainsString("10. ");
        onView(withId(R.id.grid_recyclerview)).perform(RecyclerViewActions.scrollToPosition(9));
        onView(TestUtils.withRecyclerView(R.id.grid_recyclerview).atPosition(9)).check(matches(isClickable()));
    }

    @Test
    public void shouldBeAbleToLaunchDetailScreen() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.grid_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(7, click()));
        Thread.sleep(500);
        onView(withId(R.id.movie_poster)).check(matches(isDisplayed()));
        onView(withText("줄거리")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldBeAbleToLoadDataInDetailScreen() throws InterruptedException {
        //check if 'story' is loaded properly
        Thread.sleep(2500);
        onView(withId(R.id.grid_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(7, click()));
        Thread.sleep(500);
        onView(withId(R.id.movie_poster)).check(matches(isDisplayed()));
        onView(withText("줄거리")).check(matches(isDisplayed()));
        onView(withId(R.id.movie_description)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_description)).check(matches(withText(TestUtils.getText(withId(R.id.movie_description)))));
    }

    @Test
    public void shouldBeAbleStartMovieDetailActivity() throws InterruptedException {
//        onView(withId(R.id.grid_recyclerview)).perform();
//        onView(withText("Summary")).check(matches(isDisplayed()));
    }

}
