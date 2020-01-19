package uk.co.diegobarle.funarticles

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uk.co.diegobarle.funarticles.model.Article
import uk.co.diegobarle.funarticles.model.FakeArticleDao
import uk.co.diegobarle.funarticles.repositories.FakeArticlesRep
import org.junit.Rule
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.stubbing.Answer
import uk.co.diegobarle.funarticles.articles.ArticlesActivity
import uk.co.diegobarle.funarticles.di.TestAppComponent
import uk.co.diegobarle.funarticles.network.DataResult
import uk.co.diegobarle.funarticles.repositories.ArticlesRep
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class FunArticlesAppTest{

    @get:Rule
    var activityRule: ActivityTestRule<ArticlesActivity> =
        ActivityTestRule(ArticlesActivity::class.java, false, false)

    @Inject
    lateinit var articlesRep: ArticlesRep

    lateinit var fakeArticlesRep: FakeArticlesRep

    @Before
    fun setup(){
        initFakeRepository()
        injectTest()
        mockVMRepository()
    }

    private fun initFakeRepository(){
        fakeArticlesRep = FakeArticlesRep(FakeArticleDao())
    }

    private fun injectTest(){
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestFunArticlesApp
        (app.appComponent as TestAppComponent).inject(this)
    }

    private fun mockVMRepository(){
        `when`(articlesRep.getArticles()).thenReturn(fakeArticlesRep.getArticles())

        val answer: Answer<LiveData<DataResult<Article>>> = Answer {
            val id = it.getArgument<Long>(0)
            fakeArticlesRep.getArticle(id)
        }

        `when`(articlesRep.getArticle(ArgumentMatchers.anyLong())).thenAnswer(answer)
    }

    @Test
    fun clickArticle_navigateToArticleDetail() = runBlockingTest {
        fakeArticlesRep.saveArticle(
            Article(1, "Article 1", "Article 1 subtitle", "Article 1 body", "16/1/2019"),
            Article(2, "Article 2", "Article 2 subtitle", "Article 2 body", "17/1/2019"))

        // GIVEN - On the home screen
        activityRule.launchActivity(null)

        // WHEN - Click on the list item with name "Article 1"
        Espresso.onView(ViewMatchers.withId(R.id.listArticles))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(withText("Article 1")), ViewActions.click()
                ))

        // THEN - Verify that we navigate to the "Article 1" detail screen
        Espresso.onView(ViewMatchers.withId(R.id.tvBody))
            .check(matches(withText("Article 1 body")))
    }

    @Test
    fun errorArticles_reloadArticles() = runBlockingTest {
        fakeArticlesRep.saveArticle(
            Article(1, "Article 1", "Article 1 subtitle", "Article 1 body", "16/1/2019"),
            Article(2, "Article 2", "Article 2 subtitle", "Article 2 body", "17/1/2019"))

        `when`(articlesRep.getArticles()).thenReturn(fakeArticlesRep.getArticlesError())
        // GIVEN - On the home screen
        activityRule.launchActivity(null)

        //WHEN - Articles load with error

        //THEN - Check reload button displayed
        Espresso.onView(ViewMatchers.withId(R.id.reloadButton))
            .check(matches(ViewMatchers.isDisplayed()))

        `when`(articlesRep.getArticles()).thenReturn(fakeArticlesRep.getArticles())
        // WHEN - Click on reload button
        Espresso.onView(ViewMatchers.withId(R.id.reloadButton))
            .perform(ViewActions.click())

        // THEN - Verify that articles are reload
        Espresso.onView(ViewMatchers.withId(R.id.listArticles))
            .check(matches(ViewMatchers.hasChildCount(2)))
    }
}