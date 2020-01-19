package uk.co.diegobarle.funarticles

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class FunArticlesUnitRunner: AndroidJUnitRunner(){
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestFunArticlesApp::class.java.name, context)
    }
}