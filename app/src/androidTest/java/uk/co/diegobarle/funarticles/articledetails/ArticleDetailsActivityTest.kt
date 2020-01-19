package uk.co.diegobarle.funarticles.articledetails

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
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
import uk.co.diegobarle.funarticles.R
import uk.co.diegobarle.funarticles.TestFunArticlesApp
import uk.co.diegobarle.funarticles.di.TestAppComponent
import uk.co.diegobarle.funarticles.network.DataResult
import uk.co.diegobarle.funarticles.repositories.ArticlesRep
import uk.co.diegobarle.funarticles.util.ARTICLE_ID_BUNDLE
import uk.co.diegobarle.funarticles.util.ARTICLE_TITLE_BUNDLE
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class ArticleDetailsActivityTest{

    @get:Rule
    var activityRule: ActivityTestRule<ArticleDetailsActivity> =
        ActivityTestRule(ArticleDetailsActivity::class.java, false, false)

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

        val answer: Answer<LiveData<DataResult<Article>>> = Answer {
            val id = it.getArgument<Long>(0)
            fakeArticlesRep.getArticle(id)
        }

        `when`(articlesRep.getArticle(ArgumentMatchers.anyLong())).thenAnswer(answer)
    }

    @Test
    fun loadArticle_checkArticleDataIsDisplayed() = runBlockingTest {
        fakeArticlesRep.saveArticle(
            Article(1, "Article 1", "Article 1 subtitle", "Article 1 body", "16/1/2019"))

        // GIVEN - On the article screen
        val intent = Intent().apply {
            putExtra(ARTICLE_ID_BUNDLE, 1L)
            putExtra(ARTICLE_TITLE_BUNDLE, "Article 1")
        }
        activityRule.launchActivity(intent)

        //WHEN - Article data is loaded

        // CHECK - Article body is displayed correctly

        Espresso.onView(withId(R.id.tvBody))
            .check(matches(withText("Article 1 body")))
    }

    @Test
    fun loadNonExistingArticle_checkErrorMessage() = runBlockingTest {
        // GIVEN - On the article screen
        val intent = Intent().apply {
            putExtra(ARTICLE_ID_BUNDLE, 1L)
            putExtra(ARTICLE_TITLE_BUNDLE, "Article 1")
        }
        activityRule.launchActivity(intent)

        //WHEN - No article data is loaded

        // CHECK - Error message displayed
        Espresso.onView(withId(R.id.tvBody))
            .check(matches(withText(activityRule.activity.getString(R.string.missing_data_error))))
    }

    @Test
    fun loadArticleError_checkErrorMessage() = runBlockingTest {

        `when`(articlesRep.getArticle(1L)).thenReturn(fakeArticlesRep.getArticleError())

        // GIVEN - On the article screen
        val intent = Intent().apply {
            putExtra(ARTICLE_ID_BUNDLE, 1L)
            putExtra(ARTICLE_TITLE_BUNDLE, "Article 1")
        }
        activityRule.launchActivity(intent)

        //WHEN - Data returned an error

        // CHECK - Error message displayed
        Espresso.onView(withId(R.id.tvBody))
            .check(matches(withText(activityRule.activity.getString(R.string.missing_data_error))))
    }

    @Test
    fun loadArticleMissingBody_displayLoadingMessage() = runBlockingTest {
        fakeArticlesRep.saveArticle(
            Article(1, "Article 1", "Article 1 subtitle", null, "16/1/2019"))

        // GIVEN - On the article screen
        val intent = Intent().apply {
            putExtra(ARTICLE_ID_BUNDLE, 1L)
            putExtra(ARTICLE_TITLE_BUNDLE, "Article 1")
        }
        activityRule.launchActivity(intent)

        //WHEN - Article returned missing body

        // CHECK - Loading details message displayed
        Espresso.onView(withId(R.id.tvBody))
            .check(matches(withText(activityRule.activity.getString(R.string.loading_details_message))))
    }
}