package uk.co.diegobarle.funarticles.articles

import androidx.lifecycle.LiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
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
import uk.co.diegobarle.funarticles.R
import uk.co.diegobarle.funarticles.TestFunArticlesApp
import uk.co.diegobarle.funarticles.di.TestAppComponent
import uk.co.diegobarle.funarticles.network.DataResult
import uk.co.diegobarle.funarticles.repositories.ArticlesRep
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class ArticlesActivityTest{

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
    fun loadArticles_checkArticlesAreDisplayed() = runBlockingTest {
        fakeArticlesRep.saveArticle(
            Article(1, "Article 1", "Article 1 subtitle", "Article 1 body", "16/1/2019"),
            Article(2, "Article 2", "Article 2 subtitle", "Article 2 body", "17/1/2019"))

        // GIVEN - On the articles screen
        activityRule.launchActivity(null)

        //WHEN - Data is loaded

        // CHECK - 2 Articles are displayed with the correct data
        Espresso.onView(withId(R.id.listArticles))
            .check(matches(ViewMatchers.hasChildCount(2)))

        Espresso.onView(withId(R.id.listArticles))
            .check(matches(ViewMatchers.hasDescendant(withText("Article 1"))))

        Espresso.onView(withId(R.id.listArticles))
            .check(matches(ViewMatchers.hasDescendant(withText("Article 2 subtitle"))))
    }

    @Test
    fun loadNoArticles_checkListEmpty() = runBlockingTest {
        // GIVEN - On the articles screen
        activityRule.launchActivity(null)

        //WHEN - Data is loaded

        // CHECK - Empty articles list displayed
        onData(instanceOf(Article::class.java))
            .inAdapterView(allOf(withId(android.R.id.list), ViewMatchers.hasChildCount(0)))
    }

    @Test
    fun loadArticlesError_checkReloadButtonDisplayed() = runBlockingTest {

        `when`(articlesRep.getArticles()).thenReturn(fakeArticlesRep.getArticlesError())

        // GIVEN - On the articles screen
        activityRule.launchActivity(null)

        //WHEN - Data returned an error

        // CHECK - Reload button displayed
        Espresso.onView(withId(R.id.reloadButton)).check(matches(ViewMatchers.isDisplayed()))
    }
}