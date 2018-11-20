package com.example.sharon.beagroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setID(Context ctx, String user_id)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("USER", user_id).apply();
    }

    public static String getID(Context ctx)
    {
        return getSharedPreferences(ctx).getString("USER", "null");
    }

    public static void setName(Context ctx, String user_name)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("name", user_name).apply();
    }

    public static String getName(Context ctx)
    {
        return getSharedPreferences(ctx).getString("name", "null");
    }

    public static void setLog(Context ctx, boolean status)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean("logged", status).apply();
    }

    public static boolean getLog(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean("logged", false);
    }

    public static void setToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("token", token).apply();
    }

    public static String getToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString("token", "null");
    }

    public static void clear(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear().commit();
    }

    //Erika 20181018 每次都入給予一個Unique的group_ID
    /**Set Group_ID**/

    public static void setGroup_ID(Context ctx, String group_ID){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("Group_ID", group_ID).apply();
    }

    public static String getGroup_ID(Context ctx){
        return getSharedPreferences(ctx).getString("Group_ID","null");
    }

    /**Set friendCheckCode**/

    public static void setFriendCheckCode(Context ctx, String friendCheckCode){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("FriendCheckCode", friendCheckCode).apply();
    }

    public static String getFriendCheckCode(Context ctx){
        return getSharedPreferences(ctx).getString("FriendCheckCode","null");
    }
}
