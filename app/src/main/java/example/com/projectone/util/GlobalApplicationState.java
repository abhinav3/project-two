package example.com.projectone.util;

import android.app.Application;

/**
 * Created by Abhinav Ravi on 27/11/16.
 */

//TODO : use the application class to save global variables.
class GlobalApplicationState extends Application {

    private String myGlobalState;

    public String getGlobalState(){
        return myGlobalState;
    }
    public void setGlobalState(String s){
        myGlobalState = s;
    }
}
