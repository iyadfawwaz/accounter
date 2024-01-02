package io.github.iyado.accounterx;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CompanyRef {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences saveCompanyData(Activity activity,String s){
       // SharedPreferences
                sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
              //  context.getSharedPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("company",s);
        editor.apply();
        return sharedPreferences;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
